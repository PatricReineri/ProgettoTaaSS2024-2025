import React from 'react';

function App() {
  const googleClientId = "856285122317-g1d4mlarfh9s00pubcj8uc1lij9h0peq.apps.googleusercontent.com";
  const redirectUri = "https://localhost:8443/login/grantcode";
  const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=${encodeURIComponent(redirectUri)}&response_type=code&client_id=${googleClientId}&scope=${encodeURIComponent('https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid')}&access_type=offline`;

  return (
    <div className="d-flex justify-content-center flex-column" style={{ alignItems: 'center', marginTop: '50px' }}>
      <a href={googleAuthUrl}>
        <div className="d-flex justify-content-center mt-2">
          <img src="sign-google.jpg" className="w-50" alt="Accedi con Google" />
        </div>
      </a>
    </div>
  );
}

export default App;
