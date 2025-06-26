export function login(formData) {
	const params = new URLSearchParams();
	for (const key in formData) {
		params.append(key, formData[key]);
	}

	try {
		return fetch(`https://localhost:8443/login/form?${params.toString()}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
		});
	} catch (err) {
		console.error('Error:', err.message);
		return null;
	}
}

export function forgotPasswordRequest(email) {
	return fetch('https://localhost:8443/login/generateresetpasswordlink', {
		method: 'POST',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		body: new URLSearchParams({
			email: email,
		}).toString(),
	});
}

export function register(formData) {
	const params = new URLSearchParams();
	for (const key in formData) {
		params.append(key, formData[key]);
	}
	return fetch('https://localhost:8443/login/register', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: params.toString(),
	});
}

export function modifyUser(user) {
	return fetch('https://localhost:8443/login/modifyuser', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		body: user,
	});
}

export function deleteUser(email) {
	return fetch('https://localhost:8443/login/deleteuser', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: new URLSearchParams({
			email: email,
		}).toString(),
	});
}
