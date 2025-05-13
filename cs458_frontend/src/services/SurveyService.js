// src/services/surveyService.js
export class SurveyService {
  static async submitSurveyResult(formData) {
    // In a real app, this would be an API call
    console.log('Submitting survey data:', formData);
    
    // For testing purposes, we'll just return a success response
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ status: 200 });
      }, 500);
    });
  }
}