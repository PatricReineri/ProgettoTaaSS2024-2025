import React, { useState } from 'react';
import Input from '../../components/inputs/Input';
import Button from '../../components/buttons/Button';
import { login, register } from '../../api/authentication';
import { useNavigate, Link } from 'react-router-dom';

function RegisterPage({ setLogged }) {
	const navigate = useNavigate();
	const [error, setError] = useState('');
	const [successMsg, setSuccessMsg] = useState('');
	const [formData, setFormData] = useState({
		name: '',
		surname: '',
		username: '',
		email: '',
		password: '',
	});

	const handleChange = (e) => {
		setFormData((prev) => ({
			...prev,
			[e.target.name]: e.target.value,
		}));
	};

	const handleSubmit = async (e) => {
		e.preventDefault();

		setError('');
		setSuccessMsg('');

		const password = formData.password;
		if (password.length < 6) {
			setError('Password must contain at least 6 characters');
			return;
		}
		try {
			const res = await register(formData);

			if (!res.ok) throw new Error('Registration invalid');
			const data = await res;
			console.log('Success:', data);
			const loginRes = await login({
				email: formData.email,
				password: formData.password,
			});
			const dataLogin = await loginRes.json();
			console.log('Success:', data);
			sessionStorage.setItem('user', JSON.stringify(dataLogin));
			setLogged(true);
			navigate('/home');
		} catch (err) {
			console.error('Error:', err.message);
			setError(err.message);
		}
	};

	return (
		<div className="backgroundLogin min-h-full flex items-center justify-center p-4">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-6 sm:p-8 w-full max-w-lg mx-auto flex flex-col rounded-xl shadow-2xl space-y-4">
				<h2 className="font-bold text-xl sm:text-2xl text-center">Create your account</h2>
				
				<form onSubmit={handleSubmit} className="space-y-4">
					<div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
						<Input 
							customClassContainer="w-full"
							label="Name" 
							name="name" 
							value={formData.name} 
							onChange={handleChange} 
							required 
						/>
						<Input
							customClassContainer="w-full"
							label="Surname"
							name="surname"
							value={formData.surname}
							onChange={handleChange}
							required
						/>
					</div>
					
					<Input
						customClassContainer="w-full"
						label="Username"
						name="username"
						value={formData.username}
						onChange={handleChange}
						required
					/>
					<Input
						customClassContainer="w-full"
						label="Email"
						type="email"
						name="email"
						value={formData.email}
						onChange={handleChange}
						required
					/>
					<Input
						customClassContainer="w-full"
						label="Password"
						type="password"
						name="password"
						value={formData.password}
						onChange={handleChange}
						required
					/>

					{error && <p className="text-red-400 text-sm text-center">{error}</p>}
					{successMsg && <p className="text-green-400 text-sm text-center">{successMsg}</p>}

					<Button custom="w-full mt-6" text="Register" />
				</form>
				
				<div className="flex flex-col sm:flex-row justify-center items-center gap-2 pt-4 border-t border-gray-600">
					<p className="text-sm">Hai già un account?</p>
					<Link to="/login">
						<p className="text-[#EE0E51] hover:underline text-sm font-medium">Accedi ora</p>
					</Link>
				</div>
			</div>
		</div>
	);
}

export default RegisterPage;
