import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AuthService } from "../services/authService";

export default function Login() {
  // State hooks
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loginAttempts, setLoginAttempts] = useState(
    Number(localStorage.getItem("loginAttempts")) || 0
  );
  const [isBlocked, setIsBlocked] = useState(
    localStorage.getItem("isBlocked") === "true"
  );
  const [timer, setTimer] = useState(
    Number(localStorage.getItem("timer")) || 10
  );

  const navigate = useNavigate();
  
  // Email validation regex
  const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  // Validate email format
  const isValidEmail = (email) => {
    return EMAIL_REGEX.test(email);
  };

  // Handle form submission
  const handleLogin = async (e) => {
    e.preventDefault();
    
    // Validate email format
    if (!isValidEmail(email)) {
      setError("Email format is incorrect. Email must contain one '@', characters before '@' and after '@'.");
      return;
    }
    
    try {
      const response = await AuthService.login(email, password);
      
      if (response.ok) {
        alert("Login Successful!");
        navigate("/survey");
        resetLoginState();
      } else {
        handleFailedLogin();
      }
    } catch (error) {
      console.error("Login Failed:", error);
      setError("Login failed");
    }
  };

  // Handle failed login attempts
  const handleFailedLogin = () => {
    setError("Login failed. Wrong email or password.");
    const newAttempts = loginAttempts + 1;
    setLoginAttempts(newAttempts);
    localStorage.setItem("loginAttempts", newAttempts);
    
    // Block login after 5 failed attempts
    if (newAttempts >= 5) {
      setIsBlocked(true);
      localStorage.setItem("isBlocked", "true");
      startTimer();
    }
  };

  // Start countdown timer for blocked login
  const startTimer = () => {
    const interval = setInterval(() => {
      setTimer((prevTimer) => {
        const newTimer = prevTimer - 1;
        localStorage.setItem("timer", newTimer);

        if (newTimer === 0) {
          clearInterval(interval);
          resetLoginState();
        }
        return newTimer;
      });
    }, 1000);
  };

  // Reset login state
  const resetLoginState = () => {
    setLoginAttempts(0);
    setIsBlocked(false);
    setTimer(10);
    localStorage.removeItem("loginAttempts");
    localStorage.removeItem("isBlocked");
    localStorage.removeItem("timer");
  };

  // Clean up localStorage on component unmount
  useEffect(() => {
    return () => {
      localStorage.removeItem("loginAttempts");
      localStorage.removeItem("isBlocked");
      localStorage.removeItem("timer");
    };
  }, []);

  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        backgroundColor: "#f3f4f6",
      }}
    >
      <div
        style={{
          padding: "2rem",
          backgroundColor: "white",
          borderRadius: "1rem",
          boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
          width: "24rem",
        }}
      >
        <h1
          style={{
            fontSize: "1.5rem",
            fontWeight: "bold",
            marginBottom: "1.5rem",
            textAlign: "center",
          }}
        >
          Login
        </h1>
        {error && (
          <p
            id="error-message"
            style={{
              color: "#ef4444",
              textAlign: "center",
              marginBottom: "1rem",
            }}
          >
            {error}
          </p>
        )}
        <form
          onSubmit={handleLogin}
          style={{
            display: "flex",
            flexDirection: "column",
            gap: "1rem",
          }}
        >
          <input
            type="text"
            placeholder="Email or Phone Number"
            style={{
              width: "100%",
              padding: "0.75rem",
              border: "1px solid #e5e7eb",
              borderRadius: "0.5rem",
              outline: "none",
            }}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            style={{
              width: "100%",
              padding: "0.75rem",
              border: "1px solid #e5e7eb",
              borderRadius: "0.5rem",
              outline: "none",
            }}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button
            type="submit"
            disabled={isBlocked}
            style={{
              width: "100%",
              backgroundColor: "#3b82f6",
              color: "white",
              padding: "0.75rem",
              borderRadius: "0.5rem",
              border: "none",
              cursor: "pointer",
            }}
          >
            {isBlocked ? `Try again in ${timer} seconds` : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}