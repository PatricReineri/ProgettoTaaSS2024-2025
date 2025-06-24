import React from 'react';
import { useAuth } from '../../auth/AuthContext';
import LogoutButton from '../../components/buttons/LogoutButton';
import DeleteUserButton from '../../components/buttons/DeleteUserButton';
import ModifierUserButton from '../../components/buttons/ModifierUserButton';

function UserProfilePage() {
	const { user } = useAuth();
	if (!user) return <p style={{ textAlign: 'center' }}>User not found</p>;

	return (
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
			<h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Profilo Utente</h2>

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
				<LogoutButton></LogoutButton>
			</div>

			<div style={{ marginTop: '20px', textAlign: 'center' }}>
				<DeleteUserButton></DeleteUserButton>
			</div>

			<div style={{ marginTop: '20px', textAlign: 'center' }}>
				<ModifierUserButton></ModifierUserButton>
			</div>
		</div>
	);
}

export default UserProfilePage;
