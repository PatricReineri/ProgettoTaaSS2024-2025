import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import { useState } from 'react';

function UserEditPage({ setLogged }) {
	const navigate = useNavigate();
	const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('user')));

	const [message, setMessage] = useState(null);
	const [error, setError] = useState(null);

	const handleChange = (e) => {
		const { name, value } = e.target;
		setUser((prev) => ({ ...prev, [name]: value }));
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		setError(null);
		setMessage(null);
		setUser((prev) => ({ ...prev, ['profileImageUrl']: '' }));
		try {
			const res = await fetch('https://localhost:8443/login/modifyuser', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify(user).replace('null', '""'),
			});
			if (!res.ok) throw new Error('error for user modify operation');
			setMessage('Change successful');
			sessionStorage.setItem('user', JSON.stringify(user));
			navigate('/modifyuser');
			setLogged(false);
			setTimeout(() => {
				setLogged(true);
			}, 100);
		} catch (err) {
			setError(err.message);
		}
	};

	return (
		<div className="max-w-md mx-auto mt-12 p-6 bg-white shadow-xl rounded-2xl">
			<h2 className="text-2xl font-bold mb-4 text-center">Modifica Profilo</h2>
			<form onSubmit={handleSubmit} className="space-y-4">
				<div>
					<label className="block text-sm font-medium mb-1">Username</label>
					<input
						type="text"
						name="username"
						value={user.username}
						onChange={handleChange}
						className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
						required
					/>
				</div>
				<div>
					<label className="block text-sm font-medium mb-1">Email</label>
					<input
						type="email"
						name="email"
						value={user.email}
						onChange={handleChange}
						className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
						required
					/>
				</div>
				<div>
					<label className="block text-sm font-medium mb-1">Name</label>
					<input
						type="text"
						name="name"
						value={user.name}
						onChange={handleChange}
						className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
					/>
				</div>
				<div>
					<label className="block text-sm font-medium mb-1">Surname</label>
					<input
						type="text"
						name="surname"
						value={user.surname}
						onChange={handleChange}
						className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
					/>
				</div>
				{message && <p className="text-green-600 text-sm">{message}</p>}
				{error && <p className="text-red-600 text-sm">{error}</p>}
				<button
					type="submit"
					className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg"
				>
					Save modify
				</button>
			</form>
		</div>
	);
}

export default UserEditPage;
