import React, { useState, useEffect } from 'react';
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
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
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetchAllTickets();
    }, []);

    const fetchAllTickets = async () => {
        setIsLoading(true);
        try {
            const response = await api.get('/air_ticket/getAll');
            setTickets(response.data);
            setFilteredTickets(response.data);
        } catch (error) {
            console.error('Error fetching tickets:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const formatDateTimeForBackend = (dateTimeString) => {
        if (!dateTimeString) return null;
        
        const date = new Date(dateTimeString);
        const pad = (num) => num.toString().padStart(2, '0');
        
        return `${date.getFullYear()}-${pad(date.getMonth()+1)}-${pad(date.getDate())} ` +
               `${pad(date.getHours())}:${pad(date.getMinutes())}:00`;
    };
    
    const applyFilters = async () => {
        setIsLoading(true);
        try {
            const params = {
                flightNumber: filters.flightNumber || undefined,
                departureAirportCode: filters.departureAirportCode || undefined,
                arrivalAirportCode: filters.arrivalAirportCode || undefined,
                departureTimeFrom: filters.departureTimeFrom 
                    ? formatDateTimeForBackend(filters.departureTimeFrom) 
                    : undefined,
                departureTimeTo: filters.departureTimeTo 
                    ? formatDateTimeForBackend(filters.departureTimeTo) 
                    : undefined,
                minCost: filters.minCost || undefined,
                maxCost: filters.maxCost || undefined,
                seatNumber: filters.seatNumber || undefined,
                status: filters.status || undefined
            };
    
            Object.keys(params).forEach(key => {
                if (params[key] === undefined) {
                    delete params[key];
                }
            });
    
            const response = await api.get('/air_ticket/filter', { params });
            setFilteredTickets(response.data);
        } catch (error) {
            console.error('Error filtering tickets:', error);
            alert('Ошибка при фильтрации. Проверьте введенные данные.');
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
        fetchAllTickets();
    };

    return (
        <div className="page-container">
            <h1>Фильтрация авиабилетов</h1>
            
            <div className="filter-section">
                <h2>Фильтры</h2>
                
                <div className="filter-row">
                    <div className="filter-group">
                        <label>Номер рейса:</label>
                        <input 
                            type="text" 
                            name="flightNumber"
                            value={filters.flightNumber}
                            onChange={handleFilterChange}
                            placeholder="Все рейсы"
                        />
                    </div>
                    
                    <div className="filter-group">
                        <label>Аэропорт вылета:</label>
                        <input 
                            type="text" 
                            name="departureAirportCode"
                            value={filters.departureAirportCode}
                            onChange={handleFilterChange}
                            placeholder="Любой аэропорт"
                        />
                    </div>
                    
                    <div className="filter-group">
                        <label>Аэропорт прилета:</label>
                        <input 
                            type="text" 
                            name="arrivalAirportCode"
                            value={filters.arrivalAirportCode}
                            onChange={handleFilterChange}
                            placeholder="Любой аэропорт"
                        />
                    </div>
                </div>
                
                <div className="filter-row">
                    <div className="filter-group">
                        <label>Дата вылета от:</label>
                        <input 
                            type="datetime-local" 
                            name="departureTimeFrom"
                            value={filters.departureTimeFrom}
                            onChange={handleFilterChange}
                        />
                    </div>
                    
                    <div className="filter-group">
                        <label>Дата вылета до:</label>
                        <input 
                            type="datetime-local" 
                            name="departureTimeTo"
                            value={filters.departureTimeTo}
                            onChange={handleFilterChange}
                        />
                    </div>
                </div>
                
                <div className="filter-row">
                    <div className="filter-group">
                        <label>Минимальная цена:</label>
                        <input 
                            type="number" 
                            name="minCost"
                            value={filters.minCost}
                            onChange={handleFilterChange}
                            placeholder="Любая цена"
                            min="0"
                        />
                    </div>
                    
                    <div className="filter-group">
                        <label>Максимальная цена:</label>
                        <input 
                            type="number" 
                            name="maxCost"
                            value={filters.maxCost}
                            onChange={handleFilterChange}
                            placeholder="Любая цена"
                            min="0"
                        />
                    </div>
                </div>
                
                <div className="filter-row">
                    <div className="filter-group">
                        <label>Номер места:</label>
                        <input 
                            type="number" 
                            name="seatNumber"
                            value={filters.seatNumber}
                            onChange={handleFilterChange}
                            placeholder="Любое место"
                            min="1"
                        />
                    </div>
                    
                    <div className="filter-group">
                        <label>Статус:</label>
                        <select 
                            name="status"
                            value={filters.status}
                            onChange={handleFilterChange}
                        >
                            <option value="">Все статусы</option>
                            <option value="available">Доступен</option>
                            <option value="booked">Забронирован</option>
                            <option value="sold">Продан</option>
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
                <h2>Результаты ({filteredTickets.length})</h2>
                
                {isLoading ? (
                    <p>Загрузка данных...</p>
                ) : filteredTickets.length === 0 ? (
                    <p>Билеты не найдены</p>
                ) : (
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Номер рейса</th>
                                <th>Вылет</th>
                                <th>Прилет</th>
                                <th>Аэропорт вылета</th>
                                <th>Аэропорт прилета</th>
                                <th>Цена</th>
                                <th>Место</th>
                                <th>Статус</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredTickets.map(ticket => (
                                <tr key={ticket.id}>
                                    <td>{ticket.id}</td>
                                    <td>{ticket.flightNumber}</td>
                                    <td>{new Date(ticket.departureTime).toLocaleString()}</td>
                                    <td>{new Date(ticket.arrivalTime).toLocaleString()}</td>
                                    <td>{ticket.departureAirportCode}</td>
                                    <td>{ticket.arrivalAirportCode}</td>
                                    <td>{ticket.cost.toFixed(2)}</td>
                                    <td>{ticket.seatNumber}</td>
                                    <td>{ticket.status}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
};

export default FlightFilter;