import { useState } from 'react';
import Button from './Button';
import { forgotPasswordRequest } from '../../api/authentication';

function ForgotPassword() {
	const [email, setEmail] = useState('');
	const [message, setMessage] = useState(null);
	const [loading, setLoading] = useState(false);

	const handleSubmit = async (e) => {
		e.preventDefault();
		setMessage(null);
		setLoading(true);
		try {
			const res = await forgotPasswordRequest(email);
			const message = await res.text();
			if (!res.ok) throw new Error('email not found');
			if (message === 'Error') throw new Error('internal error');
			setMessage('Apri la tua email per reimpostare la password');
		} catch (error) {
			setMessage(`Error: ${error.message}`);
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className="w-full">
			<h2>Password dimenticata?</h2>
			<form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
				<input
					type="email"
					placeholder="Inserisci l'email associata all'account"
					value={email}
					onChange={(e) => setEmail(e.target.value)}
					required
					style={{ padding: 10, borderRadius: 4, border: '1px solid #ccc' }}
					disabled={loading}
				/>
				<Button text={loading ? 'Caricamento...' : 'Invia'}></Button>
			</form>
			{message && <p style={{ marginTop: 15, color: message.startsWith('Error') ? 'red' : 'green' }}>{message}</p>}
		</div>
	);
}

export default ForgotPassword;
