import { useEffect, useState } from 'react';
import { NavLink, useParams } from 'react-router-dom';
import { getEvent, getEventService } from '../../api/eventAPI';
import Image from '../../components/imagesComponent/Image';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
	faArrowAltCircleRight,
	faClipboard,
	faClose,
	faDownload,
	faGamepad,
	faImage,
	faMap,
	faMessage,
	faUsers,
} from '@fortawesome/free-solid-svg-icons';
import { APIProvider, Map, Marker, useMapsLibrary } from '@vis.gl/react-google-maps';
import ErrorContainer from '../../components/Error/ErrorContainer';
import { convertDataTime } from '../../utils/dataFormatter';
import { setAdmin } from '../../utils/utils';
import Button from '../../components/buttons/Button';
import clsx from 'clsx';
import LoadingContainer from '../../components/Error/LoadingContainer';

const EventsPage = () => {
	const { eventId } = useParams();
	const [event, setEvent] = useState(null);
	const [loading, setLoading] = useState(true);
	const [eventServices, setEventServices] = useState(null);
	const [lat, setLat] = useState(0);
	const [lng, setLng] = useState(0);
	const [openPopup, setOpenPopup] = useState(0);
	const [activeTab, setActiveTab] = useState(0);

	const downloadImage = () => {
		if (!event.image) return;

		const link = document.createElement('a');
		link.href = 'data:image/png;base64,' + event.image;
		link.download = `${event.title || 'event'}-image.png`;
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	};

	useEffect(() => {
		async function fetchAPI() {
			const res = await getEvent(eventId);
			if (!res.ok) {
				setEvent(null);
				return;
			}
			const data = await res.json();

			if (data.admins.includes(JSON.parse(sessionStorage.getItem('user')).email)) {
				setAdmin(eventId);
			}

			if (data.creator === JSON.parse(sessionStorage.getItem('user')).magicEventTag) {
				setAdmin(eventId);
			}

			setEvent(data);
			if (data.location) {
				const coordinates = data.location.split('-');
				setLat(Number(coordinates[0]));
				setLng(Number(coordinates[1]));
			}
			setLoading(false);
		}

		async function fetchAPIServices() {
			const res = await getEventService(eventId);
			if (!res.ok) {
				setEvent(null);
				return;
			}
			const data = await res.json();
			console.log(data);
			setEventServices(data);
		}

		fetchAPI();

		fetchAPIServices();
	}, [eventId]);

	const sidebarTabs = [
		{
			icon: faUsers,
			title: 'Partecipanti',
			content: (
				<div className="flex flex-col gap-2 h-full">
					<h2 className="font-bold text-lg">Partecipanti</h2>
					<div className="flex flex-auto flex-col gap-1 overflow-y-auto bg-[#505458] p-2 rounded-md">
						{event?.partecipants.map((p, index) => (
							<p key={index} className="p-2 bg-[#363540] rounded">{p}</p>
						))}
					</div>
					<h2 className="font-bold text-lg mt-4">Admin</h2>
					<div className="flex flex-auto flex-col gap-1 overflow-y-auto bg-[#505458] p-2 rounded-md">
						{event?.admins.map((p, index) => (
							<p key={index} className="p-2 bg-[#363540] rounded">{p}</p>
						))}
					</div>
				</div>
			),
		},
		{
			icon: faMap,
			title: 'Mappa',
			content: event?.location ? (
				<div className="h-full">
					<h2 className="font-bold text-lg mb-4">Posizione</h2>
					<div className="border-[#E4DCEF] border rounded-md">
						<APIProvider apiKey={'insert api-key maps'}>
							<Map
								key={lat + '--' + lng}
								style={{ width: '100%', height: '300px' }}
								defaultCenter={{ lat: lat, lng: lng }}
								defaultZoom={15}
								gestureHandling={'greedy'}
								disableDefaultUI={true}
							>
								<Marker position={{ lat: lat ? lat : 0, lng: lng ? lng : 0 }}></Marker>
							</Map>
						</APIProvider>
					</div>
				</div>
			) : (
				<div>
					<h2 className="font-bold text-lg mb-4">Posizione</h2>
					<p>Nessuna location fornita</p>
				</div>
			),
		},
		{
			icon: faClipboard,
			title: 'Servizi',
			content: (
				<div className="h-full">
					<h2 className="font-bold text-lg mb-4">Servizi Disponibili</h2>
					{eventServices ? (
						<div className="space-y-2">
							{eventServices.board && (
								<NavLink to={`/${eventId}/board`} className="block">
									<div className="flex items-center gap-3 hover:bg-[#505458] p-3 rounded-lg transition-colors">
										<FontAwesomeIcon icon={faMessage} />
										<span>Bacheca</span>
									</div>
								</NavLink>
							)}
							{eventServices.gallery && (
								<NavLink to={`/${eventId}/gallery`} className="block">
									<div className="flex items-center gap-3 hover:bg-[#505458] p-3 rounded-lg transition-colors">
										<FontAwesomeIcon icon={faImage} />
										<span>Galleria</span>
									</div>
								</NavLink>
							)}
							{eventServices.guestGame && (
								<NavLink to={`/${eventId}/game`} className="block">
									<div className="flex items-center gap-3 hover:bg-[#505458] p-3 rounded-lg transition-colors">
										<FontAwesomeIcon icon={faGamepad} />
										<span>Mystery Guest Game</span>
									</div>
								</NavLink>
							)}
						</div>
					) : (
						<p>Errore nel caricamento dei servizi</p>
					)}
				</div>
			),
		},
	];

	return !event ? (
		loading ? (
			<LoadingContainer />
		) : (
			<ErrorContainer errorMessage={'Nessun evento trovato'} to="/home" />
		)
	) : (
		<div className="h-full flex bg-[#505458]">
			{/* Main Content */}
			<div className="flex-1 text-[#E4DCEF] p-4 flex flex-col gap-4 overflow-y-auto">
				<div className="relative">
					<Image onClick={() => setOpenPopup(true)} src={event.image} />
					<Button
						onClick={downloadImage}
						custom="absolute top-2 right-2 bg-black/50 hover:bg-black/70 backdrop-blur-sm"
						text={<FontAwesomeIcon icon={faDownload} />}
					/>
				</div>
				
				<h1 className="text-xl lg:text-2xl font-extrabold">{event.title}</h1>
				
				<p className="p-4 border bg-[#363540] border-[#E4DCEF] text-[#E4DCEF] text-sm rounded-md">
					{event.description}
				</p>
				
				<div className="flex flex-col sm:flex-row justify-between items-center gap-4">
					<p className="font-bold">Data: </p>
					<div className="flex items-center gap-2">
						<h1 className="bg-[#E4DCEF] text-[#363540] rounded-full px-3 py-2 font-bold text-sm">
							{convertDataTime(event.starting)}
						</h1>
						<FontAwesomeIcon className="text-2xl" icon={faArrowAltCircleRight} color="#E4DCEF" />
						<h1 className="bg-[#E4DCEF] text-[#363540] rounded-full px-3 py-2 font-bold text-sm">
							{convertDataTime(event.ending)}
						</h1>
					</div>
				</div>
			</div>

			{/* Fixed Sidebar */}
			<div className="w-80 bg-[#363540] text-[#E4DCEF] flex flex-col border-l border-[#E4DCEF]/20">
				{/* Tab Headers */}
				<div className="flex border-b border-[#E4DCEF]/20">
					{sidebarTabs.map((tab, index) => (
						<button
							key={index}
							onClick={() => setActiveTab(index)}
							className={clsx(
								'flex-1 p-3 flex flex-col items-center gap-1 transition-colors',
								activeTab === index 
									? 'bg-[#505458] text-[#EE0E51]' 
									: 'hover:bg-[#505458]/50'
							)}
						>
							<FontAwesomeIcon icon={tab.icon} />
							<span className="text-xs">{tab.title}</span>
						</button>
					))}
				</div>
				
				{/* Tab Content */}
				<div className="flex-1 p-4 overflow-y-auto">
					{sidebarTabs[activeTab]?.content}
				</div>
			</div>

			{/* Image Popup */}
			<div
				className={clsx({
					'fixed inset-0 bg-[#363540]/80 backdrop-blur-md flex items-center justify-center z-50': openPopup,
					hidden: !openPopup,
				})}
			>
				<div className="relative max-h-[90vh] max-w-[90vw]">
					<img
						className="max-h-full max-w-full object-contain rounded-lg"
						src={'data:image/*;base64,' + event.image}
						alt="popup image"
					/>
					<Button
						onClick={() => setOpenPopup(false)}
						custom="absolute top-4 right-4 bg-black/50 hover:bg-black/70"
						text={<FontAwesomeIcon icon={faClose} />}
					/>
				</div>
			</div>
		</div>
	);
};

export default EventsPage;
