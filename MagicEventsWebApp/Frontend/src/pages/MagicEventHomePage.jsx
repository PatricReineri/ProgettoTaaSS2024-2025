import { useEffect, useState } from 'react';
import Calendar from '../components/Lists/Calendar/Calendar';

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
		setTimeout(() => {
			let eventsResult = [
				// Eventi di oggi (2025-06-21)
				{
					title: 'Project Kickoff Meeting',
					description: 'Avvio ufficiale del progetto Alpha con tutti i team coinvolti.',
					starting: '2025-06-21T09:00',
					ending: '2025-06-21T10:00',
					location: 'Sala Riunioni 1, Sede Milano',
					creator: 'andrea.rossi@example.com',
					partecipants: ['marta.ferri@example.com', 'luca.bianco@example.com', 'giulia.verdi@example.com'],
					admins: ['andrea.rossi@example.com'],
				},
				{
					title: 'Security Breach Drill',
					description: 'Simulazione di risposta a un potenziale attacco informatico.',
					starting: '2025-06-21T14:00',
					ending: '2025-06-21T16:00',
					location: 'Laboratorio Sicurezza, Sede Roma',
					creator: 'it.security@example.com',
					partecipants: ['it.team@example.com', 'marco.pini@example.com'],
					admins: ['it.security@example.com'],
				},

				// Cluster di 6 eventi il 2025-06-25
				{
					title: 'UX Workshop',
					description: 'Laboratorio pratico di user experience con casi reali.',
					starting: '2025-06-25T09:00',
					ending: '2025-06-25T11:00',
					location: 'Sala Creativa 2, Sede Torino',
					creator: 'elena.bianchi@example.com',
					partecipants: ['valerio.gallo@example.com', 'chiara.santi@example.com', 'mario.rossi@example.com'],
					admins: ['elena.bianchi@example.com'],
				},
				{
					title: 'Marketing Brainstorm',
					description: 'Sessione creativa per la campagna estiva.',
					starting: '2025-06-25T11:30',
					ending: '2025-06-25T13:00',
					location: 'Sala Marketing 1, Sede Milano',
					creator: 'valerio.gallo@example.com',
					partecipants: ['chiara.santi@example.com', 'francesca.lodi@example.com'],
					admins: ['valerio.gallo@example.com'],
				},
				{
					title: 'Financial Review Q2',
					description: 'Analisi dettagliata dei risultati finanziari del secondo trimestre.',
					starting: '2025-06-25T14:00',
					ending: '2025-06-25T15:30',
					location: 'Sala Finanza 3, Sede Roma',
					creator: 'lucia.conti@example.com',
					partecipants: ['andrea.mele@example.com', 'marta.ferri@example.com'],
					admins: ['lucia.conti@example.com'],
				},
				{
					title: 'Client Presentation: Beta Corp',
					description: 'Demo delle nuove funzionalità per il cliente Beta Corp.',
					starting: '2025-06-25T16:00',
					ending: '2025-06-25T17:00',
					location: 'Zoom',
					creator: 'giulia.neri@example.com',
					partecipants: ['cliente.beta@example.com', 'marco.pini@example.com'],
					admins: ['giulia.neri@example.com'],
				},
				{
					title: 'DevOps Sync',
					description: 'Allineamento tra sviluppo e operations sugli ultimi deploy.',
					starting: '2025-06-25T17:30',
					ending: '2025-06-25T18:30',
					location: 'Online (Teams)',
					creator: 'stefano.marini@example.com',
					partecipants: ['it.team@example.com', 'luca.verdi@example.com'],
					admins: ['stefano.marini@example.com'],
				},
				{
					title: 'Team Building Aperitif',
					description: 'Aperitivo informale per rafforzare lo spirito di squadra.',
					starting: '2025-06-25T19:00',
					ending: '2025-06-25T21:00',
					location: 'Sky Bar, Sede Milano',
					creator: 'hr.relations@example.com',
					partecipants: ['tutti@example.com'],
					admins: ['hr.relations@example.com'],
				},

				// Altri eventi nei prossimi 10 giorni
				{
					title: 'React Native Seminar',
					description: 'Seminario introduttivo su React Native per lo sviluppo mobile.',
					starting: '2025-06-22T10:00',
					ending: '2025-06-22T12:00',
					location: 'Aula Magna, Sede Napoli',
					creator: 'elena.martini@example.com',
					partecipants: ['luca.verdi@example.com', 'chiara.santi@example.com', 'mario.rossi@example.com'],
					admins: ['elena.martini@example.com'],
				},
				{
					title: 'UX Research Interview',
					description: 'Intervista con utenti finali per la prossima release.',
					starting: '2025-06-23T09:00',
					ending: '2025-06-23T10:00',
					location: 'Sala User Lab, Sede Bologna',
					creator: 'elena.bianchi@example.com',
					partecipants: ['utente.test@example.com'],
					admins: ['elena.bianchi@example.com'],
				},
				{
					title: 'Data Science Meetup',
					description: 'Incontro informale per condividere progetti e tecniche di data science.',
					starting: '2025-06-23T18:00',
					ending: '2025-06-23T20:00',
					location: 'Cafè Tech, Sede Milano',
					creator: 'andrea.mele@example.com',
					partecipants: ['data.team@example.com', 'marta.ferri@example.com'],
					admins: ['andrea.mele@example.com'],
				},
				{
					title: 'Product Launch',
					description: 'Evento di lancio ufficiale della versione 2.0 del prodotto Gamma.',
					starting: '2025-06-24T15:00',
					ending: '2025-06-24T16:30',
					location: 'Auditorium Centrale, Sede Torino',
					creator: 'marketing@example.com',
					partecipants: ['tutti@example.com'],
					admins: ['marketing@example.com'],
				},
				{
					title: 'Mobile App Demo',
					description: 'Demo interna della nuova app mobile in sviluppo.',
					starting: '2025-06-24T11:00',
					ending: '2025-06-24T12:00',
					location: 'Sala Demo, Sede Roma',
					creator: 'dev.mobile@example.com',
					partecipants: ['dev.team@example.com'],
					admins: ['dev.mobile@example.com'],
				},
				{
					title: 'All-hands Meeting',
					description: 'Aggiornamento generale su tutti i progetti aziendali.',
					starting: '2025-06-26T09:30',
					ending: '2025-06-26T11:00',
					location: 'Sala Auditorium, Sede Milano',
					creator: 'ceo@example.com',
					partecipants: ['tutti@example.com'],
					admins: ['ceo@example.com'],
				},
				{
					title: 'Cloud Architecture Workshop',
					description: 'Approfondimenti su soluzioni cloud e best practice.',
					starting: '2025-06-27T13:00',
					ending: '2025-06-27T15:00',
					location: 'Sala IT 5, Sede Bologna',
					creator: 'it.architecture@example.com',
					partecipants: ['it.team@example.com', 'stefano.marini@example.com'],
					admins: ['it.architecture@example.com'],
				},
				{
					title: 'IT Maintenance Window',
					description: 'Finestra di manutenzione programmata dei server.',
					starting: '2025-06-27T22:00',
					ending: '2025-06-27T23:59',
					location: 'Data Center, Sede Roma',
					creator: 'it.ops@example.com',
					partecipants: ['it.team@example.com'],
					admins: ['it.ops@example.com'],
				},
				{
					title: 'HR Policy Update',
					description: 'Presentazione delle nuove linee guida HR.',
					starting: '2025-06-28T10:00',
					ending: '2025-06-28T11:00',
					location: 'Sala HR 2, Sede Torino',
					creator: 'hr.recruitment@example.com',
					partecipants: ['tutti@example.com'],
					admins: ['hr.recruitment@example.com'],
				},
				{
					title: 'Sales Training',
					description: 'Formazione sulle tecniche di vendita avanzate.',
					starting: '2025-06-29T14:00',
					ending: '2025-06-29T16:00',
					location: 'Sales Hub, Sede Milano',
					creator: 'sales.lead@example.com',
					partecipants: ['sales.team@example.com', 'andrea.mele@example.com'],
					admins: ['sales.lead@example.com'],
				},
				{
					title: 'Customer Feedback Session',
					description: 'Raccolta feedback dai principali clienti strategici.',
					starting: '2025-06-29T10:00',
					ending: '2025-06-29T11:30',
					location: 'Online (Zoom)',
					creator: 'customer.success@example.com',
					partecipants: ['cliente1@example.com', 'cliente2@example.com'],
					admins: ['customer.success@example.com'],
				},
				{
					title: 'Volunteer Day',
					description: 'Giornata di volontariato aziendale presso ONG locale.',
					starting: '2025-06-30T08:00',
					ending: '2025-06-30T12:00',
					location: 'Sede ONG, Milano',
					creator: 'hr.csr@example.com',
					partecipants: ['tutti@example.com'],
					admins: ['hr.csr@example.com'],
				},
			];
			eventsResult = mergeDaysAndEvents(getNextNDaysFormatted(), getUpcomingEvents(eventsResult));
			setEvents(eventsResult);
		}, 5000);
	}, []);

	return (
		<div className="h-full overflow-y-auto bg-[#505458] p-4">
			<div className="flex gap-2 items-center">
				<div className="h-4 w-4 bg-[#E4DCEF] rounded-full"></div>
				<p className="text-[#E4DCEF] font-bold ">Scheduled</p>
			</div>
			<Calendar days={events}></Calendar>
		</div>
	);
};

export default MagicEventHomePage;
