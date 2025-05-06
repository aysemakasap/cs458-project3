import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { AuthService } from '../services/authService';
import Login from '../pages/login';

beforeAll(() => {
  window.alert = jest.fn();
});

afterAll(() => {
  window.alert.mockRestore();
});
// Mock the required modules and services
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn()
}));

// Removed Google OAuth and JWT mocks
jest.mock('../services/authService');

describe('Login Component', () => {
  beforeEach(() => {
    // Clear mocks before each test
    jest.clearAllMocks();
    // Clear localStorage before each test
    localStorage.clear();
  });

  test('should render login form correctly', () => {
    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    expect(screen.getByRole('heading', { name: /login/i })).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Email or Phone Number')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();
  });

  test('should validate email format', async () => {
    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Enter invalid email
    fireEvent.change(screen.getByPlaceholderText('Email or Phone Number'), {
      target: { value: 'invalid-email' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'password123' }
    });

    // Submit form
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    // Check for error message
    expect(screen.getByText(/Email format is incorrect/i)).toBeInTheDocument();
  });

  test('should handle successful login', async () => {
    // Mock successful login response
    AuthService.login.mockResolvedValue({ ok: true });

    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Enter valid credentials
    fireEvent.change(screen.getByPlaceholderText('Email or Phone Number'), {
      target: { value: 'test@example.com' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'password123' }
    });

    // Submit form
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    // Wait for login to complete
    await waitFor(() => {
      expect(AuthService.login).toHaveBeenCalledWith('test@example.com', 'password123');
    });
  });

  test('should handle failed login', async () => {
    // Mock failed login response
    AuthService.login.mockResolvedValue({ ok: false });

    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Enter valid credentials but will fail authentication
    fireEvent.change(screen.getByPlaceholderText('Email or Phone Number'), {
      target: { value: 'test@example.com' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'wrongpassword' }
    });

    // Submit form
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    // Wait for login attempt to complete
    await waitFor(() => {
      expect(screen.getByText(/Login failed. Wrong email or password./i)).toBeInTheDocument();
    });
  });

  test('should block login after 5 failed attempts', async () => {
    // Mock failed login response
    AuthService.login.mockResolvedValue({ ok: false });
    
    // Mock localStorage
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: jest.fn(),
        setItem: jest.fn(),
        removeItem: jest.fn(),
        clear: jest.fn()
      },
      writable: true
    });

    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    const emailInput = screen.getByPlaceholderText('Email or Phone Number');
    const passwordInput = screen.getByPlaceholderText('Password');
    const loginButton = screen.getByRole('button', { name: /login/i });

    // Simulate 5 failed login attempts
    for (let i = 0; i < 5; i++) {
      fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
      fireEvent.change(passwordInput, { target: { value: 'wrongpassword' } });
      
      // Force localStorage to return increasing attempts count
      window.localStorage.getItem.mockImplementation((key) => {
        if (key === 'loginAttempts') return i;
        if (key === 'isBlocked' && i >= 4) return 'true';
        return null;
      });
      
      fireEvent.click(loginButton);

      await waitFor(() => {
        expect(AuthService.login).toHaveBeenCalledWith('test@example.com', 'wrongpassword');
      });
    }

    // Force localStorage to simulate blocked state
    window.localStorage.getItem.mockImplementation((key) => {
      if (key === 'loginAttempts') return 5;
      if (key === 'isBlocked') return 'true';
      if (key === 'timer') return 10;
      return null;
    });
    
    // Rerender to pick up localStorage changes
    const { rerender } = render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Check if login is blocked
    await waitFor(() => {
      const updatedLoginButton = screen.getByRole('button', { name: /try again in/i });
      expect(updatedLoginButton).toBeDisabled();
    });
  });
}); 