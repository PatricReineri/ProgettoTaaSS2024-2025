import { url } from '../utils/utils';

const guestGameUrl = url === 'localhost' ? `https://${url}:8083` : `https://${url}/api/games`;

export function getGame(eventID) {
	return fetch(
		`${guestGameUrl}/guest-game/createDecisionTree/${eventID}?userMagicEventsTag=${
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
		`${guestGameUrl}/guest-game/hasUserInsertedInfo/${eventID}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function insertInfo(formData) {
	return fetch(`${guestGameUrl}/guest-game/insertGuestInfo`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(formData),
	});
}
