import { useAuth } from '../security/AuthContext';
import { Link } from 'react-router-dom';

function AccountPage() {
    const { user, loading, error } = useAuth();

    if (loading) return <div>Загрузка...</div>;
    if (error) return <div>Ошибка: {error}</div>;
    if (!user) return <div>Пожалуйста, войдите в систему</div>;

    return (
        <div>
            <h2>Профиль пользователя</h2>


            <nav className="admin-nav">
                <Link to="/admin/users" className="admin-nav-link">
                    Управление пользователями
                </Link>
                <Link to="/admin/bookings" className="admin-nav-link">
                    Управление бронированиями
                </Link>
                <Link to="/admin/hotels" className="admin-nav-link">
                    Управление отелями
                </Link>
                <Link to="/admin/tours" className="admin-nav-link">
                    Управление турами
                </Link>
                <Link to="/admin/flights" className="admin-nav-link">
                    Управление рейсами
                </Link>
            </nav>

        </div>
    );
}

export default AccountPage;