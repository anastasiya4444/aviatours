import React, { useState, useEffect } from 'react';
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

const FlightManagement = () => {
    const [flights, setFlights] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [formData, setFormData] = useState({
        flightNumber: '',
        departureAirportCode: '',
        arrivalAirportCode: '',
        departureTime: '',
        arrivalTime: '',
        seatNumber: '',
        cost: '',
        status: 'AVAILABLE'
    });

    useEffect(() => {
        fetchFlights();
    }, []);

    const fetchFlights = async () => {
        try {
            const response = await api.get('/air_ticket/getAll');
            setFlights(response.data);
        } catch (error) {
            console.error('Error fetching flights:', error);
            alert('Failed to load flights');
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const formatDateForBackend = (dateString) => {
        if (!dateString) return '';
        return dateString.length === 16 ? `${dateString}:00` : dateString;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const payload = {
                ...formData,
                departureTime: formatDateForBackend(formData.departureTime),
                arrivalTime: formatDateForBackend(formData.arrivalTime)
            };

            if (editingId) {
                await api.put('/air_ticket/update', {
                    ...payload,
                    id: editingId,
                    seatNumber: formData.seatNumber
                });
            } else {
                const batchRequest = {
                    seatFrom: parseInt(formData.seatFrom),
                    seatTo: parseInt(formData.seatTo),
                    prototype: payload
                };

                await api.post('/air_ticket/batch_create', batchRequest);
            }

            fetchFlights();
            resetForm();
        } catch (error) {
            console.error('Error:', error.response?.data || error.message);
            alert('Operation failed: ' + (error.response?.data?.message || error.message));
        }
    };

    const handleEdit = (flight) => {
        setFormData({
            flightNumber: flight.flightNumber,
            departureAirportCode: flight.departureAirportCode,
            arrivalAirportCode: flight.arrivalAirportCode,
            departureTime: flight.departureTime.substring(0, 16),
            arrivalTime: flight.arrivalTime.substring(0, 16),
            seatNumber: flight.seatNumber,
            cost: flight.cost,
            status: flight.status
        });
        setEditingId(flight.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this ticket?')) return;

        try {
            await api.delete(`/air_ticket/${id}`);
            fetchFlights();
        } catch (error) {
            console.error('Error deleting flight:', error.response?.data || error.message);
            alert('Failed to delete ticket');
        }
    };

    const resetForm = () => {
        setFormData({
            flightNumber: '',
            departureAirportCode: '',
            arrivalAirportCode: '',
            departureTime: '',
            arrivalTime: '',
            seatNumber: '',
            cost: '',
            status: 'AVAILABLE'
        });
        setEditingId(null);
    };

    return (
        <div className="page-container">
            <h1>Flight Tickets Management</h1>

            <div className="form-section">
                <h2>{editingId ? 'Edit Ticket' : 'Create New Ticket'}</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Flight Number:</label>
                        <input
                            type="text"
                            name="flightNumber"
                            value={formData.flightNumber}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div>
                        <label>Departure Airport:</label>
                        <input
                            type="text"
                            name="departureAirportCode"
                            value={formData.departureAirportCode}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div>
                        <label>Arrival Airport:</label>
                        <input
                            type="text"
                            name="arrivalAirportCode"
                            value={formData.arrivalAirportCode}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div>
                        <label>Departure Time:</label>
                        <input
                            type="datetime-local"
                            name="departureTime"
                            value={formData.departureTime}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div>
                        <label>Arrival Time:</label>
                        <input
                            type="datetime-local"
                            name="arrivalTime"
                            value={formData.arrivalTime}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    {!editingId && (
                        <>
                            <div>
                                <label>Seat Range (from - to):</label>
                                <input
                                    type="number"
                                    name="seatFrom"
                                    value={formData.seatFrom}
                                    onChange={(e) => setFormData(prev => ({ ...prev, seatFrom: parseInt(e.target.value) || 1 }))}
                                    min="1"
                                    required
                                />
                                <input
                                    type="number"
                                    name="seatTo"
                                    value={formData.seatTo}
                                    onChange={(e) => setFormData(prev => ({ ...prev, seatTo: parseInt(e.target.value) || 1 }))}
                                    min={formData.seatFrom}
                                    required
                                />
                            </div>
                        </>
                    )}

                    <div>
                        <label>Cost:</label>
                        <input
                            type="number"
                            name="cost"
                            value={formData.cost}
                            onChange={handleInputChange}
                            step="0.01"
                            required
                        />
                    </div>

                    <div>
                        <label>Status:</label>
                        <select
                            name="status"
                            value={formData.status}
                            onChange={handleInputChange}
                        >
                            <option value="AVAILABLE">Available</option>
                            <option value="BOOKED">Booked</option>
                            <option value="CANCELLED">Cancelled</option>
                        </select>
                    </div>

                    <button type="submit">
                        {editingId ? 'Update Ticket' : 'Create Tickets'}
                    </button>
                    {editingId && (
                        <button type="button" onClick={resetForm}>
                            Cancel
                        </button>
                    )}
                </form>
            </div>

            <div className="tickets-list">
                <h2>All Tickets</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Flight Number</th>
                            <th>Route</th>
                            <th>Departure</th>
                            <th>Arrival</th>
                            <th>Seat</th>
                            <th>Cost</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {flights.map(flight => (
                            <tr key={flight.id}>
                                <td>{flight.flightNumber}</td>
                                <td>{flight.departureAirportCode} → {flight.arrivalAirportCode}</td>
                                <td>{new Date(flight.departureTime).toLocaleString()}</td>
                                <td>{new Date(flight.arrivalTime).toLocaleString()}</td>
                                <td>{flight.seatNumber}</td>
                                <td>{flight.cost}</td>
                                <td>{flight.status}</td>
                                <td>
                                    <button onClick={() => handleEdit(flight)}>Edit</button>
                                    <button onClick={() => handleDelete(flight.id)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default FlightManagement;