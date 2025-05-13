// src/services/SurveyService.js
import axios from 'axios';

export class SurveyService {
  // Rename this method to match what you're calling in SurveyPage.js
  static async submitSurveyResult(surveyData) {
    try {
      const userToken = localStorage.getItem("userToken") || sessionStorage.getItem("userToken");
      const response = await axios.post(
        `http://localhost:8080/api/survey/save`,
        surveyData,
        {
          headers: {
            'Authorization': `Bearer ${userToken}`,
            'Content-Type': 'application/json'
          }
        }
      );
      return response;
    } catch (error) {
      return error.response;
    }
  }
}