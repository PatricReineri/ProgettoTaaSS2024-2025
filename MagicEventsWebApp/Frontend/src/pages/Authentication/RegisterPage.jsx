import React, { useState } from 'react';
import Input from '../../components/inputs/Input';
import Button from '../../components/buttons/Button';
import { login, register } from '../../api/authentication';
import { useNavigate } from 'react-router-dom';

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
		<div className="backgroundLogin h-full sh-[calc(100vh-3.5rem)] p-4 flex items-center justify-center">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-4 h-fit max-h-full max-w-[30rem] flex flex-col rounded-md shadow-2xl space-y-4 ">
				<h2 className=" font-bold text-2xl">Create your account</h2>
				<form onSubmit={handleSubmit} className="flex flex-row flex-wrap space-y-2 gap-2 p-2  rounded-md">
					<Input 
						customClassContainer="flex-auto"
						label="Name" 
						name="name" 
						value={formData.name} 
						onChange={handleChange} 
						required 
					/>
					<Input
						customClassContainer="flex-auto"
						label="Surname"
						name="surname"
						value={formData.surname}
						onChange={handleChange}
						required
					/>
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

					{error && <p style={{ color: 'red' }}>{error}</p>}
					{successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}

					<Button custom="w-full mt-2" text="Register" />
				</form>
			</div>
		</div>
	);
}

export default RegisterPage;
