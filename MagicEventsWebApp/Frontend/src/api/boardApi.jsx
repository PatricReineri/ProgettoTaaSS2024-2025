import { url } from '../utils/utils';

export function getMessages(eventID, pageNumber) {
	return fetch(
		`http://${url}:8081/board/getBoard/${eventID}/${pageNumber}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
