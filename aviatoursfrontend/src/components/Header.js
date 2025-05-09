import { Link } from 'react-router-dom';

const Header = () => {
    return (
        <header className="header">
            <div className="container">
                <div className="logo">
                    <Link to="/">AVIATOURS</Link>
                </div>
                <nav className="main-nav">
                    <ul>
                        <li><Link to="/tours">Туры</Link></li>
                        <li><Link to="/flights">Авиабилеты</Link></li>
                        <li><Link to="/reviews">Отзывы</Link></li>
                        <li><Link to="/account">Личный кабинет</Link></li>
                        <li><Link to="compare-tours">Сравнить туры       </Link></li>
                    </ul>
                </nav>
                <div className="auth-buttons">
                    <Link to="/login" className="btn btn-outline">Войти</Link>
                    <Link to="/register" className="btn btn-primary">Регистрация</Link>
                </div>
            </div>
        </header>
    );
};

export default Header;