import { url } from '../utils/utils';

const boardUrl = url === 'localhost' ? `https://${url}:8081` : `https://${url}/api/boards`;

export function getMessages(eventID, pageNumber) {
	return fetch(
		`${boardUrl}/board/getBoard/${eventID}/${pageNumber}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
