import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import XButton from '../components/buttons/XButton';
import GoogleButton from '../components/buttons/GoogleButton';
import { login } from '../api/api';

function LoginPage() {
	const [formData, setFormData] = useState({ email: '', password: '' });

	const handleChange = (e) => {
		setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
	};

	const handleLogin = async (e) => {
		e.preventDefault();

		try {
			const res = await login(formData);

			if (!res.ok) throw new Error('Credential invalid');
			const data = await res.json();
			console.log('Success:', data);
			alert(data);
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return (
		<div className="background" style={{ padding: '4rem' }}>
			<div
				style={{
					maxWidth: 400,
					padding: '2rem',
					borderRadius: '28px',
					fontFamily: 'Arial',
					backgroundColor: '#363540',
					color: '#E8F2FC',
				}}
			>
				<h2 style={{ textAlign: 'center' }}>Organizza il tuo evento</h2>
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
					<XButton text="login" type="submit" />
				</form>

				{/* Separator */}
				<div style={{ textAlign: 'center', margin: '0.5rem', color: '#666' }}>or</div>

				{/*Login with Google*/}
				<GoogleButton />

				{/*Registration*/}
				<div
					style={{
						textAlign: 'center',
						marginTop: '20px',
						display: 'flex',
						justifyContent: 'center',
						gap: '4px',
					}}
				>
					<p>Are you not registered?</p>
					<Link to="/register">
						<p>Register now</p>
					</Link>
				</div>
			</div>
		</div>
	);
}

export default LoginPage;
