import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';

function GoogleCallbackPage({ setLogged }) {
	const navigate = useNavigate();
	const { setUser } = useAuth();

	useEffect(() => {
		const handleCallback = async () => {
			const urlParams = new URLSearchParams(window.location.search);
			const accessToken = urlParams.get('accessToken');
			if (!accessToken) {
				console.error('Missing access token');
				return;
			}
			try {
				const res = await fetch(`https://localhost:8443/login/userprofile?accessToken=${accessToken}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded',
					},
				});

				if (!res.ok) throw new Error('User not found');
				const data = await res.json();
				console.log('Success:', data);
				setUser(data);
				sessionStorage.setItem('user', JSON.stringify(data));
				setLogged(true);
				navigate('/home');
			} catch (err) {
				console.error('Error:', err.message);
			}
		};

		handleCallback();
	}, [navigate, setUser]);

	return <p style={{ textAlign: 'center' }}>Processing...</p>;
}

export default GoogleCallbackPage;
