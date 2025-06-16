import { useState } from 'react';

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
        `https://localhost:8443/login/changepassword?token=${encodeURIComponent(token)}&new_password=${encodeURIComponent(newPassword)}`, 
        { method: 'PUT' }
      );

      const message = await res.text();
      if (!res.ok) throw new Error('Error while changing password');
      if(message === "Error") throw new Error('Internal error');

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
    <form onSubmit={handleSubmit} style={{ maxWidth: 400, margin: 'auto' }}>
      <h2>Cambia Password</h2>

      <div style={{ marginBottom: 12 }}>
        <label htmlFor="newPassword">Nuova Password:</label><br />
        <input
          type="password"
          id="newPassword"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          required
          minLength={6}
          style={{ width: '100%', padding: 8, boxSizing: 'border-box' }}
        />
      </div>

      <div style={{ marginBottom: 12 }}>
        <label htmlFor="confirmPassword">Conferma Password:</label><br />
        <input
          type="password"
          id="confirmPassword"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
          minLength={6}
          style={{ width: '100%', padding: 8, boxSizing: 'border-box' }}
        />
      </div>

      {error && <p style={{ color: 'red' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}

      <button type="submit" disabled={loading} style={{ padding: '8px 16px' }}>
        {loading ? 'Processing...' : 'Change Password'}
      </button>
    </form>
  );
}

export default ChangePasswordForm;
