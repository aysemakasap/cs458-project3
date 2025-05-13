import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import CreateCustomSurvey from '../pages/CreateCustomSurveyPage';

// Real API test
// You must ensure the backend is running at http://localhost:8080

beforeEach(async () => {
  // Optionally reset test state via backend endpoint
  await fetch('http://localhost:8080/api/test/reset', { method: 'POST' });
});

test('Create a survey with one question of each type', async () => {
  render(
    <BrowserRouter>
      <CreateCustomSurvey />
    </BrowserRouter>
  );

  const addBtn = screen.getByText('Add Question');

  const types = ['OpenEnded', 'MultipleChoice', 'Dropdown', 'Checkbox', 'RatingScale'];

  for (let type of types) {
    fireEvent.click(addBtn);
    const typeDropdowns = screen.getAllByRole('combobox');
    fireEvent.change(typeDropdowns[typeDropdowns.length - 1], { target: { value: type } });

    const questionInputs = screen.getAllByPlaceholderText('Enter question text');
    fireEvent.change(questionInputs[questionInputs.length - 1], {
      target: { value: `${type} test question` },
    });

    if (['MultipleChoice', 'Dropdown', 'Checkbox'].includes(type)) {
      const optionInputs = screen.getAllByPlaceholderText('Enter Option');
      fireEvent.change(optionInputs[optionInputs.length - 1], {
        target: { value: 'Option 1' },
      });
      fireEvent.click(screen.getAllByText('Add Option').at(-1));

      // Wait for new input and fill it in
      await waitFor(() => {
        const updatedOptions = screen.getAllByPlaceholderText('Enter Option');
        expect(updatedOptions.length).toBeGreaterThan(optionInputs.length);
        fireEvent.change(updatedOptions[updatedOptions.length - 1], {
          target: { value: 'Option 2' },
        });
      });
    }

    if (type === 'RatingScale') {
      const ratingInputs = screen.getAllByRole('spinbutton');
      fireEvent.change(ratingInputs[ratingInputs.length - 1], {
        target: { value: '4' },
      });
    }
  }

  fireEvent.click(screen.getByText('Submit Survey'));

  await waitFor(() => {
    expect(screen.getByText('Survey created!')).toBeInTheDocument();
  });
});
