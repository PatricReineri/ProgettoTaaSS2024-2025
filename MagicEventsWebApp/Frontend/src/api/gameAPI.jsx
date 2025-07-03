import { url } from '../utils/utils';

const guestGameUrl = url === 'localhost' ? `${url}:8083` : url;

export function getGame(eventID) {
	return fetch(
		`http://${guestGameUrl}/guest-game/createDecisionTree/${eventID}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function isDataInGame(eventID) {
	return fetch(
		`http://${guestGameUrl}/guest-game/hasUserInsertedInfo/${eventID}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function insertInfo(formData) {
	return fetch(`http://${guestGameUrl}/guest-game/insertGuestInfo`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(formData),
	});
}
