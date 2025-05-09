import React, { useState, useEffect } from 'react';
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

const MyReviews = () => {
  const [reviews, setReviews] = useState([]);
  const [allReviews, setAllReviews] = useState([]);
  const [formData, setFormData] = useState({
    id: null,
    rating: 5,
    reviewText: ''
  });
  const [currentUser, setCurrentUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const [allRes, myRes] = await Promise.all([
        api.get('/review/getAll'),
        api.get('/review/my')
      ]);
      
      console.log('All reviews:', allRes.data);
      console.log('My reviews:', myRes.data);
      
      setAllReviews(allRes.data);
      setReviews(myRes.data);
      
      if (myRes.data.length > 0 && myRes.data[0].user) {
        setCurrentUser(myRes.data[0].user);
        console.log('Current user:', myRes.data[0].user);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load reviews');
      console.error('Fetch error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.reviewText || formData.reviewText.length < 10) {
      setError('Review text must be at least 10 characters');
      return;
    }

    setIsLoading(true);
    setError(null);
    try {
      if (formData.id) {
        console.log('Updating review with ID:', formData.id);
        await api.put(`/review/${formData.id}`, {
          rating: formData.rating,
          reviewText: formData.reviewText
        });
      } else {
        console.log('Creating new review');
        await api.post('/review', {
          rating: formData.rating,
          reviewText: formData.reviewText
        });
      }
      await fetchData();
      setFormData({
        id: null,
        rating: 5,
        reviewText: ''
      });
    } catch (err) {
      setError(err.response?.data?.message || 'Operation failed');
      console.error('Submit error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEdit = (review) => {
    console.log('Editing review ID:', review.id);
    setFormData({
      id: review.id,
      rating: review.rating,
      reviewText: review.reviewText
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (review) => {
    console.log('Attempting to delete review ID:', review.id);
    if (!review.id) {
      setError('Cannot delete review without ID');
      return;
    }
    
    if (!window.confirm('Are you sure you want to delete this review?')) return;
    
    setIsLoading(true);
    setError(null);
    try {
      await api.delete(`/review/${review.id}`);
      console.log('Successfully deleted review ID:', review.id);
      await fetchData();
    } catch (err) {
      setError(err.response?.data?.message || 'Delete failed');
      console.error('Delete error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const isCurrentUserAuthor = (review) => {
    return currentUser && review.user && currentUser.id === review.user.id;
  };

  const getReviewKey = (review, index) => {
    return review.id ? `review-${review.id}` : `review-temp-${index}`;
  };

  if (isLoading) return <div className="loading">Loading reviews...</div>;

  return (
    <div className="page-container">
      <h1>Отзывы</h1>
      
      {error && <div className="error">{error}</div>}

      <div className="form-section">
        <h2>{formData.id ? 'Редактировать' : 'Добавить'}</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Рейтинг (1-5):</label>
            <select
              name="rating"
              value={formData.rating}
              onChange={handleInputChange}
              required
              disabled={isLoading}
            >
              {[1, 2, 3, 4, 5].map(num => (
                <option key={num} value={num}>{num}</option>
              ))}
            </select>
          </div>

          <div>
            <label>Отзыв:</label>
            <textarea
              name="reviewText"
              value={formData.reviewText}
              onChange={handleInputChange}
              required
              minLength="10"
              disabled={isLoading}
            />
          </div>

          <div className="form-actions">
            <button type="submit" className="btn-primary" disabled={isLoading}>
              {formData.id ? 'Update' : 'Add'}
            </button>
            {formData.id && (
              <button 
                type="button" 
                onClick={() => setFormData({ id: null, rating: 5, reviewText: '' })}
                className="btn-secondary"
                disabled={isLoading}
              >
                Отменить
              </button>
            )}
          </div>
        </form>
      </div>

      <div className="reviews-section">
        <h2>Все отзывы</h2>
        {allReviews.length === 0 ? (
          <p>Пока нет отзывов</p>
        ) : (
          <div className="reviews-list">
            {allReviews.map((review, index) => (
              <div key={getReviewKey(review, index)} className="review-card">
                <div className="review-header">
                  <span className="review-id" style={{fontWeight: 'bold', marginRight: '10px'}}>
                    ID: {review.id ? review.id : 'null'}
                  </span>
                  <span className="review-rating">
                    {'★'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}
                  </span>
                  <span className="review-author">
                     {review.user?.username || 'Anonymous'}
                  </span>
                  <span className="review-date">
                    {review.createdAt ? new Date(review.createdAt).toLocaleDateString() : ''}
                  </span>
                </div>
                <div className="review-text">{review.reviewText}</div>
                
                {isCurrentUserAuthor(review) && (
                  <div className="review-actions">
                    <button 
                      onClick={() => handleEdit(review)}
                      className="btn-edit"
                      disabled={isLoading}
                    >
                      Редактировать
                    </button>
                    <button 
                      onClick={() => handleDelete(review)}
                      className="btn-delete"
                      disabled={isLoading}
                    >
                      Удалить
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MyReviews;