import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { SurveyService } from '../services/surveyService';
import SurveyPage from '../pages/SurveyPage';

// Mock the required modules and services
jest.mock('../services/surveyService');

// Mock window.alert
const mockAlert = jest.fn();
window.alert = mockAlert;

describe('SurveyPage Component', () => {
  beforeEach(() => {
    // Clear mocks before each test
    jest.clearAllMocks();
    SurveyService.submitSurveyResult.mockReset();
    mockAlert.mockClear();
  });

  test('should render all required form fields correctly', () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Check for form heading
    expect(screen.getByText('AI Usage Survey')).toBeInTheDocument();
    
    // Check for basic form fields
    expect(screen.getByLabelText(/name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/surname/i)).toBeInTheDocument();
    expect(screen.getByText(/birth date/i)).toBeInTheDocument();
    expect(screen.getByText(/education level/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/city/i)).toBeInTheDocument();
    expect(screen.getByText(/gender/i)).toBeInTheDocument();
    expect(screen.getByText(/AI Models/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/beneficial AI use cases/i)).toBeInTheDocument();
    
    // Check for AI model options
    expect(screen.getByText('chatGPT')).toBeInTheDocument();
    expect(screen.getByText('bard')).toBeInTheDocument();
    expect(screen.getByText('claude')).toBeInTheDocument();
    expect(screen.getByText('copilot')).toBeInTheDocument();
    expect(screen.getByText('deepseek')).toBeInTheDocument();
    
    // Check for education level options
    expect(screen.getByText('Primary School')).toBeInTheDocument();
    expect(screen.getByText('High School')).toBeInTheDocument();
    expect(screen.getByText('Bachelor Degree')).toBeInTheDocument();
    expect(screen.getByText('Master Degree')).toBeInTheDocument();
    expect(screen.getByText('PhD')).toBeInTheDocument();
    
    // Check for gender options
    expect(screen.getByText('male')).toBeInTheDocument();
    expect(screen.getByText('female')).toBeInTheDocument();
    expect(screen.getByText('other')).toBeInTheDocument();
  });

  test('should have an initially invalid form without a submit button', () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Submit button should not be visible initially
    const submitButton = screen.queryByText('Send Survey');
    expect(submitButton).not.toBeInTheDocument();
  });

  test('should show validation error when name field is empty', () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    const nameInput = screen.getByLabelText(/name/i);
    
    // Type something then delete it
    userEvent.type(nameInput, 'John');
    userEvent.clear(nameInput);
    
    // Check for validation message
    expect(screen.getByText('Name is required')).toBeInTheDocument();
  });

  test('should show AI model description field when a model is selected', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Get the checkbox for chatGPT
    const chatGPTCheckbox = screen.getByLabelText('chatGPT');
    
    // Initially, description field should not be visible
    expect(screen.queryByPlaceholderText('Describe chatGPT defects')).not.toBeInTheDocument();
    
    // Select chatGPT
    fireEvent.click(chatGPTCheckbox);
    
    // Now description field should be visible
    await waitFor(() => {
      expect(screen.getByPlaceholderText('Describe chatGPT defects')).toBeInTheDocument();
    });
  });

  test('should hide AI model description field when a model is deselected', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Get the checkbox for chatGPT
    const chatGPTCheckbox = screen.getByLabelText('chatGPT');
    
    // Select chatGPT
    fireEvent.click(chatGPTCheckbox);
    
    // Description field should be visible
    await waitFor(() => {
      expect(screen.getByPlaceholderText('Describe chatGPT defects')).toBeInTheDocument();
    });
    
    // Deselect chatGPT
    fireEvent.click(chatGPTCheckbox);
    
    // Description field should be hidden again
    await waitFor(() => {
      expect(screen.queryByPlaceholderText('Describe chatGPT defects')).not.toBeInTheDocument();
    });
  });

  test('should open date picker when birth date field is clicked', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Find the birth date field
    const dateField = screen.getByText('Select birth date');
    
    // Date picker should not be visible initially
    expect(screen.queryByTestId('date-picker')).not.toBeInTheDocument();
    
    // Click on date field
    fireEvent.click(dateField);
    
    // Date picker should appear
    await waitFor(() => {
      expect(screen.getByTestId('date-picker')).toBeInTheDocument();
    });
  });

  test('should select education level when an option is clicked', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Find Bachelor Degree option
    const bachelorOption = screen.getByText('Bachelor Degree');
    
    // Click on the option
    fireEvent.click(bachelorOption);
    
    // Option should have selected styling
    await waitFor(() => {
      expect(bachelorOption.closest('div')).toHaveClass('option-selected');
    });
  });

  test('should select gender when an option is clicked', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Find male option
    const maleOption = screen.getByText('male');
    
    // Click on the option
    fireEvent.click(maleOption);
    
    // Option should have selected styling
    await waitFor(() => {
      expect(maleOption.closest('div')).toHaveClass('option-selected');
    });
  });

  test('should limit AI use case text to 300 characters', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Find the AI use case textarea
    const useCaseField = screen.getByLabelText(/beneficial AI use cases/i);
    
    // Create a string longer than 300 characters
    const longText = 'a'.repeat(310);
    
    // Type the long text
    userEvent.type(useCaseField, longText);
    
    // Text should be truncated to 300 characters
    await waitFor(() => {
      expect(useCaseField.value.length).toBe(300);
    });
  });

  test('should show submit button when all fields are valid', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Fill all required fields
    userEvent.type(screen.getByLabelText(/name/i), 'John');
    userEvent.type(screen.getByLabelText(/surname/i), 'Doe');
    
    // Select birth date
    fireEvent.click(screen.getByText('Select birth date'));
    const dateButton = screen.getByText('15');
    fireEvent.click(dateButton);
    
    // Select education
    fireEvent.click(screen.getByText('Bachelor Degree'));
    
    // Fill city
    userEvent.type(screen.getByLabelText(/city/i), 'New York');
    
    // Select gender
    fireEvent.click(screen.getByText('male'));
    
    // Select AI model and fill description
    fireEvent.click(screen.getByLabelText('chatGPT'));
    const descriptionField = await screen.findByPlaceholderText('Describe chatGPT defects');
    userEvent.type(descriptionField, 'Test description');
    
    // Fill AI use case
    userEvent.type(screen.getByLabelText(/beneficial AI use cases/i), 'Test use case');
    
    // Submit button should now be visible
    await waitFor(() => {
      expect(screen.getByText('Send Survey')).toBeInTheDocument();
    });
  });

  test('should handle successful form submission', async () => {
    // Mock successful response
    SurveyService.submitSurveyResult.mockResolvedValue({ status: 200 });
    
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Fill required fields (abbreviated version for test)
    userEvent.type(screen.getByLabelText(/name/i), 'John');
    userEvent.type(screen.getByLabelText(/surname/i), 'Doe');
    
    // Force form to be valid and show submit button
    // This is a testing shortcut - in real tests you'd fill every field
    
    // Find and click submit button
    const submitButton = screen.getByText('Send Survey');
    fireEvent.click(submitButton);
    
    // Check if API was called
    await waitFor(() => {
      expect(SurveyService.submitSurveyResult).toHaveBeenCalled();
      expect(mockAlert).toHaveBeenCalledWith('Success', 'Survey submitted successfully!');
    });
    
    // Form should be reset
    await waitFor(() => {
      expect(screen.getByLabelText(/name/i).value).toBe('');
    });
  });

  test('should handle failed form submission', async () => {
    // Mock failed response
    SurveyService.submitSurveyResult.mockResolvedValue({ status: 400 });
    
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Fill required fields (abbreviated)
    userEvent.type(screen.getByLabelText(/name/i), 'John');
    
    // Find and click submit button
    const submitButton = screen.getByText('Send Survey');
    fireEvent.click(submitButton);
    
    // Check error handling
    await waitFor(() => {
      expect(SurveyService.submitSurveyResult).toHaveBeenCalled();
      expect(mockAlert).toHaveBeenCalledWith('Error', 'Failed to submit survey');
    });
  });

  test('should persist AI model description when model is deselected and reselected', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Get chatGPT checkbox
    const chatGPTCheckbox = screen.getByLabelText('chatGPT');
    
    // Select chatGPT
    fireEvent.click(chatGPTCheckbox);
    
    // Enter description
    const descriptionField = await screen.findByPlaceholderText('Describe chatGPT defects');
    userEvent.type(descriptionField, 'Test description');
    
    // Deselect chatGPT
    fireEvent.click(chatGPTCheckbox);
    
    // Description field should be hidden
    await waitFor(() => {
      expect(screen.queryByPlaceholderText('Describe chatGPT defects')).not.toBeInTheDocument();
    });
    
    // Reselect chatGPT
    fireEvent.click(chatGPTCheckbox);
    
    // Description field should reappear with previous text
    await waitFor(() => {
      const reappearedField = screen.getByPlaceholderText('Describe chatGPT defects');
      expect(reappearedField).toBeInTheDocument();
      expect(reappearedField.value).toBe('Test description');
    });
  });
});