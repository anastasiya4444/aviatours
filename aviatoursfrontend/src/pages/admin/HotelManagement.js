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
    { value: 'RO', label: 'Только номер' },
    { value: 'BB', label: 'Номер с завтраком' },
    { value: 'HB', label: 'Полупансион' },
    { value: 'FB', label: 'Полный пансион' },
    { value: 'AI', label: 'Все включено' }
  ];

  useEffect(() => {
    fetchHotels();
  }, []);

  const fetchHotels = async () => {
    try {
      const response = await api.get('/hotel/getAll');
      setHotels(response.data);
    } catch (error) {
      console.error('Ошибка при загрузке отелей:', error);
      alert('Не удалось загрузить отели');
    }
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
      console.error('Ошибка при загрузке изображения:', error);
      alert('Не удалось загрузить изображение');
      return null;
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
      console.error('Ошибка:', error.response?.data || error.message);
      alert('Операция не удалась');
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
    if (!window.confirm('Вы уверены, что хотите удалить этот отель?')) return;
    
    try {
      await api.delete(`/hotel/${id}`);
      fetchHotels();
    } catch (error) {
      console.error('Ошибка при удалении отеля:', error);
      alert('Не удалось удалить отель');
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
      <h1>Управление отелями</h1>
      
      {!showRooms ? (
        <>
          <div className="form-section">
            <h2>{editingId ? 'Редактировать отель' : 'Добавить новый отель'}</h2>
            <form onSubmit={handleSubmit}>
              
              <div>
                <label>Описание:</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div>
                <label>Адрес:</label>
                <input
                  type="text"
                  name="address"
                  value={formData.address}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div>
                <label>Тип питания:</label>
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
                <label>Рейтинг:</label>
                <select
                    name="rating"
                    value={formData.rating}
                    onChange={handleInputChange}
                    required
                >
                    <option value="" disabled>Выберите рейтинг</option>
                    {[1, 2, 3, 4, 5].map((rating) => (
                        <option key={rating} value={rating}>
                            {rating}
                        </option>
                    ))}
                </select>
              </div>

              <div>
                <label>Особенности (через запятую):</label>
                <input
                  type="text"
                  name="hotelFeatures"
                  value={formData.hotelFeatures}
                  onChange={handleInputChange}
                  placeholder="WiFi, Бассейн, Спа и т.д."
                />
              </div>
              <div>
            <label>Картинка:</label>
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
                  {editingId ? 'Обновить отель' : 'Добавить отель'}
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
            <h2>Все отели</h2>
            <table className="data-table">
              <thead>
                <tr>
                  <th>Изображение</th>
                  <th>ID</th>
                  <th>Описание</th>
                  <th>Адрес</th>
                  <th>Тип питания</th>
                  <th>Рейтинг</th>
                  <th>Действия</th>
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
                             alt="Отель"
                             style={{
                               maxWidth: '100%',
                               maxHeight: '100%',
                               objectFit: 'cover',
                               borderRadius: '4px',
                               boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                             }}
                             onError={(e) => {
                               e.target.onerror = null; 
                               e.target.parentElement.innerHTML = 'Нет изображения';
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
                           Нет изображения
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
                        Редактировать
                      </button>
                      <button 
                        onClick={() => setShowRooms(hotel.id)} 
                        className="btn-view"
                      >
                        Номера
                      </button>
                      <button 
                        onClick={() => handleDelete(hotel.id)} 
                        className="btn-delete"
                      >
                        Удалить
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