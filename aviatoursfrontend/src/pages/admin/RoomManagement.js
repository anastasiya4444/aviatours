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
  const [selectedImage, setSelectedImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);

  const roomTypes = ['Standard', 'Deluxe', 'Suite', 'Family', 'Executive'];

  useEffect(() => {
    fetchRooms();
  }, [hotelId]);

  const fetchRooms = async () => {
    try {
      const response = await api.get(`/room/getByHotel/${hotelId}`);
      setRooms(response.data);
      console.log(response.data)
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

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setSelectedImage(file);
      setImagePreview(URL.createObjectURL(file));
    }
  };

  const uploadImage = async (roomId) => {
    if (!selectedImage) return null;

    const formData = new FormData();
    formData.append('file', selectedImage);

    try {
      const response = await api.post(
        `/room/${roomId}/uploadImage`,
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
        if (selectedImage) {
          await uploadImage(editingId);
        }
      } else {
        const response = await api.post('/room/add', payload);
        if (selectedImage) {
          await uploadImage(response.data.id);
        }
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
    setImagePreview(room.imageUrls ? `http://localhost:8080/room/images/${room.imageUrls}` : null);
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
    setSelectedImage(null);
    setImagePreview(null);
  };

  return (
    <div>
      <button onClick={onBack} className="btn-back">
        ← Вернуться к управлению отелями
      </button>

      <h2>Управление комнатами для: {hotelData?.description}</h2>
      <p>Адрес: {hotelData?.address}</p>

      <div className="form-section">
        <h3>{editingId ? 'Изменить' : 'Добавить'}</h3>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Вид:</label>
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
              <label>Кровати:</label>
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
              <label>Максимальное количество гостей:</label>
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
              <label>Размер (м²):</label>
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
              <label>Тип:</label>
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
                Личная ванная
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
                Терасса/Балкон
              </label>
            </div>
          </div>

          <div className="form-row">
            <div>
              <label>Стоимость за ночь:</label>
              <input
                type="number"
                name="cost"
                value={formData.cost}
                onChange={handleInputChange}
                min="0"
                step="1"
                required
              />
            </div>

            <div>
              <label>Доступные комнаты:</label>
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

          <div>
            <label>Фото комнат:</label>
            <input
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
            {imagePreview && (
              <div style={{ marginTop: '10px' }}>
                <img
                  src={imagePreview}
                  alt="Preview"
                  style={{
                    maxWidth: '150px',
                    maxHeight: '150px',
                    borderRadius: '4px',
                    border: '1px solid #ddd',
                  }}
                />
              </div>
            )}
          </div>

          <div className="form-actions">
            <button type="submit" className="btn-primary">
              {editingId ? 'Update Room' : 'Add Room'}
            </button>
            {editingId && (
              <button type="button" onClick={resetForm} className="btn-secondary">
                Отмена
              </button>
            )}
          </div>
        </form>
      </div>

      <div className="table-section">
        <h3>Список комнат</h3>
        <table className="data-table">
          <thead>
            <tr>
              <th>Фото</th>
              <th>Тип</th>
              <th>Вид</th>
              <th>Кровати</th>
              <th>Макс. кол. человек</th>
              <th>Размер</th>
              <th>Стоимость</th>
              <th>Доступность</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {rooms.map(room => (
              <tr key={room.id}>
                <td>
                  {room.imageUrls ? (
                    <div style={{
                      width: '80px',
                      height: '60px',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center'
                    }}>
                      <img
                        src={`http://localhost:8080/room/images/${room.imageUrls}`} 
                        alt="Room"
                        style={{
                          maxWidth: '100%',
                          maxHeight: '100%',
                          objectFit: 'cover',
                          borderRadius: '4px',
                          boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                        }}
                        onError={(e) => {
                          e.target.src = 'https://via.placeholder.com/80x60?text=No+Image';
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
                      Нет фото
                    </div>
                  )}
                </td>
                <td>{room.type}</td>
                <td>{room.view}</td>
                <td>{room.beds}</td>
                <td>{room.maxGuests}</td>
                <td>{room.sizeM2} м²</td>
                <td>{room.cost.toFixed(2)} руб.</td>
                <td>{room.availableRooms}</td>
                <td className="actions">
                  <button
                    onClick={() => handleEdit(room)}
                    className="btn-edit"
                    style={{
                      padding: '5px 10px',
                      marginRight: '5px',
                      backgroundColor: '#4CAF50',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer'
                    }}
                  >
                    Обновить
                  </button>
                  <button
                    onClick={() => handleDelete(room.id)}
                    className="btn-delete"
                    style={{
                      padding: '5px 10px',
                      backgroundColor: '#f44336',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer'
                    }}
                  >
                    Удалить
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