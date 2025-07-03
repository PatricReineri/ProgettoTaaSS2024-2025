import { useAuth } from '../../auth/AuthContext';
import { useNavigate } from 'react-router-dom';
import Button from './Button';

function LogoutButton({ setLogged }) {
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
					email: JSON.parse(sessionStorage.getItem('user')).email,
				}).toString(),
			});

			if (!res.ok) throw new Error('logout failed');
			console.log('Success:', res);
			setUser(null);
			sessionStorage.removeItem('user');
			setLogged(false);
			navigate('/');
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return <Button onClick={handleLogout} secondary text="Logout"></Button>;
}

export default LogoutButton;
