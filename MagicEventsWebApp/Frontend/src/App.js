import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import UserProfilePage from './pages/UserProfilePage';
import GoogleCallbackPage from './pages/GoogleCallbackPage';
import ChangePasswordPage from './pages/ChangePasswordPage';
import ModifyUserValuePage from './pages/ModifyUserValuePage';
import HomePage from './pages/HomePage'

function App() {
	return (
		<Router>
			<Routes>
				<Route path="/" element={<HomePage />} />
				<Route path="/login" element={<LoginPage />} />
				<Route path="/register" element={<RegisterPage />} />
				<Route path="/userprofile" element={<UserProfilePage />} />
				<Route path="/googlecallback" element={<GoogleCallbackPage />} />
				<Route path="/changepassword" element={<ChangePasswordPage />} />
				<Route path="/modifyuser" element={<ModifyUserValuePage />} />
			</Routes>
		</Router>
	);
}

export default App;
