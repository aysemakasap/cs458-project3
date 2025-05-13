import React from "react";
import { useNavigate } from "react-router-dom";

export default function AfterPage() {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 gap-6">
      <h1 className="text-2xl font-bold mb-4">What would you like to do?</h1>

      <button
        onClick={() => navigate("/survey")}
        className="px-6 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
      >
        Fill a Survey
      </button>

      <button
        onClick={() => navigate("/createCustomSurvey")}
        className="px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600"
      >
        Create a Survey
      </button>

      <button
        onClick={() => navigate("/goToCustomSurvey")}
        className="px-6 py-2 bg-purple-500 text-white rounded hover:bg-purple-600"
      >
        View Created Surveys
      </button>
    </div>
  );
}
