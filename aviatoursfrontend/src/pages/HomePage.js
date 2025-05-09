import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import './MainPage.css'

const HomePage = () => {
    const [featuredTours, setFeaturedTours] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTours = async () => {
            try {
                const response = await axios.get('http://localhost:8080/tour/getAll');
                const tours = response.data.map(tour => ({
                    id: tour.id,
                    title: tour.tourName,
                    description: tour.description
                }));
                setFeaturedTours(tours.slice(0, 3)); 
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchTours();
    }, []);


    if (loading) {
        return <div className="loading">Загрузка туров...</div>;
    }

    if (error) {
        console.error('Ошибка при загрузке туров:', error);
    }

    return (
        <div className="home-page">
            <section className="hero">
                <div className="hero-content">
                    <h1>Путешествуйте с комфортом</h1>
                    <p>Откройте для себя лучшие направления по доступным ценам</p>
                    <Link to="/tours" className="btn btn-primary">Забронировать тур</Link>
                </div>
            </section>

            <section className="about-section">
                <div className="container">
                    <h2>О компании</h2>
                    <p>
                        Надёжная служба бронирования отелей и туров предоставляет широкий выбор жилья и туристических 
                        направлений по всему миру. Мы гарантируем качественное обслуживание, доступные цены, 
                        конкурентоспособные условия и профессиональную поддержку клиентов.
                    </p>
                </div>
            </section>

            {/* <section className="featured-tours">
                <div className="container">
                    <h2>Наши туры!</h2>
                    <div className="tours-grid">
                        {featuredTours.map(tour => (
                            <div key={tour.id} className="tour-card">
                                <div className="tour-image" style={{ backgroundImage: `url(${tour.image})` }}>
                                    <div className="price-badge">{tour.price}</div>
                                </div>
                                <div className="tour-content">
                                    <h3>{tour.title}</h3>
                                    <p className="description">{tour.description}</p>
                                    <Link to={`/tour/${tour.id}`} className="btn btn-outline">Подробнее</Link>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="view-more">
                        <Link to="/tours" className="btn btn-primary">Смотреть больше</Link>
                    </div>
                </div>
            </section>
            
            <section className="cta-section">
                <div className="container">
                    <h2>Готовы к незабываемому путешествию?</h2>
                    <Link to="/tours" className="btn btn-primary">Найти тур</Link>
                </div>
            </section> */}
        </div>
    );
};

export default HomePage;