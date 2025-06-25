export function getEventsp() {
	return fetch(`http://localhost:8080/gestion/geteventslistp`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		body: new URLSearchParams({
			partecipantId: JSON.parse(sessionStorage.getItem('user')).magicEventTag,
		}).toString(),
	});
}
