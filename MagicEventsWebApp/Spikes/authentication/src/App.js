import React, { useState } from 'react';
import GoogleLoginButton from './components/GoogleLoginButton';

function App() {
  const [user, setUser] = useState(null);

  return (
    <div style={{ padding: 20 }}>
      <h1>Login con Google</h1>
      {!user ? (
        <GoogleLoginButton onLogin={setUser} />
      ) : (
        <div>
          <p>Benvenuto, {user.name}</p>
          {/* <img src={user.picture} alt="avatar" style={{ borderRadius: '50%' }} /> */}
        </div>
      )}
    </div>
  );
}

export default App;
