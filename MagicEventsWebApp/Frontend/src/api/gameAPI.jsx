import { url } from '../utils/utils';

export function getGame(eventID) {
	return fetch(
		`http://${url}:8083/guest-game/createDecisionTree/${eventID}?userMagicEventsTag=${
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
		`http://${url}:8083/guest-game/hasUserInsertedInfo/${eventID}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function insertInfo(formData) {
	return fetch(`http://${url}:8083/guest-game/insertGuestInfo`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(formData),
	});
}
