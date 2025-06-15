function LogoutPage() {
	const handleLogout = async (e) => {
		e.preventDefault();

		try {
			const res = await fetch('https://localhost:8443/login/logoutuser', {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: new URLSearchParams({
					email: 'alescicolone01@gmail.com',
				}).toString(),
			});

			if (!res.ok) throw new Error('logout fallito');
			const data = await res.json();
			console.log('Success:', data);
		} catch (err) {
			console.error('Error:', err.message);
		}
	};

	return (
		<div style={{ maxWidth: 400, margin: '60px auto', fontFamily: 'Arial' }}>
			<button onClick={handleLogout}>Logout</button>
		</div>
	);
}

export default LogoutPage;
