import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const questionTypes = [
  'OpenEnded',
  'MultipleChoice',
  'Dropdown',
  'Checkbox',
  'RatingScale'
];

const CreateCustomSurvey = () => {
  const [questions, setQuestions] = useState([]);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const addQuestion = () => {
    setQuestions([
      ...questions,
      {
        type: 'OpenEnded',
        question: '',
        openAnswer: '',
        rating: null,
        options: ['']
      }
    ]);
  };

  const updateQuestionField = (index, field, value) => {
    const updated = [...questions];
    updated[index][field] = value;
    setQuestions(updated);
  };

  const updateOption = (qIndex, oIndex, value) => {
    const updated = [...questions];
    updated[qIndex].options[oIndex] = value;
    setQuestions(updated);
  };

  const addOption = (index) => {
    const updated = [...questions];
    updated[index].options.push('');
    setQuestions(updated);
  };

  const removeQuestion = (index) => {
    const updated = [...questions];
    updated.splice(index, 1);
    setQuestions(updated);
  };

  const handleSubmit = async () => {
    const survey = { questions };
    const response = await fetch('http://localhost:8080/api/surveys', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(survey)
    });

    if (response.ok) {
      setMessage('Survey created!');
      navigate('/success');
    } else {
      setMessage('Creation failed');
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Create Custom Survey</h2>
      <button onClick={addQuestion}>Add Question</button>
      {questions.map((q, index) => (
        <div key={index} style={{ marginTop: '20px', border: '1px solid #ccc', padding: '10px' }}>
          <label>Type:
            <select
              value={q.type}
              onChange={(e) => updateQuestionField(index, 'type', e.target.value)}>
              {questionTypes.map((type) => <option key={type}>{type}</option>)}
            </select>
          </label>

          <label>Question:
            <input
              type="text"
              value={q.question}
              onChange={(e) => updateQuestionField(index, 'question', e.target.value)}
              placeholder="Enter question text"
              style={{ width: '100%' }}
            />
          </label>

          {['MultipleChoice', 'Dropdown', 'Checkbox'].includes(q.type) && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
              <label>Options:</label>
              {q.options.map((opt, oIndex) => (
                <input
                  key={oIndex}
                  type="text"
                  value={opt}
                  onChange={(e) => updateOption(index, oIndex, e.target.value)}
                  style={{ marginBottom: '5px', width: '100%' }}
                  placeholder='Enter Option'
                />
              ))}
              <button onClick={() => addOption(index)}>Add Option</button>
            </div>
          )}

          {q.type === 'RatingScale' && (
            <div>
              <label>Default Rating (1–5): </label>
              <input
                type="number"
                min="1"
                max="5"
                value={q.rating || ''}
                onChange={(e) => updateQuestionField(index, 'rating', Number(e.target.value))}
              />
            </div>
          )}

          <button onClick={() => removeQuestion(index)} style={{ color: 'red' }}>Remove</button>
        </div>
      ))}

      <button onClick={handleSubmit} style={{ marginTop: '20px' }}>Submit Survey</button>
      {message && <p>{message}</p>}
    </div>
  );
};

export default CreateCustomSurvey;
