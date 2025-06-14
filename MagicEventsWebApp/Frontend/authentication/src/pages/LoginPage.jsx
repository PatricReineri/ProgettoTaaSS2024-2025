import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Button from '../components/buttons/XButton';
import XButton from '../components/buttons/XButton';

function LoginPage() {
	const [formData, setFormData] = useState({ email: '', password: '' });

	const googleClientId = '856285122317-g1d4mlarfh9s00pubcj8uc1lij9h0peq.apps.googleusercontent.com';
	const redirectUri = 'https://localhost:8443/login/grantcode';
	const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=${encodeURIComponent(
		redirectUri
	)}&response_type=code&client_id=${googleClientId}&scope=${encodeURIComponent(
		'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid'
	)}&access_type=offline`;

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
					placeholder="email"
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
				<XButton></XButton>
			</form>

			{/* Separator */}
			<div style={{ textAlign: 'center', margin: '20px 0', color: '#666' }}>or</div>

			{/*Login with Google*/}
			<div style={{ display: 'flex', justifyContent: 'center' }}>
				<a href={googleAuthUrl} style={{ textDecoration: 'none' }}>
					<button
						style={{
							display: 'flex',
							alignItems: 'center',
							backgroundColor: 'white',
							color: '#3c4043',
							border: '1px solid #dadce0',
							boxShadow: '0 1px 2px rgba(0,0,0,0.1)',
							padding: '10px 24px',
							borderRadius: '4px',
							fontSize: '16px',
							fontWeight: '500',
							cursor: 'pointer',
							fontFamily: 'Roboto, sans-serif',
						}}
					>
						<svg style={{ marginRight: '12px' }} width="20" height="20" viewBox="0 0 48 48">
							<path
								fill="#EA4335"
								d="M24 9.5c3.54 0 6.7 1.22 9.2 3.22l6.86-6.86C35.68 1.7 30.13 0 24 0 14.78 0 6.73 5.82 2.74 14.29l8.2 6.38C13.03 13.32 18.06 9.5 24 9.5z"
							/>
							<path
								fill="#4285F4"
								d="M46.1 24.5c0-1.66-.14-3.28-.41-4.84H24v9.15h12.4c-.55 2.9-2.24 5.35-4.76 7.02l7.38 5.76C43.76 37.5 46.1 31.43 46.1 24.5z"
							/>
							<path
								fill="#FBBC05"
								d="M10.94 28.34A14.5 14.5 0 019.5 24c0-1.52.26-2.98.74-4.34l-8.2-6.38A23.95 23.95 0 000 24c0 3.86.92 7.5 2.54 10.71l8.4-6.37z"
							/>
							<path
								fill="#34A853"
								d="M24 48c6.13 0 11.68-2.03 15.67-5.52l-7.38-5.76c-2.07 1.38-4.72 2.18-8.3 2.18-5.94 0-10.97-3.82-12.97-9.17l-8.4 6.37C6.73 42.18 14.78 48 24 48z"
							/>
							<path fill="none" d="M0 0h48v48H0z" />
						</svg>
						Accedi con Google
					</button>
					<Button></Button>
				</a>
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
		</div>
	);
}

export default LoginPage;
