import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

axios.defaults.withCredentials = true; 
const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        checkAuthStatus();
    }, []);
    const checkAuthStatus = async () => {
        try {
            const response = await axios.get('http://localhost:8080/current');
            setIsAuthenticated(true);
            setUser(response.data);
        } catch (error) {
            setIsAuthenticated(false);
            setUser(null);
            
            if (error.response?.status === 401) {
                console.log("Session expired, attempting to refresh...");
                await attemptRefreshToken();
            }
           
        } finally {
            setLoading(false);
        }
    };
    
    const attemptRefreshToken = async () => {
        try {
            const response = await axios.post('http://localhost:8080/refresh-token', {}, 
                { withCredentials: true });
            if (response.status === 200) {
                await checkAuthStatus();
            }
        } catch (refreshError) {
            console.log("Refresh failed, requiring new login");
        }
    };
    

    const login = async (username, password) => {
        try {
            const response = await axios.post('http://localhost:8080/login', 
                `username=${username}&password=${password}`, {
                  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                  withCredentials: true
                });
            if (response.status === 200) {
                await checkAuthStatus(); 
                return true;
            }
            return false;
        } catch (error) {
            console.error('Login error:', error);
            return false;
        }
    };

    const logout = async () => {
        try {
            await axios.post('http://localhost:8080/logout', {}, 
                { withCredentials: true });
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            setIsAuthenticated(false);
            setUser(null);
        }
    };

    const value = {
        isAuthenticated,
        user,
        loading,
        login,
        logout,
        checkAuthStatus
    };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
};