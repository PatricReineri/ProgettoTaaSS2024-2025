import { useEffect, useState } from 'react';
import Calendar from '../components/Lists/Calendar/Calendar';
import { getEventsp } from '../api/eventAPI';

const MagicEventHomePage = () => {
	const [events, setEvents] = useState([]);
	const [ready, setReady] = useState(false);

	function getUpcomingEvents(events, daysAhead = 10) {
		const now = new Date();
		now.setHours(0, 0, 0, 0);
		const max = new Date(now);
		max.setDate(max.getDate() + daysAhead);

		const monthNames = [
			'Gennaio',
			'Febbraio',
			'Marzo',
			'Aprile',
			'Maggio',
			'Giugno',
			'Luglio',
			'Agosto',
			'Settembre',
			'Ottobre',
			'Novembre',
			'Dicembre',
		];

		return events
			.map((e) => {
				const d = new Date(e.starting);
				return {
					...e,
					_date: d,
				};
			})
			.filter((e) => e._date >= now && e._date <= max)
			.map((e) => {
				const d = e._date;
				const day = `${d.getDate()} ${monthNames[d.getMonth()]}`;
				const hh = String(d.getHours()).padStart(2, '0');
				const mm = String(d.getMinutes()).padStart(2, '0');
				return {
					...e,
					starting: { day, hour: `${hh}:${mm}` },
					// tolgo il campo interno
				};
			})
			.map(({ _date, ...e }) => e);
	}

	// 2) Restituisce i prossimi N giorni (incluso oggi) formattati { day }
	function getNextNDaysFormatted(daysCount = 10) {
		const monthNames = [
			'Gennaio',
			'Febbraio',
			'Marzo',
			'Aprile',
			'Maggio',
			'Giugno',
			'Luglio',
			'Agosto',
			'Settembre',
			'Ottobre',
			'Novembre',
			'Dicembre',
		];
		const today = new Date();
		today.setHours(0, 0, 0, 0);

		return Array.from({ length: daysCount }, (_, i) => {
			const d = new Date(today);
			d.setDate(d.getDate() + i);
			return { day: `${d.getDate()} ${monthNames[d.getMonth()]}` };
		});
	}

	// 3) Unisce giorni e eventi (con starting.day) in una struttura { day, events }
	function mergeDaysAndEvents(daysList, events) {
		const grouped = daysList.reduce((acc, { day }) => {
			acc[day] = [];
			return acc;
		}, {});
		events.forEach((e) => {
			const day = e.starting.day;
			if (grouped[day]) grouped[day].push(e);
		});
		return daysList.map(({ day }) => ({ day, events: grouped[day] }));
	}

	useEffect(() => {
		setReady(false);

		async function fetchAPI() {
			const res = await getEventsp();
			if (!res.ok) {
				console.log(res);

				setEvents([]);
				return;
			}
			const data = await res.json();

			let eventsResult = mergeDaysAndEvents(getNextNDaysFormatted(), getUpcomingEvents(data));
			setEvents(eventsResult);
		}

		fetchAPI();
	}, []);

	return (
		<div className="h-full overflow-y-auto bg-[#505458] p-4">
			<div className="flex gap-2 items-center">
				<div className="h-4 w-4 bg-[#E4DCEF] rounded-full"></div>
				<p className="text-[#E4DCEF] font-bold ">In programma</p>
			</div>
			<Calendar days={events}></Calendar>
		</div>
	);
};

export default MagicEventHomePage;
