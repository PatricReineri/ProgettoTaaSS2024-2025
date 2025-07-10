import { useState } from 'react';
import Input from '../../components/inputs/Input';
import Button from '../../components/buttons/Button';
import clsx from 'clsx';

function ChangePasswordForm() {
	const [newPassword, setNewPassword] = useState('');
	const [confirmPassword, setConfirmPassword] = useState('');
	const [error, setError] = useState('');
	const [successMsg, setSuccessMsg] = useState('');
	const [loading, setLoading] = useState(false);

	const handleSubmit = async (e) => {
		e.preventDefault();
		const urlParams = new URLSearchParams(window.location.search);
		const token = urlParams.get('token');
		setError('');
		setSuccessMsg('');

		if (newPassword.length < 6) {
			setError('La password deve contenere almeno 6 caratteri');
			return;
		}

		if (newPassword !== confirmPassword) {
			setError('Le password non corrispondono');
			return;
		}

		setLoading(true);
		try {
			const res = await fetch(
				`https://localhost:8443/login/changepassword?token=${encodeURIComponent(
					token
				)}&new_password=${encodeURIComponent(newPassword)}`,
				{ method: 'PUT' }
			);

			const message = await res.text();
			if (!res.ok) throw new Error('Error while changing password');
			if (message === 'Error') throw new Error('Internal error');
			setSuccessMsg('La password è stata modificata con successo');
			setNewPassword('');
			setConfirmPassword('');
		} catch (err) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className="backgroundLogin min-h-full flex items-center justify-center p-4">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-6 sm:p-8 w-full max-w-md mx-auto flex flex-col rounded-xl shadow-2xl space-y-4">
				<h2 className="font-bold text-xl sm:text-2xl text-center">Change password</h2>
				
				<form onSubmit={handleSubmit} className="space-y-4">
					<Input
						customClassContainer="w-full"
						label="Nuova password"
						type="password"
						name="password"
						minLength={6}
						value={newPassword}
						onChange={(e) => setNewPassword(e.target.value)}
						required
					/>
					<Input
						customClassContainer="w-full"
						label="Conferma password"
						type="password"
						name="password"
						minLength={6}
						value={confirmPassword}
						onChange={(e) => setConfirmPassword(e.target.value)}
						required
						/>
					
					{error && <p className="text-red-400 text-sm text-center">{error}</p>}
					{successMsg && <p className="text-green-400 text-sm text-center">{successMsg}</p>}

					<Button
						custom={clsx({ 'w-full mt-6': true, 'opacity-50 cursor-not-allowed': loading })}
						disabled={loading}
						text={loading ? 'Processing...' : 'Cambia Password'}
					/>
				</form>
			</div>
		</div>
	);
}

export default ChangePasswordForm;
