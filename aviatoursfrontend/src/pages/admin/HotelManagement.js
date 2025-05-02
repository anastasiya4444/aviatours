import React, { useState, useEffect } from 'react';
import axios from 'axios';
import RoomManagement from './RoomManagement';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

const HotelManagement = () => {
  const [hotels, setHotels] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [showRooms, setShowRooms] = useState(null);
  const [formData, setFormData] = useState({
    description: '',
    address: '',
    mealType: 'RO',
    rating: 0,
    hotelFeatures: ''
  });

  const mealTypes = [
    { value: 'RO', label: 'Room Only' },
    { value: 'BB', label: 'Bed & Breakfast' },
    { value: 'HB', label: 'Half Board' },
    { value: 'FB', label: 'Full Board' },
    { value: 'AI', label: 'All Inclusive' }
  ];

  useEffect(() => {
    fetchHotels();
  }, []);

  const fetchHotels = async () => {
    try {
      const response = await api.get('/hotel/getAll');
      setHotels(response.data);
    } catch (error) {
      console.error('Error fetching hotels:', error);
      alert('Failed to load hotels');
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      if (editingId) {
        await api.put('/hotel/update', { ...formData, id: editingId });
      } else {
        await api.post('/hotel/add', formData);
      }
      
      fetchHotels();
      resetForm();
    } catch (error) {
      console.error('Error:', error.response?.data || error.message);
      alert('Operation failed');
    }
  };

  const handleEdit = (hotel) => {
    setFormData({
      description: hotel.description,
      address: hotel.address,
      mealType: hotel.mealType,
      rating: hotel.rating,
      hotelFeatures: hotel.hotelFeatures
    });
    setEditingId(hotel.id);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this hotel?')) return;
    
    try {
      await api.delete(`/hotel/${id}`);
      fetchHotels();
    } catch (error) {
      console.error('Error deleting hotel:', error);
      alert('Failed to delete hotel');
    }
  };

  const resetForm = () => {
    setFormData({
      description: '',
      address: '',
      mealType: 'RO',
      rating: 0,
      hotelFeatures: ''
    });
    setEditingId(null);
    setShowRooms(null);
  };

  return (
    <div className="page-container">
      <h1>Hotel Management</h1>
      
      {!showRooms ? (
        <>
          <div className="form-section">
            <h2>{editingId ? 'Edit Hotel' : 'Add New Hotel'}</h2>
            <form onSubmit={handleSubmit}>
              <div>
                <label>Description:</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div>
                <label>Address:</label>
                <input
                  type="text"
                  name="address"
                  value={formData.address}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div>
                <label>Meal Type:</label>
                <select
                  name="mealType"
                  value={formData.mealType}
                  onChange={handleInputChange}
                  required
                >
                  {mealTypes.map(type => (
                    <option key={type.value} value={type.value}>
                      {type.label}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label>Rating:</label>
                <input
                  type="number"
                  name="rating"
                  value={formData.rating}
                  onChange={handleInputChange}
                  min="0"
                  max="5"
                  step="0.1"
                  required
                />
              </div>

              <div>
                <label>Features (comma separated):</label>
                <input
                  type="text"
                  name="hotelFeatures"
                  value={formData.hotelFeatures}
                  onChange={handleInputChange}
                  placeholder="WiFi, Pool, Spa, etc."
                />
              </div>

              <div className="form-actions">
                <button type="submit" className="btn-primary">
                  {editingId ? 'Update Hotel' : 'Add Hotel'}
                </button>
                {editingId && (
                  <button type="button" onClick={resetForm} className="btn-secondary">
                    Cancel
                  </button>
                )}
              </div>
            </form>
          </div>

          <div className="table-section">
            <h2>All Hotels</h2>
            <table className="data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Description</th>
                  <th>Address</th>
                  <th>Meal Type</th>
                  <th>Rating</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {hotels.map(hotel => (
                  <tr key={hotel.id}>
                    <td>{hotel.id}</td>
                    <td>{hotel.description.substring(0, 50)}...</td>
                    <td>{hotel.address.substring(0, 30)}...</td>
                    <td>{mealTypes.find(t => t.value === hotel.mealType)?.label}</td>
                    <td>{hotel.rating}</td>
                    <td className="actions">
                      <button onClick={() => handleEdit(hotel)} className="btn-edit">
                        Edit
                      </button>
                      <button 
                        onClick={() => setShowRooms(hotel.id)} 
                        className="btn-view"
                      >
                        Rooms
                      </button>
                      <button 
                        onClick={() => handleDelete(hotel.id)} 
                        className="btn-delete"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      ) : (
        <RoomManagement 
          hotelId={showRooms} 
          onBack={() => setShowRooms(null)} 
          hotelData={hotels.find(h => h.id === showRooms)}
        />
      )}
    </div>
  );
};

export default HotelManagement;