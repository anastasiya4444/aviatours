import React, { useEffect, useState } from 'react';
import axios from 'axios';

const TourManagement = () => {
    const [activeStep, setActiveStep] = useState(0);
    const [tourId, setTourId] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [notification, setNotification] = useState({ show: false, message: '', type: '' });
    const [airTickets, setAirTickets] = useState([]);
    const [tourName, setTourName] = useState('');
    const [description, setDescription] = useState('');
    const [programDays, setProgramDays] = useState([{
        dayNumber: 1,
        activityIds: [] 
    }]);
    const [routes, setRoutes] = useState([{
        departureAirportCode: '',
        arrivalAirportCode: '',
        departureDate: '',
        arrivalDate: '',
        tickets: []
    }]);
    const [selectedHotels, setSelectedHotels] = useState([]);
    const [selectedServices, setSelectedServices] = useState([]);

    const [hotels, setHotels] = useState([]);
    const [services, setServices] = useState([]);
    const [activities, setActivities] = useState([]);

    const steps = [
        { title: 'Основная информация', description: 'Детали тура' },
        { title: 'Маршруты', description: 'Добавление маршрутов' },
        { title: 'Программа', description: 'Ежедневные активности' },
        { title: 'Сервисы', description: 'Отели и доп. услуги' },
    ];
    
    useEffect(() => {
        const fetchActivities = async () => {
            try {
                const response = await axios.get('http://localhost:8080/activity/getAll');
                setActivities(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке активностей:', error);
            }
        };
        fetchActivities();
    }, []);

    const handleAddDay = () => {
        setProgramDays([...programDays, {
            dayNumber: programDays.length + 1,
            activityIds: []
        }]);
    };
    
    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                setIsLoading(true);
                const [hotelsRes, servicesRes, activitiesRes, airTicketsRes] = await Promise.all([
                    axios.get('http://localhost:8080/hotel/getAll'),
                    axios.get('http://localhost:8080/additional_service/getAll'),
                    axios.get('http://localhost:8080/activity/getAll'),
                    axios.get('http://localhost:8080/air_ticket/getAll')
                ]);
                setHotels(hotelsRes.data);
                setServices(servicesRes.data);
                setActivities(activitiesRes.data);
                setAirTickets(airTicketsRes.data);
            } catch (error) {
                showError('Не удалось загрузить начальные данные');
            } finally {
                setIsLoading(false);
            }
        };

        fetchInitialData();
    }, []);

    const showError = (message) => {
        setNotification({ show: true, message, type: 'error' });
        setTimeout(() => setNotification({ show: false, message: '', type: '' }), 5000);
    };

    const showSuccess = (message) => {
        setNotification({ show: true, message, type: 'success' });
        setTimeout(() => setNotification({ show: false, message: '', type: '' }), 3000);
    };

    const handleAddRoute = () => {
        setRoutes([...routes, {
            departureAirportCode: '',
            arrivalAirportCode: '',
            departureDate: '',
            arrivalDate: '',
            tickets: []
        }]);
    };

    const handleCreateBasicTour = async () => {
        try {
            setIsLoading(true);
            const response = await axios.post('http://localhost:8080/tour/createBasic', {
                tourName,
                description
            });
            setTourId(response.data.id);
            showSuccess('Базовый тур создан');
            setActiveStep(1);
        } catch (error) {
            showError('Не удалось создать тур');
        } finally {
            setIsLoading(false);
        }
    };
    
    const handleAddActivity = (dayIndex, activityId) => {
        const updated = [...programDays];
        if (!updated[dayIndex].activityIds.includes(activityId)) {
            updated[dayIndex].activityIds.push(activityId);
            setProgramDays(updated);
        }
    };

    const handleRemoveActivity = (dayIndex, activityId) => {
        const updated = [...programDays];
        updated[dayIndex].activityIds = updated[dayIndex].activityIds.filter(id => id !== activityId);
        setProgramDays(updated);
    };
    
    const handleAddRoutes = async () => {
        try {
            const formattedRoutes = routes.map(route => ({
                ...route,
                departureDate: new Date(route.departureDate).toISOString(),
                arrivalDate: new Date(route.arrivalDate).toISOString(),
                ticketIds: route.tickets.map(ticket => ticket.id)
            }));
    
            await axios.post(`http://localhost:8080/tour/${tourId}/addRoutes`, formattedRoutes);
            showSuccess('Маршруты успешно добавлены');
            setActiveStep(2);
        } catch (error) {
            showError('Ошибка при добавлении маршрутов: ' + (error.response?.data || error.message));
        } finally {
            setIsLoading(false);
        }
    };

    const handleAddProgram = async () => {
        try {
            setIsLoading(true);
            const programData = {
                days: programDays.map(day => ({
                    dayNumber: day.dayNumber,
                    activities: day.activityIds.map(id => ({ id }))
                }))
            };

            await axios.post(`http://localhost:8080/tour/${tourId}/addProgram`, programData);
            showSuccess('Программа успешно добавлена');
            setActiveStep(3);
        } catch (error) {
            showError('Ошибка при добавлении программы: ' + (error.response?.data || error.message));
        } finally {
            setIsLoading(false);
        }
    };

    const handleAddServices = async () => {
        try {
            setIsLoading(true);
            const servicesData = {
                hotelIds: selectedHotels,
                additionalServiceIds: selectedServices
            };
            
            console.log("Отправка данных:", servicesData);
            
            await axios.post(`http://localhost:8080/tour/${tourId}/addServices`, servicesData);
            showSuccess('Сервисы успешно добавлены!');
        } catch (error) {
            showError('Ошибка при добавлении сервисов: ' + (error.response?.data || error.message));
        } finally {
            setIsLoading(false);
        }
    };

    const renderStepContent = () => {
        switch (activeStep) {
            case 0:
                return (
                    <div className="form-container">
                        <div className="form-group">
                            <label>Название тура *</label>
                            <input
                                type="text"
                                value={tourName}
                                onChange={(e) => setTourName(e.target.value)}
                                placeholder="Введите название тура"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Описание тура *</label>
                            <textarea
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                placeholder="Введите описание тура"
                                rows={4}
                                required
                            />
                        </div>

                        <button
                            className="btn-primary"
                            onClick={handleCreateBasicTour}
                            disabled={!tourName || !description || isLoading}
                        >
                            {isLoading ? 'Создание...' : 'Создать тур'}
                        </button>
                    </div>
                );

            case 1:
                return (
                    <div className="routes-container">
                        {routes.map((route, routeIndex) => (
                            <div key={routeIndex} className="card">
                                <h3>Маршрут {routeIndex + 1}</h3>

                                <div className="form-row">
                                    <div className="form-group">
                                        <label>Код аэропорта отправления *</label>
                                        <input
                                            type="text"
                                            value={route.departureAirportCode}
                                            onChange={(e) => {
                                                const updated = [...routes];
                                                updated[routeIndex].departureAirportCode = e.target.value;
                                                setRoutes(updated);
                                            }}
                                            placeholder="Например: JFK"
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Код аэропорта прибытия *</label>
                                        <input
                                            type="text"
                                            value={route.arrivalAirportCode}
                                            onChange={(e) => {
                                                const updated = [...routes];
                                                updated[routeIndex].arrivalAirportCode = e.target.value;
                                                setRoutes(updated);
                                            }}
                                            placeholder="Например: LHR"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="form-row">
                                    <div className="form-group">
                                        <label>Дата отправления *</label>
                                        <input
                                            type="datetime-local"
                                            value={route.departureDate}
                                            onChange={(e) => {
                                                const updated = [...routes];
                                                updated[routeIndex].departureDate = e.target.value;
                                                setRoutes(updated);
                                            }}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Дата прибытия *</label>
                                        <input
                                            type="datetime-local"
                                            value={route.arrivalDate}
                                            onChange={(e) => {
                                                const updated = [...routes];
                                                updated[routeIndex].arrivalDate = e.target.value;
                                                setRoutes(updated);
                                            }}
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="form-group">
                                    <label>Выберите билеты</label>
                                    <select
                                        multiple
                                        onChange={(e) => {
                                            const selectedOptions = Array.from(e.target.selectedOptions)
                                                .map(option => parseInt(option.value));
                                            const updated = [...routes];
                                            updated[routeIndex].ticketIds = selectedOptions;
                                            setRoutes(updated);
                                        }}
                                    >
                                        {airTickets.map(ticket => (
                                            <option key={ticket.id} value={ticket.id}>
                                                {ticket.flightNumber} ({ticket.departureAirportCode} → {ticket.arrivalAirportCode})
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        ))}

                        <div className="button-group">
                            <button
                                className="btn-outline"
                                onClick={handleAddRoute}
                            >
                                Добавить маршрут
                            </button>

                            <button
                                className="btn-primary"
                                onClick={handleAddRoutes}
                                disabled={isLoading || routes.some(r =>
                                    !r.departureAirportCode ||
                                    !r.arrivalAirportCode ||
                                    !r.departureDate ||
                                    !r.arrivalDate
                                )}
                            >
                                {isLoading ? 'Сохранение...' : 'Сохранить маршруты'}
                            </button>
                        </div>
                    </div>
                );

            case 2:
                return (
                    <div className="program-container">
                        {programDays.map((day, dayIndex) => (
                            <div key={dayIndex} className="card">
                                <h3>День {day.dayNumber}</h3>

                                <div className="form-group">
                                    <label>Добавить мероприятие</label>
                                    <select
                                        onChange={(e) => handleAddActivity(dayIndex, parseInt(e.target.value))}
                                        value=""
                                    >
                                        <option value="">Выберите активность</option>
                                        {activities.map(activity => (
                                            <option key={activity.id} value={activity.id}>
                                                {activity.activityType} - {activity.description}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className="activities-list">
                                    {day.activityIds.map(activityId => {
                                        const activity = activities.find(a => a.id === activityId);
                                        return activity ? (
                                            <div key={activityId} className="activity-item">
                                                <div className="activity-details">
                                                    <strong>{activity.activityType}</strong>
                                                    <p>{activity.description}</p>
                                                    <small>Стоимость: ${activity.cost}</small>
                                                </div>
                                                <button
                                                    className="btn-danger"
                                                    onClick={() => handleRemoveActivity(dayIndex, activityId)}
                                                >
                                                    Удалить
                                                </button>
                                            </div>
                                        ) : null;
                                    })}
                                </div>
                            </div>
                        ))}

                        <div className="button-group">
                            <button className="btn-outline" onClick={handleAddDay}>
                                Добавить следующий день
                            </button>

                            <button
                                className="btn-primary"
                                onClick={handleAddProgram}
                                disabled={isLoading || programDays.some(day => day.activityIds.length === 0)}
                            >
                                {isLoading ? 'Сохранение...' : 'Сохранить программу'}
                            </button>
                        </div>
                    </div>
                );

            case 3:
                return (
                    <div className="services-container">
                        <div className="services-section">
                            <h3>Отели</h3>
                            <div className="items-grid">
                                {hotels.map(hotel => (
                                    <div
                                        key={hotel.id}
                                        className={`card ${selectedHotels.includes(hotel.id) ? 'selected' : ''}`}
                                        onClick={() => {
                                            setSelectedHotels(prev =>
                                                prev.includes(hotel.id)
                                                    ? prev.filter(id => id !== hotel.id)
                                                    : [...prev, hotel.id]
                                            );
                                        }}
                                    >
                                        <div className="item-content">
                                            <h4>{hotel.name}</h4>
                                            <p>{hotel.address}</p>
                                            <p>Звёздность: {hotel.rating}/5</p>
                                            <p>Цена за ночь: {hotel.pricePerNight} руб.</p>
                                        </div>
                                        <input
                                            type="checkbox"
                                            checked={selectedHotels.includes(hotel.id)}
                                            onChange={() => { }}
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>

                        <div className="services-section">
                            <h3>Дополнительные услуги</h3>
                            <div className="items-grid">
                                {services.map(service => (
                                    <div
                                        key={service.id}
                                        className={`card ${selectedServices.includes(service.id) ? 'selected' : ''}`}
                                        onClick={() => {
                                            setSelectedServices(prev =>
                                                prev.includes(service.id)
                                                    ? prev.filter(id => id !== service.id)
                                                    : [...prev, service.id]
                                            );
                                        }}
                                    >
                                        <div className="item-content">
                                            <h4>{service.serviceType}</h4>
                                            <p>{service.description}</p>
                                            <p>Цена: {service.cost} руб.</p>
                                        </div>
                                        <input
                                            type="checkbox"
                                            checked={selectedServices.includes(service.id)}
                                            onChange={() => { }}
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>

                        <button
                            className="btn-primary"
                            onClick={handleAddServices}
                            disabled={isLoading || selectedHotels.length === 0}
                        >
                            {isLoading ? 'Завершение...' : 'Завершить тур'}
                        </button>
                    </div>
                );

            default:
                return null;
        }
    };

    return (
        <div className="tour-management-container">
            <h1>Управление турами</h1>

            {notification.show && (
                <div className={`notification ${notification.type}`}>
                    {notification.message}
                </div>
            )}

            <div className="stepper">
                {steps.map((step, index) => (
                    <div
                        key={index}
                        className={`step ${index === activeStep ? 'active' : ''} ${index < activeStep ? 'completed' : ''}`}
                        onClick={() => index < activeStep && setActiveStep(index)}
                    >
                        <div className="step-number">{index + 1}</div>
                        <div className="step-content">
                            <div className="step-title">{step.title}</div>
                            <div className="step-description">{step.description}</div>
                        </div>
                    </div>
                ))}
            </div>

            {renderStepContent()}
        </div>
    );
};

export default TourManagement;