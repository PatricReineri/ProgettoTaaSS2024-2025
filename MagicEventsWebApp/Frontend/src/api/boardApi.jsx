export function getMessages(eventID, pageNumber) {
	return fetch(`http://localhost:8081/board/getBoard/${eventID}/${pageNumber}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	});
}
