import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import FillSurvey from '../pages/fillSurvey';

test('Fill the latest survey and submit it successfully', async () => {
  // Step 1: Fetch latest survey
  const allSurveysRes = await fetch('http://localhost:8080/api/surveys');
  const allSurveys = await allSurveysRes.json();
  const latestSurvey = allSurveys[allSurveys.length - 1];

  // Step 2: Render FillSurvey with URL param
  window.history.pushState({}, '', `/fillSurvey/${latestSurvey.id}`);

  render(
    <BrowserRouter>
      <Routes>
        <Route path="/fillSurvey/:id" element={<FillSurvey />} />
      </Routes>
    </BrowserRouter>
  );

  // Wait for survey to load
  await screen.findByText(`Fill Out Survey #${latestSurvey.id}`);

  // Step 3: Fill each question
  for (const q of latestSurvey.questions) {
    switch (q.type) {
      case 'OpenEnded':
        fireEvent.change(screen.getByPlaceholderText('Write your response...'), {
          target: { value: 'Test answer' },
        });
        break;

      case 'MultipleChoice':
        q.options.forEach((opt) => {
          const checkboxes = screen.getAllByRole('checkbox');
          const match = checkboxes.find((cb) =>
            cb.parentElement?.textContent?.includes(opt)
          );
          if (match) fireEvent.click(match);
        });
        break;

      case 'Checkbox':
        const radios = screen.getAllByRole('radio');
        const matchRadio = radios.find((rb) =>
          rb.parentElement?.textContent?.includes(q.options[0])
        );
        if (matchRadio) fireEvent.click(matchRadio);
        break;

      case 'Dropdown':
        fireEvent.change(screen.getByRole('combobox'), {
          target: { value: q.options[0] },
        });
        break;

      case 'RatingScale':
        const slider = screen.getByRole('slider');
        fireEvent.change(slider, { target: { value: 4 } });
        break;

      default:
        break;
    }
  }

  // Step 4: Submit
  fireEvent.click(screen.getByText('Submit Survey'));

  await waitFor(() => {
    expect(screen.getByText('Survey submitted!')).toBeInTheDocument();
  });
});
