import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import CreateCustomSurvey from '../pages/CreateCustomSurveyPage';
import GoToCustomSurvey from '../pages/goToCustomSurvey';
import FillSurvey from '../pages/fillSurvey';

// Reset DB before each test (you should expose a cleanup endpoint in your backend for this in a test profile)
beforeEach(async () => {
  await fetch('http://localhost:8080/api/test/reset', { method: 'POST' });
});

describe('Full Survey Flow (Real API)', () => {
  test('CreateSurvey creates a valid survey with 5 types', async () => {
    render(
      <BrowserRouter>
        <CreateCustomSurvey />
      </BrowserRouter>
    );

    const addBtn = screen.getByText('Add Question');

    for (let type of ['OpenEnded', 'MultipleChoice', 'Dropdown', 'Checkbox', 'RatingScale']) {
      fireEvent.click(addBtn);
      const selects = screen.getAllByRole('combobox');
      fireEvent.change(selects[selects.length - 1], { target: { value: type } });

      const inputs = screen.getAllByPlaceholderText('Enter question text');
      fireEvent.change(inputs[inputs.length - 1], { target: { value: `${type} test question` } });

      if (['MultipleChoice', 'Dropdown', 'Checkbox'].includes(type)) {
        const optionInputs = screen.getAllByPlaceholderText('Enter option');
        fireEvent.change(optionInputs[optionInputs.length - 1], { target: { value: 'Option 1' } });
        fireEvent.click(screen.getAllByText('Add Option')[0]);
      }

      if (type === 'RatingScale') {
        const ratingInput = screen.getAllByRole('spinbutton')[0];
        fireEvent.change(ratingInput, { target: { value: 7 } });
      }
    }

    fireEvent.click(screen.getByText('Submit Survey'));

    await screen.findByText('Survey created!');
  });

  test('GoToCustomSurvey lists and navigates to last survey', async () => {
    render(
      <BrowserRouter>
        <GoToCustomSurvey />
      </BrowserRouter>
    );

    const listItems = await screen.findAllByRole('listitem');
    expect(listItems.length).toBeGreaterThan(0);
    fireEvent.doubleClick(listItems[listItems.length - 1]);
  });

  test('FillSurvey interaction and submission', async () => {
    // Assuming last created survey is accessible
    const res = await fetch('http://localhost:8080/api/surveys');
    const surveys = await res.json();
    const latestSurvey = surveys[surveys.length - 1];

    render(
      <BrowserRouter>
        <FillSurvey id={latestSurvey.id.toString()} />
      </BrowserRouter>
    );

    await screen.findByText(`Fill Out Survey #${latestSurvey.id}`);

    // Fill in answers
    for (const q of latestSurvey.questions) {
      if (q.type === 'OpenEnded') {
        fireEvent.change(screen.getByPlaceholderText('Write your response...'), {
          target: { value: 'Test open answer' }
        });
      }

      if (q.type === 'MultipleChoice') {
        fireEvent.click(screen.getAllByRole('checkbox')[0]);
      }

      if (q.type === 'Checkbox') {
        fireEvent.click(screen.getAllByRole('radio')[0]);
      }

      if (q.type === 'Dropdown') {
        fireEvent.change(screen.getByRole('combobox'), { target: { value: q.options[0] } });
      }

      if (q.type === 'RatingScale') {
        fireEvent.change(screen.getByRole('slider'), { target: { value: '8' } });
      }
    }

    fireEvent.click(screen.getByText('Submit Survey'));
    await screen.findByText('Survey submitted!');
  });
});
