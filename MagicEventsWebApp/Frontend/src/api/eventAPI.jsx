export function getEventsp() {
	return fetch(
		`http://localhost:8080/gestion/geteventslistp?partecipantId=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getEventsc() {
	return fetch(
		`http://localhost:8080/gestion/geteventslistc?partecipantId=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getEventId(title, day) {
	return fetch(
		`http://localhost:8080/gestion/geteventid?creatorId=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}&title=${title}&day=${day}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function annullEvent(eventId) {
	return fetch(
		`http://localhost:8080/gestion/annullevent?eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function activateServices(eventId, services) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	const magicEventsTag = user?.magicEventTag;

	const url = `http://localhost:8080/gestion/activeservices?eventId=${eventId}&magicEventsTag=${magicEventsTag}`;

	return fetch(url, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(services),
	});
}

export function getAdmins(eventId) {
	return fetch(
		`http://localhost:8080/gestion/getadminsforevent?eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getPartecipants(eventId) {
	return fetch(`http://localhost:8080/gestion/getpartecipantsforevent?eventId=${eventId}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function modifyEvent(eventId, event) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	const magicEventsTag = user?.magicEventTag;

	const url = `http://localhost:8080/gestion/modify?eventId=${eventId}&magicEventsTag=${magicEventsTag}`;

	return fetch(url, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(event),
	});
}

export function deannullEvent(eventId) {
	return fetch(
		`http://localhost:8080/gestion/de-annullevent?eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function updateAdmins(eventId, newAdmins) {
	return fetch(
		`http://localhost:8080/gestion/updateadmins?admins=${newAdmins}&eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function updatePartecipants(eventId, newPartecipants) {
	return fetch(
		`http://localhost:8080/gestion/addpartecipants?admins=${newPartecipants}&eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getEvent(eventId) {
	return fetch(`http://localhost:8080/gestion/geteventinfo?eventId=${eventId}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function getEventService(eventId) {
	const params = new URLSearchParams();
	params.append('eventId', eventId);
	params.append('magicEventsTag', JSON.parse(sessionStorage.getItem('user')).magicEventTag);
	return fetch(`http://localhost:8080/gestion/getEventEnabledServices?${params}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function createEvent(event) {
	return fetch(`http://localhost:8086/eventSetup`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(event),
	});
}

export function deleteEvent(eventId) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	const magicEventsTag = user?.magicEventTag;

	const url = `http://localhost:8080/gestion/delete?eventId=${eventId}&magicEventsTag=${magicEventsTag}`;

	return fetch(url, {
		method: 'DELETE',
	});
}
