import { Link } from 'react-router-dom';

const Footer = () => {
    return (
        <footer className="footer">
            <div className="container">
                <div className="footer-content">
                    <div className="footer-section">
                        <h4>Компания</h4>
                        <ul>
                            <li><Link to="/about">О нас</Link></li>
                            <li><Link to="/contacts">Контакты</Link></li>
                            <li><Link to="/reviews">Отзывы</Link></li>
                        </ul>
                    </div>
                    <div className="footer-section">
                        <h4>Помощь</h4>
                        <ul>
                            <li><Link to="/faq">FAQ</Link></li>
                            <li><Link to="/privacy">Политика конфиденциальности</Link></li>
                            <li><Link to="/terms">Условия использования</Link></li>
                        </ul>
                    </div>
                    <div className="footer-section">
                        <h4>Контакты</h4>
                        <p>Email: info@travelease.com</p>
                        <p>Телефон: +375 44 740 11 11</p>
                    </div>
                </div>
                <div className="footer-bottom">
                    <p>&copy; {new Date().getFullYear()} AVIATOURS. Все права защищены.</p>
                </div>
            </div>
        </footer>
    );
};

export default Footer;