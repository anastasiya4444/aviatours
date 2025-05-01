import React, { useState } from 'react';
import { useAuth } from '../security/AuthContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    
    const { login } = useAuth(); 
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setIsLoading(true); 
        setError(''); 

        try {
            const params = new URLSearchParams();
            params.append('username', username); 
            params.append('password', password);

            const response = await axios.post('http://localhost:8080/login', params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                withCredentials: true 
            });

            if (response.status === 200) {
                console.log("Успешная авторизация");
                navigate('/account'); 
            }
        } catch (error) {
            console.error('Login error:', error);
            setError('Ошибка авторизации. Проверьте свои данные.');
        } finally {
            setIsLoading(false); 
        }
    };

    return (
        <div>
            <div className="form-container">
            <h2>Авторизация</h2>
            
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        disabled={isLoading}
                    />
                    <label>
                        Логин
                    </label>
                </div>
                
                <div className="form-group">
                   
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        disabled={isLoading}
                    />
                     <label>
                        Пароль
                    </label>
                    
                </div>
                
                {error && (
                    <div>
                        {error}
                    </div>
                )}
                
                <button 
                    type="submit" 
                    disabled={isLoading}
                >
                    {isLoading ? 'Авторизация...' : 'Авторизоваться'}
                </button>
            </form>
            <div className="form-group">
                <p>Нет аккаунта?</p>
                <button 
                    onClick={() => navigate('/register')} 
                >
                    Перейти к регистрации
                </button>
            </div>
            </div>
            
        </div>
    );
}