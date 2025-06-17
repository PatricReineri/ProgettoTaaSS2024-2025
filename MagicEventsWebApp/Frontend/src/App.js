import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import UserProfilePage from './pages/UserProfilePage';
import GoogleCallbackPage from './pages/GoogleCallbackPage';
import ChangePasswordPage from './pages/ChangePasswordPage';
import ModifyUserValuePage from './pages/ModifyUserValuePage';
import HomePage from './pages/HomePage';
import NavBar from './components/navigation/NavBar';

function App() {
	useEffect(() => {
		const script = document.createElement('script');

		script.src = 'https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4';
		script.async = true;

		document.body.appendChild(script);

		return () => {
			document.body.removeChild(script);
		};
	}, []);

	return (
		<Router>
			<NavBar />
			<div className=" h-[calc(100vh-3.5rem)]">
				<Routes>
					<Route path="/" element={<HomePage />} />
					<Route path="/login" element={<LoginPage />} />
					<Route path="/register" element={<RegisterPage />} />
					<Route path="/userprofile" element={<UserProfilePage />} />
					<Route path="/googlecallback" element={<GoogleCallbackPage />} />
					<Route path="/changepassword" element={<ChangePasswordPage />} />
					<Route path="/modifyuser" element={<ModifyUserValuePage />} />
				</Routes>
			</div>
		</Router>
	);
}

export default App;
