import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import GoogleButton from '../components/buttons/GoogleButton';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import ForgotPassword from '../components/buttons/ForgotPassword';
import Button from '../components/buttons/Button';
import clsx from 'clsx';

function LoginPage() {
	const navigate = useNavigate();
	const { setUser } = useAuth();

	const [formData, setFormData] = useState({ email: '', password: '' });
	const [forgotFlag, setForgotFlag] = useState(false);

	useEffect(() => {
		const protocol = window.location.protocol.replace(':', '');
		console.log('✅ Trying to contact server...');
		const detectClientProtocol = async () => {
			try {
				const res = await fetch('https://localhost:8443/login/helloserver?protocol=' + protocol, {
					method: 'GET',
				});
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
			navigate('/userprofile');
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return (
		<div className=" backgroundLogin   h-full sh-[calc(100vh-3.5rem)] p-4 ">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-4 h-fit max-h-full max-w-[30rem] flex flex-col rounded-md shadow-2xl space-y-4 ">
				<h2 className=" font-bold text-2xl">Login</h2>
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
					<Button text="Login" />
				</form>
				<Button
					text="Password dimenticata?"
					link={true}
					onClick={() => {
						setForgotFlag(true);
					}}
					custom=" w-full text-end  "
				/>
				{/* Separator */}
				{/* <div className=" my-1 text-center ">or</div> */}

				{/*Login with Google*/}
				<div style={{ display: 'flex', justifyContent: 'center' }}>
					<GoogleButton></GoogleButton>
				</div>

				<div className=" h-full "></div>
				{/*Registration*/}
				<div className=" flex justify-center gap-4 pb-4   ">
					<p>Are you not registered?</p>
					<Link to="/register">
						<p className=" text-[#EE0E51] hover:underline ">Registrati ora</p>
					</Link>
				</div>

				<div
					className={clsx({
						hidden: !forgotFlag,
						' bg-[#363540] p-8 absolute top-0 left-0 w-full h-full rounded-md flex flex-col items-end ': true,
					})}
				>
					<button
						onClick={() => {
							setForgotFlag(false);
						}}
						className="hover:shadow-2xl"
					>
						X
					</button>
					<div className="flex-auto"></div>
					<ForgotPassword></ForgotPassword>
					<div className="flex-auto"></div>
				</div>
			</div>
		</div>
	);
}

export default LoginPage;
