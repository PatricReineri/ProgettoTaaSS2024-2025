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

export function createEvent(event) {
	console.log(event);
	return;
	// return fetch(`http://localhost:8086/eventSetup`, {
	// 	method: 'POST',
	// 	headers: { 'Content-Type': 'application/json' },
	// 	body: JSON.stringify(event),
	// });
}
