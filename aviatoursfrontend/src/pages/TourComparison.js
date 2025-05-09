import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const TourComparison = () => {
    const [tours, setTours] = useState([]);
    const [selectedTours, setSelectedTours] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showComparison, setShowComparison] = useState(false);

    useEffect(() => {
        const fetchAllTours = async () => {
            try {
                const response = await axios.get('http://localhost:8080/tour/getAll');
                setTours(response.data);
                console.log(response.data)
            } catch (err) {
                console.error('Error fetching tours:', err);
                setError('Failed to load tours. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchAllTours();
    }, []);

    const toggleTourSelection = (tourId) => {
        if (selectedTours.includes(tourId)) {
            setSelectedTours(selectedTours.filter(id => id !== tourId));
        } else {
            if (selectedTours.length >= 4) {
                alert('You can compare up to 4 tours at once');
                return;
            }
            setSelectedTours([...selectedTours, tourId]);
        }
    };

    const getHotelFeatures = (hotels) => {
        if (!hotels || hotels.length === 0) return 'N/A';
        
        const features = new Set();
        hotels.forEach(hotel => {
            if (hotel.hotelFeatures) {
                hotel.hotelFeatures.split(',').forEach(f => features.add(f.trim()));
            }
        });
        
        return Array.from(features).join(', ') || 'N/A';
    };

    const getRoomTypes = (hotels) => {
        if (!hotels || hotels.length === 0) return 'N/A';
        
        const types = new Set();
        hotels.forEach(hotel => {
            if (hotel.rooms) {
                hotel.rooms.forEach(room => {
                    if (room.type) types.add(room.type);
                });
            }
        });
        
        return Array.from(types).join(', ') || 'N/A';
    };

    const compareFeatures = () => {
        return selectedTours.map(tourId => {
            const tour = tours.find(t => t.id === tourId);
            if (!tour) return null;

            return {
                id: tour.id,
                name: tour.tourName,
                description: tour.description,
                routeCount: tour.routes?.length || 0,
                hotelCount: tour.hotels?.length || 0,
                hotelFeatures: getHotelFeatures(tour.hotels),
                mealTypes: [...new Set(tour.hotels?.map(h => h.mealType))].join(', ') || 'N/A',
                roomTypes: getRoomTypes(tour.hotels),
                bestRating: tour.hotels?.reduce((max, hotel) => 
                    hotel.rating > max ? hotel.rating : max, 0) || 'N/A'
            };
        }).filter(Boolean);
    };

    const featuresToCompare = [
        { name: 'name', label: 'Tour Name' },
        { name: 'description', label: 'Description' },
        { name: 'routeCount', label: 'Number of Routes' },
        { name: 'hotelCount', label: 'Number of Hotels' },
        { name: 'bestRating', label: 'Best Hotel Rating' },
        { name: 'mealTypes', label: 'Available Meal Types' },
        { name: 'hotelFeatures', label: 'Hotel Features' },
        { name: 'roomTypes', label: 'Available Room Types' }
    ];

    if (loading) {
        return (
            <div className="page-container">
                <h1>Tour Comparison</h1>
                <div className="loading">Loading tours...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="page-container">
                <h1>Tour Comparison</h1>
                <div className="error-message">{error}</div>
            </div>
        );
    }

    return (
        <div className="page-container">
            <h1>Сравнение туров</h1>
            
            {!showComparison ? (
                <>
                    <div className="selection-header">
                        <h2>Выберите туры для сравнения (2-4)</h2>
                        <div className="selection-info">
                            <span>Выбраны: {selectedTours.length}</span>
                            <button
                                className={`btn-primary ${selectedTours.length < 2 ? 'disabled' : ''}`}
                                onClick={() => setShowComparison(true)}
                                disabled={selectedTours.length < 2}
                            >
                                Сравнить выбранные
                            </button>
                        </div>
                    </div>
                    
                    <div className="tours-grid">
                        {tours.map(tour => (
                            <div 
                                key={tour.id} 
                                className={`tour-card ${selectedTours.includes(tour.id) ? 'selected' : ''}`}
                                onClick={() => toggleTourSelection(tour.id)}
                            >
                                <div className="tour-card-header">
                                    <h3>{tour.tourName}</h3>
                                    <div className={`selection-check ${selectedTours.includes(tour.id) ? 'checked' : ''}`}>
                                        {selectedTours.includes(tour.id) ? '✓' : '+'}
                                    </div>
                                </div>
                                <p className="description">{tour.description}</p>
                                <div className="tour-preview">
                                    <p><strong>Маршруты:</strong> {tour.routes?.length || 0}</p>
                                    <p><strong>Отели:</strong> {tour.hotels?.length || 0}</p>
                                    {tour.hotels?.length > 0 && (
                                        <p><strong>Best rating:</strong> {Math.max(...tour.hotels.map(h => h.rating || 0))}</p>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                </>
            ) : (
                <>
                    <button 
                        className="btn-secondary back-button"
                        onClick={() => setShowComparison(false)}
                    >
                        ← Вернуться к выбору
                    </button>
                    
                    <div className="comparison-container">
                        <h2>Сравнение {selectedTours.length} Tours</h2>
                        
                        <div className="comparison-table-container">
                            <table className="comparison-table">
                                <thead>
                                    <tr>
                                        <th>Характеристики</th>
                                        {compareFeatures().map(tour => (
                                            <th key={tour.id}>{tour.name}</th>
                                        ))}
                                    </tr>
                                </thead>
                                <tbody>
                                    {featuresToCompare.map(feature => (
                                        <tr key={feature.name}>
                                            <td className="feature-label">{feature.label}</td>
                                            {compareFeatures().map(tour => (
                                                <td key={`${feature.name}-${tour.id}`}>
                                                    {tour[feature.name] || 'N/A'}
                                                </td>
                                            ))}
                                        </tr>
                                    ))}
                                    <tr className="action-row">
                                        <td>Забронировать тур</td>
                                        {compareFeatures().map(tour => (
                                            <td key={`action-${tour.id}`}>
                                                <Link 
                                                    to={`/tour/${tour.id}`} 
                                                    className="btn-book"
                                                >
                                                    Забронировать
                                                </Link>
                                            </td>
                                        ))}
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default TourComparison;