import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const FillSurvey = () => {
  const { id } = useParams();
  const [survey, setSurvey] = useState(null);
  const [answers, setAnswers] = useState({});

  useEffect(() => {
    fetch(`http://localhost:8080/api/surveys/${id}`)
      .then((res) => res.json())
      .then((data) => {
        setSurvey(data);
        const initialAnswers = {};
        data.questions.forEach(q => {
          initialAnswers[q.id] = q.type === 'RatingScale' ? q.rating || 5 : '';
        });
        setAnswers(initialAnswers);
      })
      .catch((err) => console.error('Failed to load survey', err));
  }, [id]);

  const handleChange = (questionId, value) => {
    setAnswers(prev => ({ ...prev, [questionId]: value }));
  };

  const handleMultipleChoiceChange = (questionId, option) => {
    const prev = answers[questionId]?.split(', ').filter(Boolean) || [];
    const updated = prev.includes(option)
      ? prev.filter((val) => val !== option)
      : [...prev, option];
    setAnswers(prev => ({ ...prev, [questionId]: updated.join(', ') }));
  };

  const handleSubmit = async () => {
    const updatedQuestions = survey.questions.map(q => {
      const answer = answers[q.id];
      return {
        ...q,
        openAnswer: ['OpenEnded', 'MultipleChoice', 'Checkbox', 'Dropdown'].includes(q.type) ? answer : null,
        rating: q.type === 'RatingScale' ? answer : null
      };
    });

    const filledSurvey = { ...survey, questions: updatedQuestions };

    const response = await fetch(`http://localhost:8080/api/surveys/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(filledSurvey)
    });

    if (response.ok) {
      alert('Survey submitted!');
    } else {
      alert('Submission failed.');
    }
  };

  if (!survey) return <p>Loading...</p>;

  return (
    <div style={{ padding: '20px' }}>
      <h2>Fill Out Survey #{survey.id}</h2>

      {survey.questions.map((q, index) => (
        <div key={q.id} style={{ marginBottom: '20px' }}>
          <p><strong>Q{index + 1}:</strong> {q.question} ({q.type})</p>

          {q.type === 'OpenEnded' && (
            <textarea
              rows={3}
              style={{ width: '100%' }}
              value={answers[q.id] || ''}
              onChange={(e) => handleChange(q.id, e.target.value)}
              placeholder="Write your response..."
            />
          )}

          {q.type === 'MultipleChoice' && (
            q.options.map((opt, i) => (
              <label key={i} style={{ display: 'block' }}>
                <input
                  type="checkbox"
                  checked={answers[q.id]?.includes(opt) || false}
                  onChange={() => handleMultipleChoiceChange(q.id, opt)}
                />
                {opt}
              </label>
            ))
          )}

          {q.type === 'Checkbox' && (
            q.options.map((opt, i) => (
              <label key={i} style={{ display: 'block' }}>
                <input
                  type="radio"
                  name={`checkbox-${q.id}`}
                  value={opt}
                  checked={answers[q.id] === opt}
                  onChange={() => handleChange(q.id, opt)}
                />
                {opt}
              </label>
            ))
          )}

          {q.type === 'Dropdown' && (
            <select value={answers[q.id] || ''} onChange={(e) => handleChange(q.id, e.target.value)}>
              <option value="">Select an option</option>
              {q.options.map((opt, i) => (
                <option key={i} value={opt}>{opt}</option>
              ))}
            </select>
          )}

          {q.type === 'RatingScale' && (
            <input
              type="range"
              min="1"
              max="10"
              value={answers[q.id] || 5}
              onChange={(e) => handleChange(q.id, Number(e.target.value))}
            />
          )}
        </div>
      ))}

      <button onClick={handleSubmit}>Submit Survey</button>
    </div>
  );
};

export default FillSurvey;
