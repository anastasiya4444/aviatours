import React, { useState, useEffect } from 'react';
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

const RoomManagement = ({ hotelId, onBack, hotelData }) => {
  const [rooms, setRooms] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    view: '',
    beds: 1,
    maxGuests: 1,
    sizeM2: 20,
    type: 'Standard',
    bath: false,
    terrace: false,
    cost: 100,
    availableRooms: 1,
    bookingCount: 0
  });

  const roomTypes = ['Standard', 'Deluxe', 'Suite', 'Family', 'Executive'];

  useEffect(() => {
    fetchRooms();
  }, [hotelId]);

  const fetchRooms = async () => {
    try {
      const response = await api.get(`/room/getByHotel/${hotelId}`);
      setRooms(response.data);
    } catch (error) {
      console.error('Error fetching rooms:', error);
      alert('Failed to load rooms');
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const payload = {
        ...formData,
        hotel: { id: hotelId },
        beds: parseInt(formData.beds),
        maxGuests: parseInt(formData.maxGuests),
        sizeM2: parseFloat(formData.sizeM2),
        cost: parseFloat(formData.cost),
        availableRooms: parseInt(formData.availableRooms)
      };

      if (editingId) {
        await api.put('/room/update', { ...payload, id: editingId });
      } else {
        await api.post('/room/add', payload);
      }
      
      fetchRooms();
      resetForm();
    } catch (error) {
      console.error('Error:', error.response?.data || error.message);
      alert('Operation failed');
    }
  };

  const handleEdit = (room) => {
    setFormData({
      view: room.view,
      beds: room.beds,
      maxGuests: room.maxGuests,
      sizeM2: room.sizeM2,
      type: room.type,
      bath: room.bath,
      terrace: room.terrace,
      cost: room.cost,
      availableRooms: room.availableRooms,
      bookingCount: room.bookingCount
    });
    setEditingId(room.id);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this room?')) return;
    
    try {
      await api.delete(`/room/${id}`);
      fetchRooms();
    } catch (error) {
      console.error('Error deleting room:', error);
      alert('Failed to delete room');
    }
  };

  const resetForm = () => {
    setFormData({
      view: '',
      beds: 1,
      maxGuests: 1,
      sizeM2: 20,
      type: 'Standard',
      bath: false,
      terrace: false,
      cost: 100,
      availableRooms: 1,
      bookingCount: 0
    });
    setEditingId(null);
  };

  return (
    <div>
      <button onClick={onBack} className="btn-back">
        ← Back to Hotels
      </button>
      
      <h2>Managing Rooms for: {hotelData?.description}</h2>
      <p>Address: {hotelData?.address}</p>

      <div className="form-section">
        <h3>{editingId ? 'Edit Room' : 'Add New Room'}</h3>
        <form onSubmit={handleSubmit}>
          <div>
            <label>View:</label>
            <input
              type="text"
              name="view"
              value={formData.view}
              onChange={handleInputChange}
              required
              placeholder="Sea view, City view, etc."
            />
          </div>

          <div className="form-row">
            <div>
              <label>Beds:</label>
              <input
                type="number"
                name="beds"
                value={formData.beds}
                onChange={handleInputChange}
                min="1"
                required
              />
            </div>

            <div>
              <label>Max Guests:</label>
              <input
                type="number"
                name="maxGuests"
                value={formData.maxGuests}
                onChange={handleInputChange}
                min="1"
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div>
              <label>Size (m²):</label>
              <input
                type="number"
                name="sizeM2"
                value={formData.sizeM2}
                onChange={handleInputChange}
                min="10"
                step="0.5"
                required
              />
            </div>

            <div>
              <label>Type:</label>
              <select
                name="type"
                value={formData.type}
                onChange={handleInputChange}
                required
              >
                {roomTypes.map(type => (
                  <option key={type} value={type}>{type}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-row">
            <div>
              <label>
                <input
                  type="checkbox"
                  name="bath"
                  checked={formData.bath}
                  onChange={handleInputChange}
                />
                Private Bath
              </label>
            </div>

            <div>
              <label>
                <input
                  type="checkbox"
                  name="terrace"
                  checked={formData.terrace}
                  onChange={handleInputChange}
                />
                Terrace/Balcony
              </label>
            </div>
          </div>

          <div className="form-row">
            <div>
              <label>Cost per night:</label>
              <input
                type="number"
                name="cost"
                value={formData.cost}
                onChange={handleInputChange}
                min="0"
                step="0.01"
                required
              />
            </div>

            <div>
              <label>Available Rooms:</label>
              <input
                type="number"
                name="availableRooms"
                value={formData.availableRooms}
                onChange={handleInputChange}
                min="0"
                required
              />
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn-primary">
              {editingId ? 'Update Room' : 'Add Room'}
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
        <h3>Rooms List</h3>
        <table className="data-table">
          <thead>
            <tr>
              <th>Type</th>
              <th>View</th>
              <th>Beds</th>
              <th>Guests</th>
              <th>Size</th>
              <th>Cost</th>
              <th>Available</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {rooms.map(room => (
              <tr key={room.id}>
                <td>{room.type}</td>
                <td>{room.view}</td>
                <td>{room.beds}</td>
                <td>{room.maxGuests}</td>
                <td>{room.sizeM2} m²</td>
                <td>${room.cost.toFixed(2)}</td>
                <td>{room.availableRooms}</td>
                <td className="actions">
                  <button 
                    onClick={() => handleEdit(room)}
                    className="btn-edit"
                  >
                    Edit
                  </button>
                  <button 
                    onClick={() => handleDelete(room.id)}
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
    </div>
  );
};

export default RoomManagement;