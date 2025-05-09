import React, { useState } from 'react';

const FlightBooking = ({ tour, onNext, initialData }) => {
    const [selectedFlights, setSelectedFlights] = useState(initialData || []);

    const handleSelectFlight = (flight) => {
        setSelectedFlights(prev => [...prev, flight]);
    };

    const handleRemoveFlight = (flightId) => {
        setSelectedFlights(prev => prev.filter(f => f.id !== flightId));
    };

    const handleSubmit = () => {
        onNext({ selectedFlights });
    };

    return (
        <div className="booking-step">
            <h2>Выбрать билет</h2>
            
            <div className="flights-list">
                {tour?.routes?.flatMap(route => 
                    route.tickets?.map(ticket => (
                        <div key={ticket.id} className="flight-card">
                            <div>
                                <p>{ticket.flightNumber}</p>
                                <p>{ticket.departureAirportCode} → {ticket.arrivalAirportCode}</p>
                                <p>{new Date(ticket.departureTime).toLocaleString()}</p>
                            </div>
                            <button 
                                onClick={() => 
                                    selectedFlights.some(f => f.id === ticket.id) 
                                        ? handleRemoveFlight(ticket.id) 
                                        : handleSelectFlight(ticket)
                                }
                            >
                                {selectedFlights.some(f => f.id === ticket.id) ? 'Remove' : 'Выбрать'}
                            </button>
                        </div>
                    ))
                )}
            </div>
            
            <div className="booking-actions">
                <button 
                    onClick={handleSubmit} 
                    disabled={selectedFlights.length === 0}
                >
                    Далее: Выбрать комнаты
                </button>
            </div>
        </div>
    );
};

export default FlightBooking;