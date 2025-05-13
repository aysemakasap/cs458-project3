// src/tests/SurveyPage.test.js
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { SurveyService } from '../services/SurveyService';
import SurveyPage from '../pages/SurveyPage';

// Mock the required modules and services
jest.mock('../services/SurveyService');

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
    expect(screen.getByLabelText('Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Surname')).toBeInTheDocument();
    expect(screen.getByText('Birth Date')).toBeInTheDocument();
    expect(screen.getByText(/Education Level/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/City/i)).toBeInTheDocument();
    expect(screen.getByText(/Gender/i)).toBeInTheDocument();
    expect(screen.getByText(/AI Models/i)).toBeInTheDocument();
    expect(screen.getByLabelText('Beneficial AI Use Cases')).toBeInTheDocument();
    
    // Check for AI model options
    expect(screen.getByText('chatGPT')).toBeInTheDocument();
    expect(screen.getByText('bard')).toBeInTheDocument();
    expect(screen.getByText('claude')).toBeInTheDocument();
    expect(screen.getByText('copilot')).toBeInTheDocument();
    expect(screen.getByText('deepseek')).toBeInTheDocument();
    
    // Check for education options
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

    const nameInput = screen.getByLabelText('Name');
    
    // Type something then delete it
    userEvent.type(nameInput, 'John');
    userEvent.clear(nameInput);
    
    // Trigger blur event to validate
    fireEvent.blur(nameInput);
    
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

  // FIXED: Updated test to work with HTML5 date input
  test('should handle date input selection', () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Find the date input
    const dateInput = screen.getByLabelText('Birth Date');
    
    // Set a date value
    fireEvent.change(dateInput, { target: { value: '2000-01-15' } });
    
    // Verify the date was set
    expect(dateInput.value).toBe('2000-01-15');
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
    const useCaseField = screen.getByLabelText('Beneficial AI Use Cases');
    
    // Create a string longer than 300 characters
    const longText = 'a'.repeat(310);
    
    // Type the long text
    userEvent.type(useCaseField, longText);
    
    // Text should be truncated to 300 characters
    await waitFor(() => {
      expect(useCaseField.value.length).toBe(300);
    });
  });

  // FIXED: Updated test to work with HTML5 date input
  test('should show submit button when all fields are valid', async () => {
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Fill all required fields
    userEvent.type(screen.getByLabelText('Name'), 'John');
    userEvent.type(screen.getByLabelText('Surname'), 'Doe');
    
    // Select birth date using HTML5 date input
    const dateInput = screen.getByLabelText('Birth Date');
    fireEvent.change(dateInput, { target: { value: '2000-01-15' } });
    
    // Select education level
    fireEvent.click(screen.getByText('Bachelor Degree'));
    
    // Fill city
    userEvent.type(screen.getByLabelText(/City/i), 'New York');
    
    // Select gender
    fireEvent.click(screen.getByText('male'));
    
    // Select AI model
    fireEvent.click(screen.getByLabelText('chatGPT'));
    
    // Enter model description
    const descriptionField = await screen.findByPlaceholderText('Describe chatGPT defects');
    userEvent.type(descriptionField, 'Sometimes provides inaccurate information');
    
    // Fill AI use case
    userEvent.type(screen.getByLabelText('Beneficial AI Use Cases'), 'Helps with research and generating creative content');
    
    // Now the submit button should be visible
    await waitFor(() => {
      expect(screen.getByText('Send Survey')).toBeInTheDocument();
    });
  });

  // FIXED: Updated test to work with HTML5 date input
  test('should handle successful form submission', async () => {
    // Mock successful submission response
    SurveyService.submitSurveyResult.mockResolvedValue({ status: 200 });
    
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Fill required fields and make form valid
    userEvent.type(screen.getByLabelText('Name'), 'John');
    userEvent.type(screen.getByLabelText('Surname'), 'Doe');
    
    // Select birth date using HTML5 date input
    const dateInput = screen.getByLabelText('Birth Date');
    fireEvent.change(dateInput, { target: { value: '2000-01-15' } });
    
    // Select education
    fireEvent.click(screen.getByText('Bachelor Degree'));
    
    // Fill city
    userEvent.type(screen.getByLabelText(/City/i), 'New York');
    
    // Select gender
    fireEvent.click(screen.getByText('male'));
    
    // Select AI model
    fireEvent.click(screen.getByLabelText('chatGPT'));
    
    // Fill description
    const descriptionField = await screen.findByPlaceholderText('Describe chatGPT defects');
    userEvent.type(descriptionField, 'Test description');
    
    // Fill AI use case
    userEvent.type(screen.getByLabelText('Beneficial AI Use Cases'), 'Test use case');
    
    // Submit button should now be visible
    const submitButton = await screen.findByText('Send Survey');
    fireEvent.click(submitButton);
    
    // Check if API was called and alert shown
    await waitFor(() => {
      expect(SurveyService.submitSurveyResult).toHaveBeenCalled();
      expect(mockAlert).toHaveBeenCalledWith('Success', 'Survey submitted successfully!');
    });
    
    // Form should be reset
    await waitFor(() => {
      expect(screen.getByLabelText('Name').value).toBe('');
    });
  });

  // FIXED: Updated test to work with HTML5 date input
  test('should handle failed form submission', async () => {
    // Mock failed submission response
    SurveyService.submitSurveyResult.mockResolvedValue({ status: 400 });
    
    render(
      <BrowserRouter>
        <SurveyPage />
      </BrowserRouter>
    );

    // Fill required fields and make form valid
    userEvent.type(screen.getByLabelText('Name'), 'John');
    userEvent.type(screen.getByLabelText('Surname'), 'Doe');
    
    // Select birth date using HTML5 date input
    const dateInput = screen.getByLabelText('Birth Date');
    fireEvent.change(dateInput, { target: { value: '2000-01-15' } });
    
    // Select education
    fireEvent.click(screen.getByText('Bachelor Degree'));
    
    // Fill city
    userEvent.type(screen.getByLabelText(/City/i), 'New York');
    
    // Select gender
    fireEvent.click(screen.getByText('male'));
    
    // Select AI model
    fireEvent.click(screen.getByLabelText('chatGPT'));
    
    // Fill description
    const descriptionField = await screen.findByPlaceholderText('Describe chatGPT defects');
    userEvent.type(descriptionField, 'Test description');
    
    // Fill AI use case
    userEvent.type(screen.getByLabelText('Beneficial AI Use Cases'), 'Test use case');
    
    // Submit button should now be visible
    const submitButton = await screen.findByText('Send Survey');
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
    fireEvent.change(descriptionField, { target: { value: 'Test description' } });
    
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