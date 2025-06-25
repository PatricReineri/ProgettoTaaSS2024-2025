import React, { useEffect, useState } from 'react';
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
import LogoutButton from './components/buttons/LogoutButton';
import CreationEventPage from './pages/Event/CreationEventPage';
import MyEventsPage from './pages/Event/MyEventsPage';

function App() {
	const [logged, setLogged] = useState(sessionStorage.getItem('user') ? true : false);

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
						<h1 className="text-[#EE0E51] font-extrabold  hover:scale-110">MagicEvent</h1>
					</NavLink>
				}
				actions={
					!logged ? (
						<div className={'flex gap-2 '}>
							<NavLink to="/login">
								<Button text="Login"></Button>
							</NavLink>
							<NavLink to="/register">
								<Button secondary text="Register"></Button>
							</NavLink>
						</div>
					) : (
						<div className={'flex gap-2 items-center'}>
							<LogoutButton setLogged={setLogged}></LogoutButton>
							<NavLink to="/userprofile">
								<button className="bg-[#E4DCEF] text-[#363540] px-4  inner-shadow p-1 cursor-pointer hover:scale-105 rounded-full">
									{JSON.parse(sessionStorage.getItem('user')).username}
								</button>
							</NavLink>
						</div>
					)
				}
			>
				<NavLink className="w-fit" to="/myevents">
					<Button text="My events" link custom="  !w-20 text-md  "></Button>
				</NavLink>
				<NavLink className="w-fit" to="/newevent">
					<Button text="Create event" link custom="  !w-20  !text-md    "></Button>
				</NavLink>
			</NavBar>
			<div className=" h-[calc(100vh-3.5rem)]">
				<Routes>
					<Route path="/" element={logged ? <MagicEventHomePage /> : <HomePage />} />
					<Route path="/login" element={<LoginPage setLogged={setLogged} />} />
					<Route path="/register" element={<RegisterPage setLogged={setLogged} />} />
					<Route path="/home" element={<MagicEventHomePage />} />
					<Route path="/userprofile" element={<UserProfilePage setLogged={setLogged} />} />
					<Route path="/googlecallback" element={<GoogleCallbackPage setLogged={setLogged} />} />
					<Route path="/changepassword" element={<ChangePasswordPage />} />
					<Route path="/modifyuser" element={<ModifyUserValuePage setLogged={setLogged} />} />
					<Route path="/newevent" element={<CreationEventPage />} />
					<Route path="/myevents" element={<MyEventsPage />} />
					<Route path="/:eventId/board" element={<BoardPage eventID={1} />} />
				</Routes>
			</div>
		</Router>
	);
}

export default App;
