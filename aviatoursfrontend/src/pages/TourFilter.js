import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const TourFilter = () => {
    const [tours, setTours] = useState([]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const [filters, setFilters] = useState({
        tourName: '',
        description: '',
        mealType: '',
        minRating: '',
        maxRating: '',
        hotelFeatures: '',
        roomView: '',
        minBeds: '',
        maxBeds: '',
        minRoomSize: '',
        maxRoomSize: '',
        roomType: '',
        hasBath: false,
        hasTerrace: false,
        minRoomCost: '',
        maxRoomCost: '',
        sortBy: 'rating', 
        sortDirection: 'desc' 
    });

    useEffect(() => {
        fetchAllTours();
    }, [filters]); 

    const fetchAllTours = async () => {
        setLoading(true);
        try {
            const response = await axios.get('http://localhost:8080/tour/search', {
                params: filters
            });
            setTours(response.data);
            console.log(response.data);
        } catch (error) {
            console.error('Error fetching tours:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleFilterChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFilters(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSort = (key) => {
        let direction = 'asc';
        if (filters.sortBy === key && filters.sortDirection === 'asc') {
            direction = 'desc';
        }
        setFilters(prev => ({
            ...prev,
            sortBy: key,
            sortDirection: direction
        }));
    };

    const resetFilters = () => {
        setFilters({
            tourName: '',
            description: '',
            mealType: '',
            minRating: '',
            maxRating: '',
            hotelFeatures: '',
            roomView: '',
            minBeds: '',
            maxBeds: '',
            minRoomSize: '',
            maxRoomSize: '',
            roomType: '',
            hasBath: false,
            hasTerrace: false,
            minRoomCost: '',
            maxRoomCost: '',
            sortBy: 'rating',
            sortDirection: 'desc'
        });
    };

    return (
        <div className="page-container">
            <h1>Фильтр туров</h1>
            <nav className="admin-nav">
                
            </nav>
            <div className="filter-container">
                <div className="filter-section">
                    <h3>Фильтры</h3>
                    <div className="filter-row">
                        <div className="filter-group">
                            <label>Название тура</label>
                            <input
                                type="text"
                                name="tourName"
                                value={filters.tourName}
                                onChange={handleFilterChange}
                                placeholder="Search by tour name"
                            />
                        </div>
                        <div className="filter-group">
                            <label>Описание</label>
                            <input
                                type="text"
                                name="description"
                                value={filters.description}
                                onChange={handleFilterChange}
                                placeholder="Search in description"
                            />
                        </div>
                    </div>
                </div>

                <div className="filter-section">
                    <h3>Фильтрация по характеристикам отеля</h3>
                    <div className="filter-row">
                        <div className="filter-group">
                            <label>Тип питания</label>
                            <select
                                name="mealType"
                                value={filters.mealType}
                                onChange={handleFilterChange}
                            >
                                <option value="">Любой</option>
                                <option value="BB">BB</option>
                                <option value="HB">HB</option>
                                <option value="FB">FB</option>
                                <option value="AI">AI</option>
                            </select>
                        </div>
                        <div className="filter-group">
                            <label>Рейтинг</label>
                            <div className="range-inputs">
                                <input
                                    type="number"
                                    name="minRating"
                                    value={filters.minRating}
                                    onChange={handleFilterChange}
                                    placeholder="Min"
                                    min="0"
                                    max="5"
                                    step="0.1"
                                />
                                <span>-</span>
                                <input
                                    type="number"
                                    name="maxRating"
                                    value={filters.maxRating}
                                    onChange={handleFilterChange}
                                    placeholder="Max"
                                    min="0"
                                    max="5"
                                    step="0.1"
                                />
                            </div>
                        </div>
                        <div className="filter-group">
                            <label>Характеристики</label>
                            <input
                                type="text"
                                name="hotelFeatures"
                                value={filters.hotelFeatures}
                                onChange={handleFilterChange}
                                placeholder="Pool, WiFi, etc."
                            />
                        </div>
                    </div>
                </div>

                <div className="filter-section">
                    <h3>Фильтрация по характеристикам комнат</h3>
                    <div className="filter-row">
                        <div className="filter-group">
                            <label>Вид</label>
                            <input
                                type="text"
                                name="roomView"
                                value={filters.roomView}
                                onChange={handleFilterChange}
                                placeholder="Sea view, etc."
                            />
                        </div>
                        <div className="filter-group">
                            <label>Кровати</label>
                            <div className="range-inputs">
                                <input
                                    type="number"
                                    name="minBeds"
                                    value={filters.minBeds}
                                    onChange={handleFilterChange}
                                    placeholder="Min"
                                    min="1"
                                />
                                <span>-</span>
                                <input
                                    type="number"
                                    name="maxBeds"
                                    value={filters.maxBeds}
                                    onChange={handleFilterChange}
                                    placeholder="Max"
                                    min="1"
                                />
                            </div>
                        </div>
                    </div>
                    <div className="filter-row">
                        <div className="filter-group">
                            <label>Размер (м²)</label>
                            <div className="range-inputs">
                                <input
                                    type="number"
                                    name="minRoomSize"
                                    value={filters.minRoomSize}
                                    onChange={handleFilterChange}
                                    placeholder="Min"
                                    min="10"
                                />
                                <span>-</span>
                                <input
                                    type="number"
                                    name="maxRoomSize"
                                    value={filters.maxRoomSize}
                                    onChange={handleFilterChange}
                                    placeholder="Max"
                                    min="10"
                                />
                            </div>
                        </div>
                        <div className="filter-group">
                            <label>Тип</label>
                            <select
                                name="roomType"
                                value={filters.roomType}
                                onChange={handleFilterChange}
                            >
                                <option value="">Любой</option>
                                <option value="Standard">Standard</option>
                                <option value="Deluxe">Deluxe</option>
                                <option value="Suite">Suite</option>
                                <option value="Family">Family</option>
                                <option value="Executive">Executive</option>
                            </select>
                        </div>
                    </div>
                    <div className="filter-row">
                        <div className="filter-group">
                            <label>Цена за ночь</label>
                            <div className="range-inputs">
                                <input
                                    type="number"
                                    name="minRoomCost"
                                    value={filters.minRoomCost}
                                    onChange={handleFilterChange}
                                    placeholder="Min"
                                    min="0"
                                />
                                <span>-</span>
                                <input
                                    type="number"
                                    name="maxRoomCost"
                                    value={filters.maxRoomCost}
                                    onChange={handleFilterChange}
                                    placeholder="Max"
                                    min="0"
                                />
                            </div>
                        </div>
                        <div className="filter-group checkboxes">
                            <label>
                                <input
                                    type="checkbox"
                                    name="hasBath"
                                    checked={filters.hasBath}
                                    onChange={handleFilterChange}
                                />
                                Личная ванная
                            </label>
                            <label>
                                <input
                                    type="checkbox"
                                    name="hasTerrace"
                                    checked={filters.hasTerrace}
                                    onChange={handleFilterChange}
                                />
                                Терасса/Балкон
                            </label>
                        </div>
                    </div>
                </div>

                <div className="filter-actions">
                    <button
                        className="btn-primary"
                        onClick={fetchAllTours}
                    >
                        Применить фильтры
                    </button>
                    <button
                        className="btn-secondary"
                        onClick={resetFilters}
                    >
                        Сбросить фильтры
                    </button>
                </div>
            </div>

            <div className="sort-options">
                <span>Сортировка:</span>
                <button
                    className={`sort-btn ${filters.sortBy === 'rating' ? 'active' : ''}`}
                    onClick={() => handleSort('rating')}
                >
                    Рейтинг {filters.sortBy === 'rating' && (filters.sortDirection === 'asc' ? '↑' : '↓')}
                </button>
                <button
                    className={`sort-btn ${filters.sortBy === 'price' ? 'active' : ''}`}
                    onClick={() => handleSort('price')}
                >
                    Цена {filters.sortBy === 'price' && (filters.sortDirection === 'asc' ? '↑' : '↓')}
                </button>
            </div>

            {loading ? (
                <div className="loading">Загрузка туров...</div>
            ) : (
                <div className="results-container">
                    <h3>Найдены {tours.length}</h3>

                    <div className="tours-grid">
                        {tours.map(tour => (
                            <div key={tour.id} className="tour-card">
                                <h4>{tour.tourName}</h4>
                                <p className="description">{tour.description}</p>

                                <div className="tour-preview">
                                    <p><strong>Маршруты:</strong> {tour.routes?.length || 0}</p>
                                    <p><strong>Отели:</strong> {tour.hotels?.length || 0}</p>
                                </div>

                                <Link to={`/tour/${tour.id}`} className="btn-book">
                                    Забронировать
                                </Link>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default TourFilter;