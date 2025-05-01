import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from './components/Login';
import Registration from './components/Registration';
import { AuthProvider } from './security/AuthContext';
import ProtectedRoute from './security/ProtectedRoute';
import Header from './components/Header';
import Footer from './components/Footer';

const App = () => {
    return (
        <Router>
            <AuthProvider>
                <div className="app-container">
                    <Header />
                    <main className="main-content">
                        <Routes>
                            {/* Публичные маршруты */}
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Registration />} />
                            
                            {/* Защищенные маршруты */}
                            <Route element={<ProtectedRoute />}>
                           
                            </Route>
                            
                            {/* Остальные публичные маршруты */}
                            <Route path="/" element={<Login />} />
                                                     
                        </Routes>
                    </main>
                    <Footer />
                </div>
            </AuthProvider>
        </Router>
    );
};

export default App;