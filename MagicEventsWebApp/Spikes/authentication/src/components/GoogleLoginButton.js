import { GoogleLogin } from '@react-oauth/google';
import { jwtDecode } from 'jwt-decode';

const GoogleLoginButton = ({ onLogin }) => {
  return (
    <GoogleLogin
      onSuccess={credentialResponse => {
        const decoded = jwtDecode(credentialResponse.credential);
        onLogin(decoded);
      }}
      onError={() => {
        console.log('Login Failed');
      }}
    />
  );
};

export default GoogleLoginButton;
