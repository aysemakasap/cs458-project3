// Constants for API endpoints
const API_BASE_URL = 'http://localhost:8080';
const API_ENDPOINTS = {
  LOGIN: `${API_BASE_URL}/api/login`
};

export class AuthService {
  /**
   * Authenticate user with email and password
   * @param {string} email - User email
   * @param {string} password - User password
   * @returns {Promise<Response>} - Fetch response
   */
  static login(email, password) {
    return fetch(API_ENDPOINTS.LOGIN, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });
  }
}