import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

function GoogleCallbackPage() {
  const navigate = useNavigate();
  const { setUser } = useAuth();

  useEffect(() => {
    const handleCallback = async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const accessToken = urlParams.get('accessToken');
      if (!accessToken) {
        console.error('Missing access token');
        return;
      }
      try {
        const res = await fetch('https://localhost:8443/login/userprofile', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: new URLSearchParams({ accessToken }).toString(),
        });

        if (!res.ok) throw new Error('User not found');
        const data = await res.json();
        console.log('Success:', data);
        setUser(data);
        navigate('/userprofile');
      } catch (err) {
        console.error('Error:', err.message);
      }
    };

    handleCallback();
  }, [navigate, setUser]);

  return <p style={{ textAlign: 'center' }}>Processing...</p>;
}

export default GoogleCallbackPage;