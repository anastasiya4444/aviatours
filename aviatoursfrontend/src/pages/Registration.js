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

    const validateUsername = (username) => {
        const usernameRegex = /^[A-Za-z0-9]{3,20}$/; 
        return usernameRegex.test(username);
    };

    const validatePassword = (password) => {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,}$/;
        return passwordRegex.test(password);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccessMessage('');

        if (!validateUsername(username)) {
            setError('Логин должен содержать от 3 до 20 символов и состоять только из цифр и букв латинского алфавита.');
            return;
        }

        if (!validatePassword(password)) {
            setError('Пароль должен содержать не менее 8 символов, включая хотя бы одну заглавную и одну строчную букву, а также цифру.');
            return;
        }

        const formData = { username, password };

        try {
            const response = await axios.post('http://localhost:8080/register', formData, {
                withCredentials: true 
            });

            if (response.status === 200 || response.status === 201) {
                const loginData = { username, password }; 
                const loginResponse = await axios.post('http://localhost:8080/login', loginData, {
                    withCredentials: true 
                });

                if (loginResponse.status === 200) {
                    login(); 
                    setSuccessMessage('Регистрация успешна! Перенаправление на вашу учетную запись...');
                    setTimeout(() => {
                        navigate('/account'); 
                    }, 2000);
                } else {
                    const errorText = loginResponse.data.message || 'Ошибка входа. Пожалуйста, попробуйте еще раз!';
                    setError(errorText);
                }
            } else {
                const errorText = response.data.message || 'Ошибка регистрации. Пожалуйста, попробуйте еще раз!';
                setError(errorText);
            }
        } catch (err) {
            setError('Произошла ошибка во время регистрации пользователя');
        }
    };

    return (
        <div>
            <div className="form-container">
                <h2>Регистрация</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Логин:</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Пароль:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {error && <div className="error-message">{error}</div>}
                    {successMessage && <div className="success-message">{successMessage}</div>}
                    <button type="submit">Регистрация</button>
                </form>
            </div>
        </div>
    );
}