import { useAuth } from '../../auth/AuthContext';
import { useNavigate } from 'react-router-dom';

function DeleteUserButton({ user, setLogged }) {
	const navigate = useNavigate();

	const handleDeleteUser = async (e) => {
		e.preventDefault();

		try {
			console.log(user.email);
			if (!user.email) {
				return;
			}

			const res = await fetch('https://localhost:8443/login/deleteuser', {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: new URLSearchParams({
					email: JSON.parse(sessionStorage.getItem('user')).email,
				}).toString(),
			});

			if (!res.ok) throw new Error('Delete user failed');
			console.log('Success:', res);
			sessionStorage.removeItem('user');
			setLogged(false);
			navigate('/');
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return (
		<button
			onClick={handleDeleteUser}
			style={{
				padding: '10px 20px',
				backgroundColor: '#FF0000',
				color: 'white',
				border: 'none',
				borderRadius: '4px',
				cursor: 'pointer',
			}}
		>
			Delete Account
		</button>
	);
}

export default DeleteUserButton;
