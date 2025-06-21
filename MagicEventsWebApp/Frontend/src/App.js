import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import LoginPage from './pages/Authentication/LoginPage';
import RegisterPage from './pages/Authentication/RegisterPage';
import UserProfilePage from './pages/UserProfile/UserProfilePage';
import GoogleCallbackPage from './pages/Authentication/GoogleCallbackPage';
import ChangePasswordPage from './pages/Authentication/ChangePasswordPage';
import ModifyUserValuePage from './pages/UserProfile/ModifyUserValuePage';
import HomePage from './pages/HomePage';
import NavBar from './components/navigation/NavBar';
import BoardPage from './pages/Event/Board/BoardPage';
import Button from './components/buttons/Button';
import MagicEventHomePage from './pages/MagicEventHomePage';

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
			<NavBar
				logo={
					<NavLink to="/">
						<h1 className="text-[#EE0E51] font-extrabold hover:scale-110">MagicEvent</h1>
					</NavLink>
				}
				actions={
					<div className="flex gap-2 ">
						<NavLink to="/login">
							<Button text="Login"></Button>
						</NavLink>
						<NavLink to="/register">
							<Button secondary text="Register"></Button>
						</NavLink>
					</div>
				}
			/>
			<div className=" h-[calc(100vh-3.5rem)]">
				<Routes>
					<Route path="/" element={<HomePage />} />
					<Route path="/login" element={<LoginPage />} />
					<Route path="/register" element={<RegisterPage />} />
					<Route path="/home" element={<MagicEventHomePage />} />
					<Route path="/userprofile" element={<UserProfilePage />} />
					<Route path="/googlecallback" element={<GoogleCallbackPage />} />
					<Route path="/changepassword" element={<ChangePasswordPage />} />
					<Route path="/modifyuser" element={<ModifyUserValuePage />} />
					<Route path="/:eventId/board" element={<BoardPage />} />
				</Routes>
			</div>
		</Router>
	);
}

export default App;
