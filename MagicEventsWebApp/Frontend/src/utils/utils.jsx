export function isAdmin(eventId) {
	return localStorage.getItem('admin') !== null && JSON.parse(localStorage.getItem('admin')).includes(eventId);
}

export function setAdmin(eventId) {
	if (localStorage.getItem('admin')) {
		var admin = JSON.parse(localStorage.getItem('admin'));
		if (!admin.includes(eventId)) {
			admin.push(eventId);
			localStorage.setItem('admin', JSON.stringify(admin));
		}
	} else {
		localStorage.setItem('admin', JSON.stringify([eventId]));
	}
}

// export const url = 'localhost';
export const url = 'magicevents.com';
