import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import LoginPage from './pages/Authentication/LoginPage';
import RegisterPage from './pages/Authentication/RegisterPage';
import UserProfilePage from './pages/UserProfile/UserProfilePage';
import GoogleCallbackPage from './pages/Authentication/GoogleCallbackPage';
import ChangePasswordPage from './pages/Authentication/ChangePasswordPage';
import ModifyUserValuePage from './pages/UserProfile/ModifyUserPage';
import HomePage from './pages/HomePage';
import NavBar from './components/navigation/NavBar';
import BoardPage from './pages/Event/Board/BoardPage';
import Button from './components/buttons/Button';
import MagicEventHomePage from './pages/MagicEventHomePage';
import LogoutButton from './components/buttons/LogoutButton';
import CreationEventPage from './pages/Event/CreationEventPage';
import MyEventsPage from './pages/Event/MyEventsPage';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBoxArchive, faHome, faPen } from '@fortawesome/free-solid-svg-icons';
import { APIProvider } from '@vis.gl/react-google-maps';
import EventsPage from './pages/Event/EventPage';
import clsx from 'clsx';
import GalleryPage from './pages/Event/Gallery/GalleryPage';
import GamePage from './pages/Event/Game/GamePage';
import ModifyEventPage from './pages/Event/ModifyEventPage';
import GameForm from './pages/Event/Game/GameForm';

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
						<FontAwesomeIcon className="text-2xl hover:scale-110" icon={faHome} color="#EE0E51" />
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
								<button className="inline-flex items-center px-4 py-1 bg-[#E4DCEF] text-[#363540] inner-shadow cursor-pointer hover:scale-105 rounded-full max-h-[40px] overflow-hidden text-ellipsis whitespace-nowrap">
									{JSON.parse(sessionStorage.getItem('user')).username}
								</button>
							</NavLink>
						</div>
					)
				}
			>
				<NavLink className="w-fit" to="/myevents">
					<Button
						text={
							<div className="flex max-sm:flex-col gap-2 justify-center items-center">
								<FontAwesomeIcon className="text-lg" icon={faBoxArchive} />{' '}
								<p className="max-sm:text-[0.6rem]">I miei eventi</p>
							</div>
						}
						link
						custom={clsx({ '!w-max text-md  ': true, hidden: !logged })}
					></Button>
				</NavLink>
				<NavLink className="w-fit" to="/newevent">
					<Button
						text={
							<div className="flex max-sm:flex-col gap-2 justify-center items-center">
								<FontAwesomeIcon className="text-lg" icon={faPen} />{' '}
								<p className="max-sm:text-[0.6rem]">Crea il tuo evento</p>
							</div>
						}
						link
						custom={clsx({ '!w-max text-md  ': true, hidden: !logged })}
					></Button>
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
					<Route
						path="/newevent"
						element={
							<APIProvider apiKey={'insert api-key maps'}>
								<CreationEventPage />
							</APIProvider>
						}
					/>
					<Route
						path="/myevents"
						element={
							<APIProvider apiKey={'insert api-key maps'}>
								<MyEventsPage />
							</APIProvider>
						}
					/>
					<Route path="/modifyevent/:eventId" element={<ModifyEventPage />} />
					<Route path="/:eventId/board" element={<BoardPage />} />
					<Route path="/:eventId/gallery" element={<GalleryPage />} />
					<Route path="/:eventId" element={<EventsPage />} />
					<Route path="/:eventId/game" element={<GamePage />} />
					<Route path="/:eventId/game/form" element={<GameForm />} />
				</Routes>
			</div>
		</Router>
	);
}

export default App;
