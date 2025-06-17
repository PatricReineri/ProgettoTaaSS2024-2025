import React, { useState } from 'react';
import Input from '../components/inputs/Input';
import Button from '../components/buttons/Button';

function RegisterPage() {
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

		const params = new URLSearchParams();
		for (const key in formData) {
			params.append(key, formData[key]);
		}
		setError('');
		setSuccessMsg('');

		const password = formData.password;
		if (password.length < 6) {
			setError('Password must contain at least 6 characters');
			return;
		}
		try {
			const res = await fetch('https://localhost:8443/login/register', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: params.toString(),
			});

			if (!res.ok) throw new Error('Registration invalid');
			const data = await res;
			console.log('Success:', data);
		} catch (err) {
			console.error('Error:', err.message);
			setError(err.message);
		}
	};

	return (
		<div className=" backgroundLogin  h-full sh-[calc(100vh-3.5rem)] p-4 ">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-4 h-fit max-h-full max-w-[30rem] flex flex-col rounded-md shadow-2xl space-y-4 ">
				<h2 className=" font-bold text-2xl">Create your account</h2>
				<form onSubmit={handleSubmit} className="flex  flex-row flex-wrap space-y-2 gap-2 p-2  rounded-md   ">
					<Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
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

const styles = {
	container: {
		minHeight: '100vh',
		background: 'linear-gradient(to right, #6a11cb, #2575fc)',
		display: 'flex',
		justifyContent: 'center',
		alignItems: 'center',
		padding: '30px',
	},
	card: {
		background: 'white',
		borderRadius: '10px',
		padding: '40px 30px',
		boxShadow: '0 8px 20px rgba(0,0,0,0.1)',
		width: '100%',
		maxWidth: '400px',
	},
	title: {
		textAlign: 'center',
		marginBottom: '30px',
		fontWeight: 'bold',
		color: '#333',
	},
	form: {
		display: 'flex',
		flexDirection: 'column',
	},
	fieldGroup: {
		marginBottom: '15px',
	},
	label: {
		marginBottom: '5px',
		fontWeight: 'bold',
		fontSize: '14px',
		color: '#444',
	},
	input: {
		width: '100%',
		padding: '10px',
		borderRadius: '6px',
		border: '1px solid #ccc',
		fontSize: '14px',
	},
	button: {
		padding: '12px',
		backgroundColor: '#28a745',
		color: 'white',
		border: 'none',
		fontWeight: 'bold',
		borderRadius: '6px',
		cursor: 'pointer',
		marginTop: '10px',
		transition: 'background-color 0.3s',
	},
};

export default RegisterPage;
