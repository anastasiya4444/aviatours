import { useAuth } from './AuthContext';
import { Navigate } from 'react-router-dom';

const AdminRoute = ({ children }) => {
    const { user, isAuthenticated } = useAuth();
    
    if (!isAuthenticated || user?.role !== 'ADMIN') {
        return <Navigate to="/" replace />;
    }
    
    return children;
};

export default AdminRoute;