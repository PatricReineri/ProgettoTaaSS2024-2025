import { url } from '../utils/utils';

const boardUrl = url === 'localhost' ? `${url}:8081` : `${url}/api/boards`;

export function getMessages(eventID, pageNumber) {
	return fetch(
		`http://${boardUrl}/board/getBoard/${eventID}/${pageNumber}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
