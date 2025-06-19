import { useState } from 'react';
import { useAuth } from '../../auth/AuthContext';
import { useNavigate } from 'react-router-dom';

function LogoutButton() {
	const navigate = useNavigate();
	const { user, setUser } = useAuth();

	const handleLogout = async (e) => {
		e.preventDefault();

		try {
			const res = await fetch('https://localhost:8443/login/logoutuser', {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: new URLSearchParams({
					email: user.email,
				}).toString(),
			});

			if (!res.ok) throw new Error('logout failed');
			console.log('Success:', res);
			setUser(null);
			navigate('/');
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return (
		<button
			onClick={handleLogout}
			style={{
				padding: '10px 20px',
				backgroundColor: '#FF0000',
				color: 'white',
				border: 'none',
				borderRadius: '4px',
				cursor: 'pointer',
			}}
		>
			Logout
		</button>
	);
}

export default LogoutButton;
