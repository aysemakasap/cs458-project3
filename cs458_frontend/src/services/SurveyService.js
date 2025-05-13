
export class SurveyService {
  static async submitSurveyResult(surveyData) {
    try {
      const userToken = localStorage.getItem("userToken") || sessionStorage.getItem("userToken");
      const response = await fetch('http://localhost:8080/api/survey/save', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${userToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(surveyData)
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      
      return { status: 200, data: await response.json() };
    } catch (error) {
      console.error('Error submitting survey:', error);
      return { status: 500, error: error.message };
    }
  }
}