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
      
      setAllReviews(allRes.data.filter(review => review.id !== null)); 
      setReviews(myRes.data.filter(review => review.id !== null));
      
      if (myRes.data.length > 0 && myRes.data[0].user) {
        setCurrentUser(myRes.data[0].user);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load data');
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
    try {
      if (formData.id) {
        await api.put(`/review/${formData.id}`, {
          rating: formData.rating,
          reviewText: formData.reviewText
        });
      } else {
        await api.post('/review', {
          rating: formData.rating,
          reviewText: formData.reviewText
        });
      }
      await fetchData();
      resetForm();
    } catch (err) {
      setError(err.response?.data?.message || 'Operation failed');
      console.error('Submit error:', err);
    }
  };

  const handleEdit = (review) => {
    if (!review.id) return;
    setFormData({
      id: review.id,
      rating: review.rating,
      reviewText: review.reviewText
    });
  };

  const handleDelete = async (id) => {
    if (!id || !window.confirm('Are you sure you want to delete this review?')) return;
    try {
      await api.delete(`/review/${id}`);
      await fetchData();
    } catch (err) {
      setError(err.response?.data?.message || 'Delete failed');
      console.error('Delete error:', err);
    }
  };

  const resetForm = () => {
    setFormData({
      id: null,
      rating: 5,
      reviewText: ''
    });
  };

  const canEditReview = (review) => {
    if (!review.id || !currentUser) return false;
    return currentUser.id === review.user?.id || currentUser.role?.name === 'ADMIN';
  };

  if (isLoading) return <div>Loading reviews...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="page-container">
      <h1>Reviews {currentUser && `(Hello, ${currentUser.username})`}</h1>
      
      <div className="form-section">
        <h2>{formData.id ? 'Edit Review' : 'Add New Review'}</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Rating (1-5):</label>
            <select
              name="rating"
              value={formData.rating}
              onChange={handleInputChange}
              required
            >
              {[1, 2, 3, 4, 5].map(num => (
                <option key={num} value={num}>{num}</option>
              ))}
            </select>
          </div>

          <div>
            <label>Review Text:</label>
            <textarea
              name="reviewText"
              value={formData.reviewText}
              onChange={handleInputChange}
              required
              minLength="10"
            />
          </div>

          <div className="form-actions">
            <button type="submit" className="btn-primary">
              {formData.id ? 'Update' : 'Add'}
            </button>
            {formData.id && (
              <button type="button" onClick={resetForm} className="btn-secondary">
                Cancel
              </button>
            )}
          </div>
        </form>
      </div>

      <div className="table-section">
        <h2>All Reviews</h2>
        {allReviews.length === 0 ? (
          <p>No reviews yet</p>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Rating</th>
                <th>Review</th>
                <th>Author</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {allReviews.map(review => (
                <tr key={`review-${review.id}`}>
                  <td>{'★'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}</td>
                  <td>{review.reviewText}</td>
                  <td>{review.user?.username || 'Unknown'}</td>
                  <td>{review.createdAt ? new Date(review.createdAt).toLocaleDateString() : 'N/A'}</td>
                  <td className="actions">
                    {canEditReview(review) && (
                      <>
                        <button 
                          onClick={() => handleEdit(review)}
                          className="btn-edit"
                        >
                          Edit
                        </button>
                        <button 
                          onClick={() => handleDelete(review.id)}
                          className="btn-delete"
                        >
                          Delete
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default MyReviews;