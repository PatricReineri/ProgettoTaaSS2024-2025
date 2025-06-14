import { useState } from 'react';

function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState(null);  
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
    setLoading(true);

    try {
        const res = await fetch('https://localhost:8443/login/generateresetpasswordlink', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                email: email
            }).toString(),
        });
        const message = await res.text();
        if (!res.ok) throw new Error('email not found');
        if(message === "Error") throw new Error('internat error');
        setMessage('Open your email to reset your password');
    } catch (error) {
        setMessage(`Error: ${error.message}`);
    } finally {
        setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '60px auto', fontFamily: 'Arial' }}>
      <h2>Forgot password?</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
        <input
          type="email"
          placeholder="Enter the email associated with the account"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={{ padding: 10, borderRadius: 4, border: '1px solid #ccc' }}
          disabled={loading}
        />
        <button
          type="submit"
          disabled={loading}
          style={{ padding: 10, backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: 4 }}
        >
          {loading ? 'Processing...' : 'Send'}
        </button>
      </form>
      {message && (
        <p style={{ marginTop: 15, color: message.startsWith('Error') ? 'red' : 'green' }}>
          {message}
        </p>
      )}
    </div>
  );
}

export default ForgotPassword;
