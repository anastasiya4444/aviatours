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
  const [selectedImage, setSelectedImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);

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

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setSelectedImage(file);
      setImagePreview(URL.createObjectURL(file));
    }
  };

  const uploadImage = async (hotelId) => {
    if (!selectedImage) return null;

    const formData = new FormData();
    formData.append('file', selectedImage);

    try {
      const response = await api.post(
        `/hotel/${hotelId}/uploadImage`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      );
      return response.data;
    } catch (error) {
      console.error('Error uploading image:', error);
      alert('Failed to upload image');
      return null;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (editingId) {
        await api.put('/hotel/update', { ...formData, id: editingId });
        if (selectedImage) {
          await uploadImage(editingId);
        }
      } else {
        const response = await api.post('/hotel/add', formData);
        if (selectedImage) {
          await uploadImage(response.data.id);
        }
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
    setImagePreview(hotel.imageUrls ? `http://localhost:8080/hotel/images/${hotel.imageUrls}` : null);
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
    setSelectedImage(null);
    setImagePreview(null);
  };

  return (
    <div className="page-container">
      <h1>Hotel Management</h1>

      {!showRooms ? (
        <>
          <div className="form-section">
            <h2>{editingId ? 'Edit Hotel' : 'Add New Hotel'}</h2>
            <form onSubmit={handleSubmit}>
            </form>
          </div>

          <div className="table-section">
            <h2>All Hotels</h2>
            <table className="data-table">
              <thead>
                <tr>
                  <th>Image</th>
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
                    <td>
                      {hotel.imageUrls ? (
                        <div style={{
                          width: '80px',
                          height: '60px',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center'
                        }}>
                          <img
                            src={`http://localhost:8080/hotel/images/${hotel.imageUrls}`}
                            alt="Hotel"
                            style={{
                              maxWidth: '100%',
                              maxHeight: '100%',
                              objectFit: 'cover',
                              borderRadius: '4px',
                              boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                            }}
                            onError={(e) => {
                              e.target.onerror = null; 
                              e.target.parentElement.innerHTML = 'No Image';
                            }}
                          />
                        </div>
                      ) : (
                        <div style={{
                          width: '80px',
                          height: '60px',
                          backgroundColor: '#f5f5f5',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                          borderRadius: '4px',
                          color: '#999'
                        }}>
                          No Image
                        </div>
                      )}
                    </td>
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