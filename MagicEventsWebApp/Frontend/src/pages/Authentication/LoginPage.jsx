import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import GoogleButton from '../../components/buttons/GoogleButton';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import ForgotPassword from '../../components/buttons/ForgotPassword';
import Button from '../../components/buttons/Button';
import clsx from 'clsx';
import { login, helloServer } from '../../api/authentication';

function LoginPage({ setLogged }) {
	const navigate = useNavigate();
	const { setUser } = useAuth();

	const [formData, setFormData] = useState({ email: '', password: '' });
	const [forgotFlag, setForgotFlag] = useState(false);

	useEffect(() => {
		const user = JSON.parse(sessionStorage.getItem('user'));
		if (user) {
			navigate('/', { replace: true });
		}
	}, [navigate]);

	useEffect(() => {
		const protocol = window.location.protocol.replace(':', '');
		console.log('✅ Trying to contact server...');
		const detectClientProtocol = async () => {
			try {
				const res = await helloServer(protocol);
				if (!res.ok) console.warn('Protocol detection failed');
			} catch (err) {
				console.error('Error contacting server:', err);
			}
		};

		detectClientProtocol();
	}, []);

	const handleChange = (e) => {
		setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
	};

	const handleLogin = async (e) => {
		e.preventDefault();

		const res = await login(formData);
		if (!res.ok) throw new Error('Credential invalid');
		const data = await res.json();
		console.log('Success:', data);
		setUser(data);
		sessionStorage.setItem('user', JSON.stringify(data));
		setLogged(true);
		navigate('/home');
	};

	return (
		<div className="backgroundLogin min-h-full flex items-center justify-center p-4">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-6 sm:p-8 w-full max-w-md mx-auto flex flex-col rounded-xl shadow-2xl space-y-4 sm:space-y-6">
				<h2 className="font-bold text-xl sm:text-2xl text-center">Login</h2>
				
				{/* Login with email e password */}
				<form
					className={clsx({ hidden: forgotFlag })}
					onSubmit={handleLogin}
					style={{ display: 'flex', flexDirection: 'column', gap: 10 }}
				>
					<input
						type="text"
						name="email"
						placeholder="Email"
						value={formData.email}
						onChange={handleChange}
						required
						className="p-3 rounded-lg border border-gray-300 text-gray-800 text-sm sm:text-base"
					/>
					<input
						type="password"
						name="password"
						placeholder="Password"
						value={formData.password}
						onChange={handleChange}
						required
						className="p-3 rounded-lg border border-gray-300 text-gray-800 text-sm sm:text-base"
					/>
					<Button text="Login" />
				</form>
				
				<Button
					text="Password dimenticata?"
					link={true}
					onClick={() => {
						setForgotFlag(true);
					}}
					custom="w-full text-center text-sm"
				/>

				{/*Login with Google*/}
				<div className="flex justify-center">
					<GoogleButton></GoogleButton>
				</div>

				{/*Registration*/}
				<div className="flex flex-col sm:flex-row justify-center items-center gap-2 pt-4 border-t border-gray-600">
					<p className="text-sm">Non hai un account?</p>
					<Link to="/register">
						<p className="text-[#EE0E51] hover:underline text-sm font-medium">Registrati ora</p>
					</Link>
				</div>

				<div
					className={clsx({
						hidden: !forgotFlag,
						'bg-[#363540] p-6 sm:p-8 absolute inset-0 w-full h-full rounded-xl flex flex-col': true,
					})}
				>
					<button
						onClick={() => {
							setForgotFlag(false);
						}}
						className="self-end text-xl hover:bg-gray-600 w-8 h-8 rounded-full flex items-center justify-center mb-4"
					>
						×
					</button>
					<div className="flex-1 flex items-center justify-center">
						<ForgotPassword></ForgotPassword>
					</div>
				</div>
			</div>
		</div>
	);
}

export default LoginPage;
