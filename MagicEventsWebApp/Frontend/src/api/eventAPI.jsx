import { url } from '../utils/utils';


const eventsManagementUrl = url === 'localhost' ? `${url}:8080` : url;
const eventsetupUrl = url === 'localhost' ? `${url}:8086` : url;

export function getEventsp() {
	return fetch(
		`http://${eventsManagementUrl}/gestion/geteventslistp?partecipantId=${
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
		`http://${eventsManagementUrl}/gestion/geteventslistc?creatorId=${JSON.parse(sessionStorage.getItem('user')).magicEventTag}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getEventId(title, day) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	const magicEventsTag = user?.magicEventTag;

	return fetch(`http://${eventsManagementUrl}/gestion/geteventid?title=${title}&day=${day}&magicEventTag=${magicEventsTag}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function annullEvent(eventId) {
	return fetch(
		`http://${eventsManagementUrl}/gestion/annullevent?eventId=${eventId}&magicEventsTag=${
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

	const url = `http://${eventsManagementUrl}/gestion/activeservices?eventId=${eventId}&magicEventsTag=${magicEventsTag}`;

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
		`http://${eventsManagementUrl}/gestion/getadminsforevent?eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getPartecipants(eventId) {
	return fetch(`http://${eventsManagementUrl}/gestion/getpartecipantsforevent?eventId=${eventId}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function modifyEvent(eventId, event) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	const magicEventsTag = user?.magicEventTag;

	const url = `http://${eventsManagementUrl}/gestion/modify?eventId=${eventId}&magicEventsTag=${magicEventsTag}`;

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
		`http://${eventsManagementUrl}/gestion/de-annullevent?eventId=${eventId}&magicEventsTag=${
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
		`http://${eventsManagementUrl}/gestion/updateadmins?admins=${newAdmins}&eventId=${eventId}&magicEventsTag=${
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
		`http://${eventsManagementUrl}/gestion/addpartecipants?partecipants=${newPartecipants}&eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function removePartecipant(eventId, partecipantEmail) {
	return fetch(
		`http://${eventsManagementUrl}/gestion/removepartecipant?partecipant=${partecipantEmail}&eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function removeAdmin(eventId, adminEmail) {
	return fetch(
		`http://${eventsManagementUrl}/gestion/removeadmin?admin=${adminEmail}&eventId=${eventId}&magicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'PUT',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getEvent(eventId) {
	return fetch(`http://${eventsManagementUrl}/gestion/geteventinfo?eventId=${eventId}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function getEventService(eventId) {
	const params = new URLSearchParams();
	params.append('eventId', eventId);
	params.append('magicEventTag', JSON.parse(sessionStorage.getItem('user')).magicEventTag);
	return fetch(`http://${eventsManagementUrl}/gestion/geteventenabledservices?${params}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}

export function createEvent(event) {
	var temp = JSON.parse(JSON.stringify(event));
	console.log(temp);

	temp.participants = temp.participants.filter((item) => !temp.admins.includes(item));
	console.log(temp);
	return fetch(`http://${eventsetupUrl}/eventSetup`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(temp),
	});
}

export function deleteEvent(eventId) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	const magicEventsTag = user?.magicEventTag;

	const urldelete = `http://${eventsManagementUrl}/gestion/delete?eventId=${eventId}&magicEventsTag=${magicEventsTag}`;

	return fetch(urldelete, {
		method: 'DELETE',
	});
}

export function isActive(eventId) {
	return fetch(
		`http://${eventsManagementUrl}/gestion/isactive?creatorId=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}&eventId=${eventId}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
