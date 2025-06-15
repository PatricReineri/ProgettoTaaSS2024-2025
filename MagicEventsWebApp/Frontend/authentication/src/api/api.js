export function login(formData) {
	const params = new URLSearchParams();
	for (const key in formData) {
		params.append(key, formData[key]);
	}
	return fetch('https://localhost:8443/login/form', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: params.toString(),
	});
}
