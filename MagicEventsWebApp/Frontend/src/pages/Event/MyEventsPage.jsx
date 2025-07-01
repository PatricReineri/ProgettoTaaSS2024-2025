import { useEffect, useState } from 'react';
import { getEventsc } from '../../api/eventAPI';
import { mapEventDTOtoCardProps } from '../../utils/eventObjectMapping';
import EventList from '../../components/Lists/EventList';

const MyEventsPage = () => {
	const [events, setEvents] = useState([]);
	const [ready, setReady] = useState(false);
	useEffect(() => {
		setReady(false);

		async function fetchAPI() {
			const res = await getEventsc();
			if (!res.ok) {
				console.log(res);

				setEvents([]);
				return;
			}
			const data = await res.json();

			// Mappa ogni eventDTO in formato per la card
			const mappedEvents = await data.map(mapEventDTOtoCardProps);

			setEvents(mappedEvents);
			setReady(true);
		}
		fetchAPI();
	}, []);
	return (
		<div className="h-full overflow-auto">
			{ready ? <EventList events={events} /> : <p className="p-4">Caricamento...</p>}
		</div>
	);
};

export default MyEventsPage;
