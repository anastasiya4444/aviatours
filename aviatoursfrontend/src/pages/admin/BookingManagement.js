import React, { useState, useEffect } from 'react';
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

const BookingManagement = () => {
    const [bookings, setBookings] = useState([]);
    const [routes, setRoutes] = useState([]);
    const [users, setUsers] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [formData, setFormData] = useState({
        route: { id: '' },
        user: { id: '' },
        paymentMethod: 'CREDIT_CARD',
        paymentStatus: 'PENDING',
        amount: '',
        createdAt: ''
    });

    useEffect(() => {
        fetchBookings();
        fetchRoutes();
        fetchUsers();
    }, []);

    const fetchBookings = async () => {
        try {
            const response = await api.get('/booking/getAll');
            setBookings(response.data);
        } catch (error) {
            console.error('Error fetching bookings:', error);
            alert('Failed to load bookings');
        }
    };

    const fetchRoutes = async () => {
        try {
            const response = await api.get('/route/getAll');
            setRoutes(response.data);
        } catch (error) {
            console.error('Error fetching routes:', error);
            alert('Failed to load routes');
        }
    };

    const fetchUsers = async () => {
        try {
            const response = await api.get('/user/getAll');
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
            alert('Failed to load users');
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleRouteChange = (e) => {
        const routeId = parseInt(e.target.value);
        setFormData(prev => ({
            ...prev,
            route: { id: routeId }
        }));
    };

    const handleUserChange = (e) => {
        const userId = parseInt(e.target.value);
        setFormData(prev => ({
            ...prev,
            user: { id: userId }
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const bookingPayload = {
                route: formData.route,
                user: formData.user,
                createdAt: formData.createdAt || new Date().toISOString(),
                payment: {
                    paymentMethod: formData.paymentMethod,
                    paymentStatus: formData.paymentStatus,
                    amount: parseFloat(formData.amount),
                    createdAt: new Date().toISOString()
                }
            };

            if (editingId) {
                await api.put('/booking/update', {
                    ...bookingPayload,
                    id: editingId
                });
            } else {
                await api.post('/booking/add', bookingPayload);
            }

            fetchBookings();
            resetForm();
        } catch (error) {
            console.error('Error:', error.response?.data || error.message);
            alert('Operation failed: ' + (error.response?.data?.message || error.message));
        }
    };

    const handleEdit = (booking) => {
        setFormData({
            route: booking.route || { id: '' },
            user: booking.user || { id: '' },
            paymentMethod: booking.payment?.paymentMethod || 'CREDIT_CARD',
            paymentStatus: booking.payment?.paymentStatus || 'PENDING',
            amount: booking.payment?.amount || '',
            createdAt: booking.createdAt || ''
        });
        setEditingId(booking.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Вы уверены что хотите отменить бронирование?')) return;

        try {
            await api.delete(`/booking/${id}`);
            fetchBookings();
        } catch (error) {
            console.error('Ошибка удаления:', error.response?.data || error.message);
            alert('Failed to delete booking');
        }
    };

    const resetForm = () => {
        setFormData({
            route: { id: '' },
            user: { id: '' },
            paymentMethod: 'CREDIT_CARD',
            paymentStatus: 'PENDING',
            amount: '',
            createdAt: ''
        });
        setEditingId(null);
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString();
    };

    return (
        <div className="page-container">
            <h1>Управление бронированиями</h1>

            <div className="form-section">
                <h2>{editingId ? 'Редактировать' : 'Добавить'}</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Маршрут:</label>
                        <select
                            name="routeId"
                            value={formData.route?.id || ''}
                            onChange={handleRouteChange}
                            required
                        >
                            <option value="">Выбрать маршрут</option>
                            {routes.map(route => (
                                <option key={route.id} value={route.id}>
                                    {route.name || `Route ${route.id}`}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label>Пользователь:</label>
                        <select
                            name="userId"
                            value={formData.user?.id || ''}
                            onChange={handleUserChange}
                            required
                        >
                            <option value="">Выбрать пользователя</option>
                            {users.map(user => (
                                <option key={user.id} value={user.id}>
                                    {user.username || `User ${user.id}`}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label>Способ оплаты:</label>
                        <select
                            name="paymentMethod"
                            value={formData.paymentMethod}
                            onChange={handleInputChange}
                            required
                        >
                            <option value="CREDIT_CARD">Кредитная карта</option>
                            <option value="ONLINE">Онлайн</option>
                            <option value="BANK_TRANSFER">Банковский перевод</option>
                        </select>
                    </div>

                    <div>
                        <label>Статус оплаты:</label>
                        <select
                            name="paymentStatus"
                            value={formData.paymentStatus}
                            onChange={handleInputChange}
                            required
                        >
                            <option value="PENDING">Ожидает</option>
                            <option value="COMPLETED">Оплачено</option>
                            <option value="FAILED">Прервано</option>
                            <option value="REFUNDED">Отменено</option>
                        </select>
                    </div>

                    <div>
                        <label>Сумма:</label>
                        <input
                            type="number"
                            name="amount"
                            value={formData.amount}
                            onChange={handleInputChange}
                            required
                            step="0.01"
                            min="0"
                        />
                    </div>

                    <div>
                        <label>Дата создания:</label>
                        <input
                            type="datetime-local"
                            name="createdAt"
                            value={formData.createdAt}
                            onChange={handleInputChange}
                        />
                    </div>

                    <div className="form-actions">
                        <button type="submit" className="btn-primary">
                            {editingId ? 'Update Booking' : 'Add Booking'}
                        </button>
                        {editingId && (
                            <button
                                type="button"
                                onClick={resetForm}
                                className="btn-secondary"
                            >
                                Отменить
                            </button>
                        )}
                    </div>
                </form>
            </div>

            <div className="table-section">
                <h2>Все бронирования</h2>
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Маршрут</th>
                            <th>Пользователь</th>
                            <th>Метод оплаты</th>
                            <th>Статус</th>
                            <th>Сумма</th>
                            <th>Дата создания</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        {bookings.map(booking => (
                            <tr key={booking.id}>
                                <td>{booking.id}</td>
                                <td>{booking.route?.name || `Маршрут ${booking.route?.id}`}</td>
                                <td>{booking.user?.username || `Пользователь ${booking.user?.id}`}</td>
                                <td>{booking.payment?.paymentMethod || 'N/A'}</td>
                                <td>{booking.payment?.paymentStatus || 'N/A'}</td>
                                <td>{booking.payment?.amount || '0'}</td>
                                <td>{formatDate(booking.createdAt)}</td>
                                <td className="actions">
                                    <button
                                        onClick={() => handleEdit(booking)}
                                        className="btn-edit"
                                    >
                                        Обновить
                                    </button>
                                    <button
                                        onClick={() => handleDelete(booking.id)}
                                        className="btn-delete"
                                    >
                                        Удалить
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default BookingManagement;