import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const GoToCustomSurvey = () => {
  const [surveys, setSurveys] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/api/surveys')
      .then((res) => res.json())
      .then((data) => setSurveys(data))
      .catch((err) => console.error('Failed to load surveys', err));
  }, []);

  const handleDoubleClick = (id) => {
    navigate(`/fillSurvey/${id}`);
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Available Custom Surveys</h2>
      <ul>
        {surveys.map((survey) => (
          <li
            key={survey.id}
            onDoubleClick={() => handleDoubleClick(survey.id)}
            style={{ cursor: 'pointer', marginBottom: '10px', border: '1px solid #ccc', padding: '10px' }}
          >
            Survey #{survey.id} - {survey.questions.length} Questions
          </li>
        ))}
      </ul>
    </div>
  );
};

export default GoToCustomSurvey;
