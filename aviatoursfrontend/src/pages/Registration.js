import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../security/AuthContext';

export default function Registration() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const formData = {
            username,
            password
        };

        try {
            const response = await axios.post('http://localhost:8080/register', formData, {
                withCredentials: true 
            });
            if (response.status === 200 || response.status === 201) {
                login(); 
                setSuccessMessage('Registration successful! Redirecting to your account...');
                setTimeout(() => {
                    navigate('/login'); 
                }, 2000);
            } else {
                const errorText = response.data.message || 'Login failed. Please try again!';
                setError(errorText);
            }
            if (response.status === 201) {
                const loginData = { username, password }; 
                const loginResponse = await axios.post('http://localhost:8080/login', loginData, {
                    withCredentials: true 
                });

                if (loginResponse.status === 200) {
                    login(); 
                    setSuccessMessage('Registration successful! Redirecting to your account...');
                    setTimeout(() => {
                        navigate('/account'); 
                    }, 2000);
                } else {
                    const errorText = loginResponse.data.message || 'Login failed. Please try again!';
                    setError(errorText);
                }
            } else {
                const errorText = response.data.message || 'Registration failed. Please try again!';
                setError(errorText);
            }
        } catch (err) {
            setError('An error occurred during user registration');
        }
    };

    return (
        <div>
            <div className="form-container">
            <h2>Регистрация</h2>
            <form onSubmit={handleSubmit}>
                <div  className="form-group">
                    
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                   <label>
                   Логин: </label>
                </div>
                <div className="form-group">
                    
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    <label>
                    Пароль:</label>
                </div>
                {error && (
                    <div>
                        {error}
                    </div>
                )}
                {successMessage && (
                    <div>
                        {successMessage}
                    </div>
                )}
                <button type="submit">
                    Регистрация
                </button>
            </form>
            </div>
        </div>
    );
}