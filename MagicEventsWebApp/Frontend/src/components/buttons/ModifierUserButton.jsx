import { useNavigate } from 'react-router-dom';

function LogoutButton() {
	const navigate = useNavigate();
	return (
		<button onClick={ () => navigate('/modifyuser') } style={{
						padding: '10px 20px',
						backgroundColor: '#28a745',
						color: 'white',
						border: 'none',
						borderRadius: '4px',
						cursor: 'pointer',
					}}
		>
			Modifica Account
		</button>
	);
}

export default LogoutButton;