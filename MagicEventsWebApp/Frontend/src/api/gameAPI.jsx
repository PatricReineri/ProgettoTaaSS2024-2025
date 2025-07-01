export function getGame(eventID) {
	return fetch(
		`http://localhost:8081/guest-game/createDecisionTree/${eventID}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
