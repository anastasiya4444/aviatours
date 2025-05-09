import React, { useState, useEffect } from 'react';
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [roles, setRoles] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        role: { id: 2 }
    });

    useEffect(() => {
        fetchUsers();
        fetchRoles();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await api.get('/user/getAll');
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
            alert('Failed to load users');
        }
    };

    const fetchRoles = async () => {
        try {
            const response = await api.get('/role/getAll');
            setRoles(response.data);
        } catch (error) {
            console.error('Error fetching roles:', error);
            alert('Failed to load roles');
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleRoleChange = (e) => {
        const roleId = parseInt(e.target.value);
        setFormData(prev => ({
            ...prev,
            role: { id: roleId }
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const payload = {
                username: formData.username,
                password: formData.password,
                role: formData.role
            };

            if (editingId) {
                await api.put('/user/update', {
                    ...payload,
                    id: editingId
                });
            } else {
                await api.post('/user/add', payload);
            }

            fetchUsers();
            resetForm();
        } catch (error) {
            console.error('Error:', error.response?.data || error.message);
            alert('Operation failed: ' + (error.response?.data?.message || error.message));
        }
    };

    const handleEdit = (user) => {
        setFormData({
            username: user.username,
            password: '',
            role: user.role || { id: 2 }
        });
        setEditingId(user.id);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this user?')) return;

        try {
            await api.delete(`/user/${id}`);
            fetchUsers();
        } catch (error) {
            console.error('Error deleting user:', error.response?.data || error.message);
            alert('Failed to delete user');
        }
    };

    const resetForm = () => {
        setFormData({
            username: '',
            password: '',
            role: { id: 2 }
        });
        setEditingId(null);
    };

    const getRoleName = (role) => {
        if (!role) return 'Unknown';
        return role.name || `Role ${role.id}`;
    };

    return (
        <div className="page-container">
            <h1>Управление пользователями</h1>

            <div className="form-section">
                <h2>{editingId ? 'Редактировать пользователя' : 'Добавить пользователя'}</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Логин:</label>
                        <input
                            type="text"
                            name="username"
                            value={formData.username}
                            onChange={handleInputChange}
                            required
                            minLength={3}
                        />
                    </div>

                    <div>
                        <label>Пароль:</label>
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleInputChange}
                            required={!editingId}
                            minLength={6}
                            placeholder={editingId ? "Leave empty to keep current" : ""}
                        />
                    </div>

                    <div>
                        <label>Роль:</label>
                        <select
                            name="roleId"
                            value={formData.role?.id || ''}
                            onChange={handleRoleChange}
                            required
                        >
                            {roles.map(role => (
                                <option key={role.id} value={role.id}>
                                    {role.name || `Role ${role.id}`}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-actions">
                        <button type="submit" className="btn-primary">
                            {editingId ? 'Редактировать' : 'Добавить'}
                        </button>
                        {editingId && (
                            <button
                                type="button"
                                onClick={resetForm}
                                className="btn-secondary"
                            >
                                Отменить
                            </button>
                        )}
                    </div>
                </form>
            </div>

            <div className="table-section">
                <h2>Все пользователи</h2>
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Логин</th>
                            <th>Роль</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.username}</td>
                                <td>{getRoleName(user.role)}</td>
                                <td className="actions">
                                    <button
                                        onClick={() => handleEdit(user)}
                                        className="btn-edit"
                                    >
                                        Обновить
                                    </button>
                                    <button
                                        onClick={() => handleDelete(user.id)}
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
        </div>
    );
};

export default UserManagement;