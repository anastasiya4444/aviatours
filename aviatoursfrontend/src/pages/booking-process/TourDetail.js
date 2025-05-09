import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../../security/AuthContext';

const TourDetail = () => {
    const { tourId } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    
    const [tour, setTour] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const [currentStep, setCurrentStep] = useState(1);
    const [selectedFlight, setSelectedFlight] = useState(null);
    const [selectedRoom, setSelectedRoom] = useState(null);
    const [selectedActivities, setSelectedActivities] = useState([]);
    const [hotelsWithRooms, setHotelsWithRooms] = useState([]);
    const [availableActivities, setAvailableActivities] = useState([]);
    const [routeTickets, setRouteTickets] = useState({});
    
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
    
    const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(() => {
        const fetchTourData = async () => {
            try {
                const [tourResponse] = await Promise.all([
                    axios.get(`http://localhost:8080/tour/${tourId}`),
                ]);
                
                setTour(tourResponse.data);
                
                if (tourResponse.data.routes) {
                    tourResponse.data.routes.forEach(route => {
                        fetchTicketsForRoute(route.id);
                    });
                }
            } catch (error) {
                console.error('Ошибка загрузки данных:', error);
                setError('Не удалось загрузить данные тура');
            } finally {
                setLoading(false);
            }
        };
        
        fetchTourData();
    }, [tourId]);

    useEffect(() => {
        if (currentStep === 2 && tour?.hotels && hotelsWithRooms.length === 0) {
            fetchRoomsForHotels();
        }
    }, [currentStep, tour]);

    const fetchTicketsForRoute = async (routeId) => {
        try {
            const response = await axios.get(
                `http://localhost:8080/air_ticket/available_route?routeId=${routeId}`
            );
            setRouteTickets(prev => ({
                ...prev,
                [routeId]: response.data
            }));
        } catch (error) {
            console.error(`Ошибка загрузки билетов для маршрута ${routeId}:`, error);
            setRouteTickets(prev => ({
                ...prev,
                [routeId]: []
            }));
        }
    };

    const fetchRoomsForHotels = async () => {
        try {
            const hotels = tour.hotels;
            const hotelsData = await Promise.all(
                hotels.map(async hotel => {
                    const response = await axios.get(
                        `http://localhost:8080/room/getByHotel/${hotel.id}`
                    );
                    return {
                        ...hotel,
                        rooms: response.data
                    };
                })
            );
            setHotelsWithRooms(hotelsData);
        } catch (error) {
            console.error('Ошибка загрузки номеров:', error);
            setError('Не удалось загрузить номера');
        }
    };

    const handleFlightSelect = (ticket) => {
        setSelectedFlight(ticket);
        setCurrentStep(2); 
    };

    const handleRoomSelect = (room) => {
        setSelectedRoom(room);
        setCurrentStep(3); 
    };

    const handleActivityToggle = (activity) => {
        setSelectedActivities(prev => 
            prev.some(a => a.id === activity.id) 
                ? prev.filter(a => a.id !== activity.id)
                : [...prev, activity]
        );
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setGuestData(prev => ({
            ...prev,
            [name]: value
        }));
        console.log(name)
    };

    useEffect(() => {
        const calculateTotalPrice = async () => {
            if (selectedFlight && selectedRoom) {
                try {
                    const response = await axios.post('http://localhost:8080/booking/calculate-price', {
                        airTicketIds: [selectedFlight.id],
                        roomId: selectedRoom.id,
                        activityIds: selectedActivities.map(a => a.id)
                    }, {
                        headers: { Authorization: `Bearer ${user.token}` }
                    });
                    setTotalPrice(response.data);
                    console.log('Итоговая цена:', response.data);
                } catch (err) {
                    console.error('Ошибка расчета цены:', err);
                }
            }
        };

        calculateTotalPrice();
    }, [selectedFlight, selectedRoom, selectedActivities, user.token]);

    const confirmBooking = async () => {
        try {
            const bookingData = {
                routeId: selectedFlight.route.id,
                roomId: selectedRoom?.id,
                airTicketIds: selectedFlight ? [selectedFlight.id] : [],
                activityIds: selectedActivities.map(a => a.id),
                paymentMethod: paymentMethod,
                user: { 
                    id: user.id,
                    username: user.username
                },
            };
    
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
    
        } catch (error) {
            console.error('Ошибка создания бронирования или загрузки отчета:', error);
        }
    };

    const downloadBookingReport = async (bookingId) => {
        try {
            const response = await axios.get(
                `http://localhost:8080/booking/report/${bookingId}/pdf`,
                {
                    responseType: 'blob',
                    withCredentials: true
                }
            );
            
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `booking_report_${bookingId}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (error) {
            console.error('Ошибка загрузки отчета:', error);
        }
    };

    const updateTicketAndRoomStatus = async () => {
        try {
            await axios.put(`http://localhost:8080/air_ticket/update`, {
                ...selectedFlight,
                status: 'BOOKED',
                passenger: guestData
            });
            
            if (selectedRoom) {
                await axios.put(`http://localhost:8080/room/update`, {
                    ...selectedRoom,
                    availableRooms: selectedRoom.availableRooms - 1
                });
            }
        } catch (error) {
            console.error('Ошибка обновления ресурсов:', error);
            throw error;
        }
    };

    const renderStepContent = () => {
        console.log('Текущий шаг:', currentStep);
        switch (currentStep) {
            case 1: 
                return (
                    <div className="flights-step">
                        <h2>Выберите авиабилет для вашего тура</h2>
                        {tour.routes?.map(route => (
                            <div key={route.id} className="route-section">
                                <h3>{route.departureAirportCode} → {route.arrivalAirportCode}</h3>
                                <div className="flights-list">
                                    {routeTickets[route.id]?.map(ticket => (
                                        <FlightCard 
                                            key={ticket.id}
                                            ticket={ticket}
                                            selected={selectedFlight?.id === ticket.id}
                                            onSelect={handleFlightSelect}
                                        />
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                );
                
            case 2: 
                return (
                    <div className="rooms-step">
                        <h2>Выберите размещение</h2>
                        {hotelsWithRooms.map(hotel => (
                            <div key={hotel.id} className="hotel-section">
                                <h3>{hotel.name} ({hotel.rating}★)</h3>
                                <div className="rooms-list">
                                    {hotel.rooms.map(room => (
                                        <RoomCard 
                                            key={room.id}
                                            room={room}
                                            selected={selectedRoom?.id === room.id}
                                            onSelect={handleRoomSelect}
                                        />
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                );
                
            case 3: 
                return (
                    <div className="guest-info-step">
                        <h2>Информация о госте</h2>
                        <GuestInfoForm 
                            data={guestData}
                            onChange={handleInputChange}
                        />
                    </div>
                );

            case 4: 
                return (
                    <ConfirmationStep
                        tour={tour}
                        flight={selectedFlight}
                        room={selectedRoom}
                        activities={selectedActivities}
                        totalPrice={totalPrice}
                        paymentMethod={paymentMethod}
                        onPaymentMethodChange={setPaymentMethod}
                        onSubmit={confirmBooking}
                    />
                );
            default:
                return null;
        }
    };

    if (loading) return <div className="loading">Загрузка деталей тура...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!tour) return <div className="error">Тур не найден</div>;

    return (
        <div className="tour-booking-page">
            <h1>Бронирование тура: {tour.tourName}</h1>
            <div className="steps-indicator">
                <span className={currentStep >= 1 ? 'active' : ''}>1. Авиабилет</span>
                <span className={currentStep >= 2 ? 'active' : ''}>2. Отель</span>
                <span className={currentStep >= 3 ? 'active' : ''}>3. Данные гостя</span>
                <span className={currentStep >= 4 ? 'active' : ''}>4. Подтверждение</span>
            </div>

            <div className="step-content">
                {renderStepContent()}
            </div>

            <div className="navigation-buttons">
                {currentStep > 1 && (
                    <button onClick={() => setCurrentStep(prev => prev - 1)}>Назад</button>
                )}
                {currentStep < 4 ? (
                    <button onClick={() => {
                        if (currentStep === 1 && !selectedFlight) {
                            setError('Пожалуйста, выберите авиабилет');
                            return;
                        }
                        if (currentStep === 2 && !selectedRoom) {
                            setError('Пожалуйста, выберите номер');
                            return;
                        }
                        setCurrentStep(prev => prev + 1);
                    }}>
                        Далее
                    </button>
                ) : (
                    <button onClick={confirmBooking}>Подтвердить бронирование</button>
                )}
            </div>

            {error && <div className="error-message">{error}</div>}
        </div>
    );
};

const FlightCard = ({ ticket, selected, onSelect }) => (
    <div className={`flight-card ${selected ? 'selected' : ''}`}>
        <div className="flight-header">
            <span>{ticket.flightNumber}</span>
            <span className="price">${ticket.cost.toFixed(2)}</span>
        </div>
        <div className="flight-details">
            <div className="departure">
                <span className="time">{new Date(ticket.departureTime).toLocaleTimeString()}</span>
                <span className="airport">{ticket.departureAirportCode}</span>
            </div>
            <div className="arrival">
                <span className="time">{new Date(ticket.arrivalTime).toLocaleTimeString()}</span>
                <span className="airport">{ticket.arrivalAirportCode}</span>
            </div>
        </div>
        <button 
            onClick={() => onSelect(ticket)}
            disabled={ticket.status !== 'AVAILABLE'}
        >
            {selected ? 'Выбрано' : 'Выбрать'}
        </button>
    </div>
);

const RoomCard = ({ room, selected, onSelect }) => (
    <div className={`room-card ${selected ? 'selected' : ''}`}>
        <img 
            src={room.imageUrls ? `http://localhost:8080/room/images/${room.imageUrls}` : 'placeholder.jpg'} 
            alt={room.type}
        />
        <div className="room-info">
            <h4>{room.type} номер</h4>
            <p>${room.cost.toFixed(2)} за ночь</p>
            <p>Макс. гостей: {room.maxGuests}</p>
            <button 
                onClick={() => onSelect(room)}
                disabled={room.availableRooms <= 0}
            >
                {selected ? 'Выбрано' : 'Выбрать'}
            </button>
        </div>
    </div>
);

const ActivityCard = ({ activity, selected, onToggle }) => (
    <div 
        className={`activity-card ${selected ? 'selected' : ''}`}
        onClick={() => onToggle(activity)}
    >
        <img src={activity.imageUrl || 'activity-placeholder.jpg'} alt={activity.name} />
        <h4>{activity.name}</h4>
        <p>${activity.cost.toFixed(2)}</p>
        <p>{activity.duration} часов</p>
    </div>
);

const GuestInfoForm = ({ data, onChange }) => (
    <form className="guest-form">
        <div className="form-section">
            <h3>Персональная информация</h3>
            <div className="form-row">
                <div className="form-group">
                    <label>Фамилия *</label>
                    <input
                        type="text"
                        name="lastName"
                        value={data.lastName}
                        onChange={onChange}
                        required
                        placeholder="Введите фамилию"
                    />
                </div>
                <div className="form-group">
                    <label>Имя *</label>
                    <input
                        type="text"
                        name="firstName"
                        value={data.firstName}
                        onChange={onChange}
                        required
                        placeholder="Введите имя"
                    />
                </div>
                <div className="form-group">
                    <label>Отчество</label>
                    <input
                        type="text"
                        name="middleName"
                        value={data.middleName}
                        onChange={onChange}
                        placeholder="Введите отчество"
                    />
                </div>
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label>Гражданство *</label>
                    <input
                        type="text"
                        name="citizenship"
                        value={data.citizenship}
                        onChange={onChange}
                        required
                        placeholder="Введите гражданство"
                    />
                </div>
                <div className="form-group">
                    <label>Дата рождения *</label>
                    <input
                        type="date"
                        name="birthDate"
                        value={data.birthDate}
                        onChange={onChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Пол *</label>
                    <select
                        name="gender"
                        value={data.gender}
                        onChange={onChange}
                        required
                    >
                        <option value="">Выберите пол</option>
                        <option value="MALE">Мужской</option>
                        <option value="FEMALE">Женский</option>
                    </select>
                </div>
            </div>
        </div>

        <div className="form-section">
            <h3>Паспортные данные</h3>
            <div className="form-row">
                <div className="form-group">
                    <label>Тип паспорта *</label>
                    <input
                        type="text"
                        name="passportType"
                        value={data.passportType}
                        onChange={onChange}
                        required
                        placeholder="Введите тип паспорта"
                    />
                </div>
                <div className="form-group">
                    <label>Номер паспорта *</label>
                    <input
                        type="text"
                        name="passportNumber"
                        value={data.passportNumber}
                        onChange={onChange}
                        required
                        placeholder="Введите номер паспорта"
                    />
                </div>
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label>Дата выдачи *</label>
                    <input
                        type="date"
                        name="passportIssueDate"
                        value={data.passportIssueDate}
                        onChange={onChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Срок действия *</label>
                    <input
                        type="date"
                        name="passportExpiryDate"
                        value={data.passportExpiryDate}
                        onChange={onChange}
                        required
                    />
                </div>
            </div>

            <div className="form-row">
                <div className="form-group full-width">
                    <label>Орган, выдавший документ *</label>
                    <input
                        type="text"
                        name="issuingAuthority"
                        value={data.issuingAuthority}
                        onChange={onChange}
                        required
                        placeholder="Введите орган, выдавший документ"
                    />
                </div>
            </div>
        </div>
    </form>
);

const ConfirmationStep = ({ 
    tour, 
    flight, 
    room, 
    activities, 
    totalPrice, 
    paymentMethod, 
    onPaymentMethodChange, 
    onSubmit 
}) => (
    <div className="confirmation-step">
        <h2>Проверьте информацию о бронировании</h2>
        
        <div className="summary-section">
            <h3>Детали тура</h3>
            <p>{tour.tourName}</p>
            <p>{new Date(tour.startDate).toLocaleDateString()} - {new Date(tour.endDate).toLocaleDateString()}</p>
        </div>
        
        {flight && (
            <div className="summary-section">
                <h3>Авиабилеты</h3>
                <p>{flight.flightNumber}: {flight.departureAirportCode} → {flight.arrivalAirportCode}</p>
                <p>${flight.cost.toFixed(2)}</p>
            </div>
        )}
        
        {room && (
            <div className="summary-section">
                <h3>Размещение</h3>
                <p>{room.type} номер в {room.hotelName}</p>
                <p>{room.cost.toFixed(2)} руб. за ночь</p>
            </div>
        )}
        
        {activities.length > 0 && (
            <div className="summary-section">
                <h3>Мероприятия</h3>
                <ul>
                    {activities.map(activity => (
                        <li key={activity.id}>
                            {activity.name} - ${activity.cost.toFixed(2)}
                        </li>
                    ))}
                </ul>
            </div>
        )}
        
        <div className="payment-section">
            <h3>Способ оплаты</h3>
            <select
                value={paymentMethod}
                onChange={(e) => onPaymentMethodChange(e.target.value)}
            >
                <option value="CREDIT_CARD">Кредитная карта</option>
                <option value="ONLINE">Онлайн</option>
                <option value="BANK_TRANSFER">Банковский перевод</option>
            </select>
        </div>
        
        <div className="total-section">
            <h3>Итоговая цена:</h3>
            <p className="total-price">{totalPrice.toFixed(2)} руб.</p>
        </div>
        
        <button className="confirm-button" onClick={onSubmit}>
            Подтвердить и оплатить
        </button>
    </div>
);

export default TourDetail;