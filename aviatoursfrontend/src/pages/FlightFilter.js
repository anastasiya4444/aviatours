import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../security/AuthContext';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.request.use(request => {
    console.log('Starting Request', request);
    return request;
});

api.interceptors.response.use(response => {
    console.log('Response:', response);
    return response;
}, error => {
    console.error('Error Response:', error);
    return Promise.reject(error);
});

const FlightFilter = () => {
    const [tickets, setTickets] = useState([]);
    const [filteredTickets, setFilteredTickets] = useState([]);
    const [filters, setFilters] = useState({
        flightNumber: '',
        departureAirportCode: '',
        arrivalAirportCode: '',
        departureTimeFrom: '',
        departureTimeTo: '',
        minCost: '',
        maxCost: '',
        seatNumber: '',
        status: ''
    });
    const { user } = useAuth();
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const [guestData, setGuestData] = useState({
        lastName: '',
        firstName: '',
        middleName: '',
        citizenship: '',
        birthDate: '',
        gender: '',
        passportType: '',
        passportNumber: '',
        passportIssueDate: '',
        passportExpiryDate: '',
        issuingAuthority: ''
    });
    const [selectedFlight, setSelectedFlight] = useState(null);
    const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
    const [bookingStatus, setBookingStatus] = useState(null);

    useEffect(() => {
        fetchAvailableTickets();
    }, []);

    const fetchAvailableTickets = async () => {
        setIsLoading(true);
        try {
            const response = await api.get('/air_ticket/getAll', {
                params: { routeId: null }
            });
            setTickets(response.data);
            setFilteredTickets(response.data);
        } catch (error) {
            console.error('Error fetching tickets:', error);
            alert('Ошибка при загрузке билетов');
        } finally {
            setIsLoading(false);
        }
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters(prev => ({ ...prev, [name]: value }));
    };

    const applyFilters = async () => {
        setIsLoading(true);
        try {
            const params = {
                flightNumber: filters.flightNumber || undefined,
                departureAirportCode: filters.departureAirportCode || undefined,
                arrivalAirportCode: filters.arrivalAirportCode || undefined,
                departureTimeFrom: filters.departureTimeFrom || undefined,
                departureTimeTo: filters.departureTimeTo || undefined,
                minCost: filters.minCost || undefined,
                maxCost: filters.maxCost || undefined,
                seatNumber: filters.seatNumber || undefined,
                status: filters.status || undefined
            };

            const response = await api.get('/air_ticket/filter', { params });
            setFilteredTickets(response.data);
        } catch (error) {
            console.error('Error filtering tickets:', error);
            alert('Ошибка при фильтрации');
        } finally {
            setIsLoading(false);
        }
    };

    const resetFilters = () => {
        setFilters({
            flightNumber: '',
            departureAirportCode: '',
            arrivalAirportCode: '',
            departureTimeFrom: '',
            departureTimeTo: '',
            minCost: '',
            maxCost: '',
            seatNumber: '',
            status: ''
        });
        fetchAvailableTickets();
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setGuestData(prev => ({ ...prev, [name]: value }));
    };

    const handlePaymentMethodChange = (e) => {
        setPaymentMethod(e.target.value);
    };

    const validateGuestData = () => {
        const requiredFields = ['lastName', 'firstName', 'passportNumber', 'passportType', 'birthDate'];
        for (const field of requiredFields) {
            if (!guestData[field]) {
                alert(`Пожалуйста, заполните обязательное поле: ${field}`);
                return false;
            }
        }
        return true;
    };

    const handleBooking = async (ticketId) => {
        console.log('Starting booking process for ticket:', ticketId);
        
        if (!validateGuestData()) {
            return;
        }

        const ticket = filteredTickets.find(t => t.id === ticketId);
        if (!ticket) {
            console.error('Ticket not found');
            return;
        }

        setSelectedFlight(ticket);
        setBookingStatus('processing');

        try {
            const newRoute = {
                departureAirportCode: ticket.departureAirportCode,
                arrivalAirportCode: ticket.arrivalAirportCode,
                departureDate: new Date(ticket.departureTime).toISOString(),
                arrivalDate: new Date(ticket.arrivalTime).toISOString()
            };

            console.log('Creating route:', newRoute);
            const routeResponse = await api.post('/route/add_get', newRoute);
            const createdRouteId = routeResponse.data.id;

            if (!createdRouteId) {
                throw new Error('Не удалось создать маршрут: ID не получен');
            }

            const updatedTicket = {
                ...ticket,
                status: "BOOKED",
                passenger: {
                    lastName: guestData.lastName,
                    firstName: guestData.firstName,
                    middleName: guestData.middleName,
                    citizenship: guestData.citizenship,
                    birthDate: guestData.birthDate,
                    gender: guestData.gender,
                    passportType: guestData.passportType,
                    passportNumber: guestData.passportNumber,
                    passportIssueDate: guestData.passportIssueDate,
                    passportExpiryDate: guestData.passportExpiryDate,
                    issuingAuthority: guestData.issuingAuthority
                },
                route: {
                    id: createdRouteId
                }
            };

            console.log('Updating ticket:', updatedTicket);
            await api.put(`/air_ticket/${ticket.id}/update`, updatedTicket);

            const bookingData = {
                routeId: createdRouteId,
                airTicketIds: [ticket.id],
                paymentMethod: paymentMethod,
                user: {
                    id: user.id,
                    username: user.username
                }
            };

            console.log('Creating booking:', bookingData);
             const response = await axios.post('http://localhost:8080/booking/book', bookingData, {
                            responseType: 'blob', 
                            headers: { Authorization: `Bearer ${user.token}` }
                        });
                
                        const url = window.URL.createObjectURL(new Blob([response.data]));
                        const link = document.createElement('a');
                        link.href = url;
                        link.setAttribute('download', `booking_report_${bookingData.routeId}.pdf`); 
                        document.body.appendChild(link);
                        link.click();
                        link.remove();

            alert('Бронирование успешно завершено!');
            fetchAvailableTickets(); 

        } catch (error) {
            console.error('Booking failed:', error);
            setBookingStatus('error');
            alert(`Ошибка бронирования: ${error.response?.data?.message || error.message}`);
        }
    };

    return (
        <div className="page-container">
            <h1>Поиск и бронирование авиабилетов</h1>

            <div className="filter-section">
                <h2>Фильтры</h2>
                
                <div className="filter-grid">
                    <div className="filter-group">
                        <label>Номер рейса:</label>
                        <input type="text" name="flightNumber" value={filters.flightNumber} onChange={handleFilterChange} />
                    </div>
                    
                    <div className="filter-group">
                        <label>Аэропорт вылета:</label>
                        <input type="text" name="departureAirportCode" value={filters.departureAirportCode} onChange={handleFilterChange} />
                    </div>
                    
                    <div className="filter-group">
                        <label>Аэропорт прилета:</label>
                        <input type="text" name="arrivalAirportCode" value={filters.arrivalAirportCode} onChange={handleFilterChange} />
                    </div>
                    
                    <div className="filter-group">
                        <label>Дата вылета от:</label>
                        <input type="datetime-local" name="departureTimeFrom" value={filters.departureTimeFrom} onChange={handleFilterChange} />
                    </div>
                    
                    <div className="filter-group">
                        <label>Дата вылета до:</label>
                        <input type="datetime-local" name="departureTimeTo" value={filters.departureTimeTo} onChange={handleFilterChange} />
                    </div>
                    
                    <div className="filter-group">
                        <label>Цена от:</label>
                        <input type="number" name="minCost" value={filters.minCost} onChange={handleFilterChange} min="0" />
                    </div>
                    
                    <div className="filter-group">
                        <label>Цена до:</label>
                        <input type="number" name="maxCost" value={filters.maxCost} onChange={handleFilterChange} min="0" />
                    </div>
                    
                    <div className="filter-group">
                        <label>Номер места:</label>
                        <input type="number" name="seatNumber" value={filters.seatNumber} onChange={handleFilterChange} min="1" />
                    </div>
                    
                    <div className="filter-group">
                        <label>Статус:</label>
                        <select name="status" value={filters.status} onChange={handleFilterChange}>
                            <option value="">Все</option>
                            <option value="AVAILABLE">Доступен</option>
                            <option value="BOOKED">Забронирован</option>
                            <option value="SOLD">Продан</option>
                        </select>
                    </div>
                </div>
                
                <div className="filter-actions">
                    <button onClick={applyFilters} disabled={isLoading}>
                        {isLoading ? 'Загрузка...' : 'Применить фильтры'}
                    </button>
                    <button onClick={resetFilters} disabled={isLoading}>
                        Сбросить фильтры
                    </button>
                </div>
            </div>

            <div className="results-section">
                <h2>Найденные билеты ({filteredTickets.length})</h2>
                
                {isLoading ? (
                    <p>Загрузка билетов...</p>
                ) : filteredTickets.length === 0 ? (
                    <p>Билеты не найдены</p>
                ) : (
                    <div className="tickets-list">
                        {filteredTickets.map(ticket => (
                            <div key={ticket.id} className="ticket-card">
                                <div className="ticket-info">
                                    <h3>Рейс {ticket.flightNumber}</h3>
                                    <p>{ticket.departureAirportCode} → {ticket.arrivalAirportCode}</p>
                                    <p>Вылет: {new Date(ticket.departureTime).toLocaleString()}</p>
                                    <p>Прилет: {new Date(ticket.arrivalTime).toLocaleString()}</p>
                                    <p>Место: {ticket.seatNumber}</p>
                                    <p className="price">{ticket.cost} руб.</p>
                                </div>
                                <button 
                                    onClick={() => handleBooking(ticket.id)}
                                    disabled={ticket.status !== 'AVAILABLE' || bookingStatus === 'processing'}
                                >
                                    {ticket.status !== 'AVAILABLE' ? 'Недоступен' : 'Забронировать'}
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <div className="guest-info-section">
                <h2>Данные пассажира</h2>
                
                <form className="guest-form">
                    <div className="form-row">
                        <div className="form-group">
                            <label>Фамилия*</label>
                            <input type="text" name="lastName" value={guestData.lastName} onChange={handleInputChange} required />
                        </div>
                        
                        <div className="form-group">
                            <label>Имя*</label>
                            <input type="text" name="firstName" value={guestData.firstName} onChange={handleInputChange} required />
                        </div>
                        
                        <div className="form-group">
                            <label>Отчество</label>
                            <input type="text" name="middleName" value={guestData.middleName} onChange={handleInputChange} />
                        </div>
                    </div>
                    
                    <div className="form-row">
                        <div className="form-group">
                            <label>Дата рождения*</label>
                            <input type="date" name="birthDate" value={guestData.birthDate} onChange={handleInputChange} required />
                        </div>
                        
                        <div className="form-group">
                            <label>Гражданство</label>
                            <input type="text" name="citizenship" value={guestData.citizenship} onChange={handleInputChange} />
                        </div>
                        
                        <div className="form-group">
                            <label>Пол</label>
                            <select name="gender" value={guestData.gender} onChange={handleInputChange}>
                                <option value="">Не указан</option>
                                <option value="MALE">Мужской</option>
                                <option value="FEMALE">Женский</option>
                            </select>
                        </div>
                    </div>
                    
                    <div className="form-row">
                        <div className="form-group">
                            <label>Тип документа*</label>
                            <input type="text" name="passportType" value={guestData.passportType} onChange={handleInputChange} required />
                        </div>
                        
                        <div className="form-group">
                            <label>Номер документа*</label>
                            <input type="text" name="passportNumber" value={guestData.passportNumber} onChange={handleInputChange} required />
                        </div>
                    </div>
                    
                    <div className="form-row">
                        <div className="form-group">
                            <label>Дата выдачи</label>
                            <input type="date" name="passportIssueDate" value={guestData.passportIssueDate} onChange={handleInputChange} />
                        </div>
                        
                        <div className="form-group">
                            <label>Срок действия</label>
                            <input type="date" name="passportExpiryDate" value={guestData.passportExpiryDate} onChange={handleInputChange} />
                        </div>
                        
                        <div className="form-group">
                            <label>Кем выдан</label>
                            <input type="text" name="issuingAuthority" value={guestData.issuingAuthority} onChange={handleInputChange} />
                        </div>
                    </div>
                </form>
                
                <div className="payment-section">
                    <h3>Способ оплаты</h3>
                    <select value={paymentMethod} onChange={handlePaymentMethodChange}>
                        <option value="CREDIT_CARD">Кредитная карта</option>
                        <option value="BANK_TRANSFER">Банковский перевод</option>
                        <option value="ELECTRONIC_PAYMENT">Электронный платеж</option>
                    </select>
                </div>
            </div>

            {bookingStatus === 'processing' && (
                <div className="booking-status processing">
                    <p>Обработка бронирования...</p>
                </div>
            )}
            
            {bookingStatus === 'success' && (
                <div className="booking-status success">
                    <p>Бронирование успешно завершено!</p>
                </div>
            )}
            
            {bookingStatus === 'error' && (
                <div className="booking-status error">
                    <p>Произошла ошибка при бронировании</p>
                </div>
            )}
        </div>
    );
};

export default FlightFilter;