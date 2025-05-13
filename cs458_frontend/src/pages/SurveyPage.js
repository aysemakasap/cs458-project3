// src/pages/SurveyPage.js
import React, { useState, useEffect } from 'react';
import '../styles/SurveyPage.css';
import { SurveyService } from '../services/SurveyService';

const SurveyPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    dateOfBirth: null,
    educationLevel: '',
    city: '',
    gender: '',
    aiModel: [],
    useCaseOfAi: ''
  });

  // This will store descriptions even when models are deselected
  const [modelDescriptions, setModelDescriptions] = useState({});
  
  const [isFormValid, setIsFormValid] = useState(false);
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [nameError, setNameError] = useState('');

  const AI_MODELS = ['chatGPT', 'bard', 'claude', 'copilot', 'deepseek'];
  const EDUCATION_LEVELS = ['Primary School', 'High School', 'Bachelor Degree', 'Master Degree', 'PhD', 'Other'];
  const GENDERS = ['male', 'female', 'other'];

  // Validation effect
  useEffect(() => {
    const isValid =
      formData.name.trim() !== '' &&
      formData.surname.trim() !== '' &&
      formData.dateOfBirth !== null &&
      formData.educationLevel !== '' &&
      formData.city.trim() !== '' &&
      formData.gender !== '' &&
      formData.aiModel.length > 0 &&
      formData.aiModel.every(model => model.description && model.description.trim() !== '') &&
      formData.useCaseOfAi.trim() !== '';
    
    setIsFormValid(isValid);
  }, [formData]);

  // Handle name input change with validation
  const handleNameChange = (e) => {
    const value = e.target.value;
    setFormData({ ...formData, name: value });
    
    if (value.trim() === '') {
      setNameError('Name is required');
    } else {
      setNameError('');
    }
  };

  // Toggle AI model selection
  const toggleAIModel = (modelType) => {
    const modelExists = formData.aiModel.some(m => m.aiType === modelType);
    
    if (modelExists) {
      // Remove model but keep description in our persistence store
      const currentDesc = formData.aiModel.find(m => m.aiType === modelType)?.description || '';
      setModelDescriptions({
        ...modelDescriptions,
        [modelType]: currentDesc
      });
      
      setFormData({
        ...formData,
        aiModel: formData.aiModel.filter(m => m.aiType !== modelType)
      });
    } else {
      // Add model with description from persistence store if available
      setFormData({
        ...formData,
        aiModel: [
          ...formData.aiModel,
          { 
            aiType: modelType, 
            description: modelDescriptions[modelType] || '' 
          }
        ]
      });
    }
  };

  // Update model description
  const updateModelDescription = (modelType, description) => {
    // Update in form data
    setFormData({
      ...formData,
      aiModel: formData.aiModel.map(model => 
        model.aiType === modelType 
          ? { ...model, description } 
          : model
      )
    });
    
    // Also update in our persistence store
    setModelDescriptions({
      ...modelDescriptions,
      [modelType]: description
    });
  };

  const handleSubmit = async () => {
    if (!isFormValid) return;
    
    try {
      const response = await SurveyService.submitSurveyResult(formData);
      
      if (response.status === 200) {
        window.alert('Success', 'Survey submitted successfully!');
        // Reset form after submission
        setFormData({
          name: '',
          surname: '',
          dateOfBirth: null,
          educationLevel: '',
          city: '',
          gender: '',
          aiModel: [],
          useCaseOfAi: ''
        });
        setModelDescriptions({});
      } else {
        window.alert('Error', 'Failed to submit survey');
      }
    } catch (error) {
      console.error('Error submitting survey:', error);
      window.alert('Error', 'Failed to submit survey');
    }
  };

  return (
    <div className="survey-container">
      <h1>AI Usage Survey</h1>
      
      <div className="form-group">
        <label htmlFor="name">Name</label>
        <input
          id="name"
          placeholder="Enter your name"
          value={formData.name}
          onChange={handleNameChange}
          onBlur={() => {
            if (formData.name.trim() === '') {
              setNameError('Name is required');
            }
          }}
          aria-label="Name"
        />
        {nameError && <div className="error-message">{nameError}</div>}
      </div>
      
      <div className="form-group">
        <label htmlFor="surname">Surname</label>
        <input
          id="surname"
          placeholder="Enter your surname"
          value={formData.surname}
          onChange={(e) => setFormData({ ...formData, surname: e.target.value })}
          aria-label="Surname"
        />
      </div>
      
      <div className="form-group">
        <label htmlFor="birth-date">Birth Date</label>
        <input
          id="birth-date"
          type="date"
          className="date-input"
          value={formData.dateOfBirth ? formData.dateOfBirth.toISOString().split('T')[0] : ''}
          onChange={(e) => {
            const date = e.target.value ? new Date(e.target.value) : null;
            setFormData({ ...formData, dateOfBirth: date });
          }}
        />
      </div>
      
      <div className="form-group">
        <label>Education Level</label>
        <div className="options-container">
          {EDUCATION_LEVELS.map(level => (
            <div
              key={level}
              className={`option ${formData.educationLevel === level ? 'option-selected' : ''}`}
              onClick={() => setFormData({ ...formData, educationLevel: level })}
            >
              {level}
            </div>
          ))}
        </div>
      </div>
      
      <div className="form-group">
        <label htmlFor="city">City</label>
        <input
          id="city"
          placeholder="Enter your city"
          value={formData.city}
          onChange={(e) => setFormData({ ...formData, city: e.target.value })}
        />
      </div>
      
      <div className="form-group">
        <label>Gender</label>
        <div className="options-container">
          {GENDERS.map(gender => (
            <div
              key={gender}
              className={`option ${formData.gender === gender ? 'option-selected' : ''}`}
              onClick={() => setFormData({ ...formData, gender })}
            >
              {gender}
            </div>
          ))}
        </div>
      </div>
      
      <div className="form-group">
        <label>AI Models You've Tried</label>
        <div className="models-container">
          {AI_MODELS.map(model => (
            <div key={model} className="model-item">
              <label>
                <input
                  type="checkbox"
                  checked={formData.aiModel.some(m => m.aiType === model)}
                  onChange={() => toggleAIModel(model)}
                  aria-label={model}
                />
                {model}
              </label>
              
              {formData.aiModel.some(m => m.aiType === model) && (
                <textarea
                  placeholder={`Describe ${model} defects`}
                  value={formData.aiModel.find(m => m.aiType === model)?.description || ''}
                  onChange={(e) => updateModelDescription(model, e.target.value)}
                />
              )}
            </div>
          ))}
        </div>
      </div>
      
      <div className="form-group">
        <label htmlFor="ai-use-case">Beneficial AI Use Cases</label>
        <textarea
          id="ai-use-case"
          placeholder="Describe how AI has been beneficial"
          value={formData.useCaseOfAi}
          onChange={(e) => {
            const text = e.target.value;
            // Limit to 300 characters
            if (text.length <= 300) {
              setFormData({ ...formData, useCaseOfAi: text });
            }
          }}
          maxLength={300}
          aria-label="Beneficial AI Use Cases"
        />
        <div className="character-count">
          {formData.useCaseOfAi.length}/300
        </div>
      </div>
      
      {isFormValid && (
        <button 
          className="submit-button"
          onClick={handleSubmit}
        >
          Send Survey
        </button>
      )}
    </div>
  );
};

export default SurveyPage;