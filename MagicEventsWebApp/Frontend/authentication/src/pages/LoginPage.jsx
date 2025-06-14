import { useState } from 'react';
import { Link } from 'react-router-dom';
import GoogleButton from '../components/buttons/GoogleButton';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import ForgotPassword from '../components/buttons/ForgotPassword';

function LoginPage() {
	const navigate = useNavigate();
	const { setUser } = useAuth();

	const [formData, setFormData] = useState({ email: '', password: '' });

	const handleChange = (e) => {
		setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
	};

	const handleLogin = async (e) => {
		e.preventDefault();

		const params = new URLSearchParams();
		for (const key in formData) {
			params.append(key, formData[key]);
		}

		try {
			const res = await fetch('https://localhost:8443/login/form', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: params.toString(),
			});

			if (!res.ok) throw new Error('Credential invalid');
			const data = await res.json();
			console.log('Success:', data);
			setUser(data);
			navigate("/userprofile")
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return (
		<div style={{ maxWidth: 400, margin: '60px auto', fontFamily: 'Arial' }}>
			<h2 style={{ textAlign: 'center' }}>Login</h2>
			{/* Login with email e password */}
			<form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
				<input
					type="text"
					name="email"
					placeholder="Email"
					value={formData.email}
					onChange={handleChange}
					required
					style={{ padding: 10, borderRadius: 4, border: '1px solid #ccc' }}
				/>
				<input
					type="password"
					name="password"
					placeholder="Password"
					value={formData.password}
					onChange={handleChange}
					required
					style={{ padding: 10, borderRadius: 4, border: '1px solid #ccc' }}
				/>
				<button
					type="submit"
					style={{ padding: 10, backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: 4 }}
				>
					Login
				</button>
			</form>

			{/* Separator */}
			<div style={{ textAlign: 'center', margin: '20px 0', color: '#666' }}>or</div>

			{/*Login with Google*/}
			<div style={{ display: 'flex', justifyContent: 'center' }}>		
				<GoogleButton></GoogleButton>
			</div>

			{/*Registration*/}
			<div style={{ textAlign: 'center', marginTop: '20px' }}>
				<p>Are you not registered?</p>
				<Link to="/register">
					<button
						style={{
							padding: '10px 20px',
							backgroundColor: '#28a745',
							color: 'white',
							border: 'none',
							borderRadius: '4px',
							cursor: 'pointer',
						}}
					>
						Register now
					</button>
				</Link>
			</div>
			<ForgotPassword></ForgotPassword>
		</div>
	);
}

export default LoginPage;
