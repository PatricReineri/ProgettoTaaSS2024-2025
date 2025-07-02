import React from 'react';
import LogoutButton from '../../components/buttons/LogoutButton';
import DeleteUserButton from '../../components/buttons/DeleteUserButton';
import ModifierUserButton from '../../components/buttons/ModifierUserButton';

function UserProfilePage({ setLogged }) {
	const user = JSON.parse(sessionStorage.getItem('user'));
	if (!user) {
		return <p style={{ textAlign: 'center' }}>User not found</p>;
	}
	
	let img = user.profileImageUrl;
	if(img === null || !img){
		img = '/default-avatar.png'
	}

	return (
		<div className="h-full overflow-y-auto bg-[#505458] p-4">
			<div
				style={{
					maxWidth: '400px',
					margin: '50px auto',
					padding: '20px',
					borderRadius: '12px',
					boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
					fontFamily: 'Arial',
					backgroundColor: '#f9f9f9',
				}}
			>
				<h2 className="text-2xl font-bold mb-4 text-center">Profilo utente</h2>

				<img
					src={
							img.startsWith('http')
							? img.replace(/'+$/, '')
							: img.startsWith('/default-avatar.png')
							? img
							: 'data:image/*;base64,' + img
					}
					alt="Profile image"
					className="w-24 h-24 rounded-full object-cover mx-auto"
				/>

				<div style={{ marginBottom: '10px' }}>
					<strong>Name:</strong>
					<div>{user.name}</div>
				</div>
				<div style={{ marginBottom: '10px' }}>
					<strong>Surname:</strong>
					<div>{user.surname}</div>
				</div>
				<div style={{ marginBottom: '10px' }}>
					<strong>Username:</strong>
					<div>{user.username}</div>
				</div>
				<div style={{ marginBottom: '10px' }}>
					<strong>Email:</strong>
					<div>{user.email}</div>
				</div>

				<div style={{ marginTop: '20px', textAlign: 'center' }}>
					<LogoutButton setLogged={setLogged}></LogoutButton>
				</div>

				<div style={{ display: 'flex', justifyContent: 'center', gap: '20px', marginTop: '20px' }}>
					<DeleteUserButton user={user} setLogged={setLogged} style={{ width: '200px' }} />
					<ModifierUserButton style={{ width: '200px' }} />
				</div>
			</div>
		</div>
	);
}

export default UserProfilePage;
