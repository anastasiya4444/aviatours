import React, { useEffect, useState } from 'react';

export default function User() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [users, setUsers] = useState([]);
    const [editUserId, setEditUserId] = useState(null); // Для редактирования пользователя

    const handleClick = (e) => {
        e.preventDefault();
        const user = { username, password };

        if (editUserId) {
            // Обновление пользователя
            fetch(`http://localhost:8080/user/update`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: editUserId, ...user })
            }).then(() => {
                console.log("User updated");
                resetForm();
            });
        } else {
            // Добавление нового пользователя
            fetch("http://localhost:8080/user/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user)
            }).then(() => {
                console.log("New User added");
                resetForm();
            });
        }

        fetchUsers(); // Обновляем список пользователей после операции
    };

    const fetchUsers = () => {
        fetch("http://localhost:8080/user/getAll")
            .then(res => res.json())
            .then((result) => {
                setUsers(result);
            });
    };

    const handleEdit = (user) => {
        setUsername(user.username);
        setPassword(user.password);
        setEditUserId(user.id); // Устанавливаем ID для редактирования
    };

    const handleDelete = (id) => {
        fetch(`http://localhost:8080/user/${id}`, {
            method: "DELETE",
        }).then(() => {
            console.log("User deleted");
            fetchUsers(); // Обновляем список пользователей после удаления
        });
    };

    const resetForm = () => {
        setUsername('');
        setPassword('');
        setEditUserId(null); // Сбрасываем ID редактируемого пользователя
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return (
        <div style={{ padding: '20px', maxWidth: '600px', margin: '20px auto', border: '1px solid #ccc', borderRadius: '5px' }}>
            <h1 style={{ color: 'blue', textDecoration: 'underline' }}>
                {editUserId ? 'Edit User' : 'Add User'}
            </h1>

            <form onSubmit={handleClick}>
                <div style={{ margin: '10px 0' }}>
                    <label>
                        Username:
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                            required
                        />
                    </label>
                </div>
                <div style={{ margin: '10px 0' }}>
                    <label>
                        Password:
                        <input
                            type="text"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                            required
                        />
                    </label>
                </div>
                <button type="submit" style={{ backgroundColor: 'red', color: 'white', padding: '10px 15px', border: 'none', borderRadius: '5px' }}>
                    {editUserId ? 'Update' : 'Submit'}
                </button>
            </form>

            <h1>Users</h1>
            <div style={{ marginTop: '20px' }}>
                {users.map(user => (
                    <div key={user.id} style={{ margin: '10px', padding: '15px', border: '1px solid #ccc', borderRadius: '5px' }}>
                        <strong>Id:</strong> {user.id}<br />
                        <strong>Username:</strong> {user.username}<br />
                        <strong>Password:</strong> {user.password}<br />
                        <button onClick={() => handleEdit(user)} style={{ marginRight: '10px' }}>
                            Edit
                        </button>
                        <button onClick={() => handleDelete(user.id)} style={{ backgroundColor: 'red', color: 'white', border: 'none', borderRadius: '5px' }}>
                            Delete
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}