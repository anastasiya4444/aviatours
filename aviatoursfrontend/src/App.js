import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from './pages/Login';
import Registration from './pages/Registration';
import { AuthProvider } from './security/AuthContext';
import ProtectedRoute from './security/ProtectedRoute';
import Header from './components/Header';
import Footer from './components/Footer';

// Страницы фильтрации
import FlightFilter from './pages/FlightFilter';
import TourFilter from './pages/TourFilter';

// Страницы работы с контентом
import MyReviews from './pages/MyReviews';
import RouteBuilder from './pages/RouteBuilder';
import ActivityBooking from './pages/ActivityBooking';

// Личный кабинет и бронирования
import AccountPage from './pages/AccountPage';
import MyBookings from './pages/MyBookings';

// Процесс бронирования тура
import TourDetail from './pages/booking-process/TourDetail';
import FlightBooking from './pages/booking-process/FlightBooking';
import GuestInfo from './pages/booking-process/GuestInfo';
import RoomSelection from './pages/booking-process/RoomSelection';
import ActivitySelection from './pages/booking-process/ActivitySelection';

// Страницы сравнения
import TourComparison from './pages/TourComparison';

// Административные страницы
import HotelManagement from './pages/admin/HotelManagement';
import TourManagement from './pages/admin/TourManagement';
import FlightManagement from './pages/admin/FlightManagement';
import UserManagement from './pages/admin/UserManagement';


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
                                {/* Основные функциональные страницы */}
                                <Route path="/flights" element={<FlightFilter />} />
                                <Route path="/tours" element={<TourFilter />} />
                                <Route path="/reviews" element={<MyReviews />} />
                                <Route path="/route-builder" element={<RouteBuilder />} />
                                <Route path="/activity-booking" element={<ActivityBooking />} />
                                
                                {/* Личный кабинет */}
                                <Route path="/account" element={<AccountPage />} />
                                <Route path="/my-bookings" element={<MyBookings />} />
                                
                                {/* Процесс бронирования */}
                                <Route path="/tour/:id" element={<TourDetail />} />
                                <Route path="/book-flight/:id" element={<FlightBooking />} />
                                <Route path="/guest-info" element={<GuestInfo />} />
                                <Route path="/select-room" element={<RoomSelection />} />
                                <Route path="/select-activities" element={<ActivitySelection />} />
                                
                                {/* Сравнение */}
                                <Route path="/compare-tours" element={<TourComparison />} />
                                
                                {/* Администрирование (только для админов) */}
                                <Route path="/admin/hotels" element={<HotelManagement />} />
                                <Route path="/admin/tours" element={<TourManagement />} />
                                <Route path="/admin/flights" element={<FlightManagement />} />
                                <Route path="/admin/users" element={<UserManagement />} />
                            </Route>
                            
                            {/* Стартовая страница */}
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