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
			setError('Password must contain at least 6 characters');
			return;
		}

		if (newPassword !== confirmPassword) {
			setError('The passwords do not match');
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

			setSuccessMsg('Password changed successfully');
			setNewPassword('');
			setConfirmPassword('');
		} catch (err) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className=" backgroundLogin h-full sh-[calc(100vh-3.5rem)] p-4 ">
			<div className="relative bg-[#363540] text-[#E8F2FC] p-4 h-fit max-h-full max-w-[30rem] flex flex-col rounded-md shadow-2xl space-y-4 ">
				<h2 className=" font-bold text-2xl  ">Change password</h2>
				<form onSubmit={handleSubmit} className="flex  flex-row flex-wrap space-y-2 gap-2 p-2  rounded-md   ">
					<Input
						customClassContainer="flex-auto"
						label="New password"
						type="password"
						name="password"
						minLength={6}
						value={newPassword}
						onChange={(e) => setNewPassword(e.target.value)}
						required
					/>
					<Input
						customClassContainer="flex-auto"
						label="Confirm password"
						type="password"
						name="password"
						minLength={6}
						value={confirmPassword}
						onChange={(e) => setConfirmPassword(e.target.value)}
						required
					/>
					{error && <p style={{ color: 'red' }}>{error}</p>}
					{successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}

					<Button
						custom={clsx({ 'w-full active:animate-pulse': true, active: loading })}
						disabled={loading}
						text={loading ? 'Processing...' : 'Change Password'}
					/>
				</form>
			</div>
		</div>
	);
}

export default ChangePasswordForm;
