import { useNavigate } from 'react-router-dom';
import { useState, useRef } from 'react';
import { modifyUser } from '../../api/authentication';
import { faEdit, faClose } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Input from '../../components/inputs/Input';
import Button from '../../components/buttons/Button';

function UserEditPage({ setLogged }) {
	const navigate = useNavigate();
	const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('user')));

	const [message, setMessage] = useState(null);
	const [error, setError] = useState(null);

	const handleChange = (e) => {
		const { name, value } = e.target;
		setUser((prev) => ({ ...prev, [name]: value }));
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		setError(null);
		setMessage(null);
		try {
			console.log(JSON.stringify(user))
			const res = await modifyUser(JSON.stringify(user));
			if (!res.ok) throw new Error('error for user modify operation');
			setMessage('Modifica riuscita');
			sessionStorage.setItem('user', JSON.stringify(user));
			navigate('/modifyuser');
			setLogged(false);
			setTimeout(() => {
				setLogged(true);
			}, 100);
		} catch (err) {
			setError(err.message);
		}
	};

	let img = user.profileImageUrl;
	if(img === null || !img){
		img = '/default-avatar.png'
	}

	const [editingImage, setEditingImage] = useState(false);
	const imgInput = useRef(null);

	const handleChangeImage = (e) => {
		alert('Handled');
		imageUploaded(e.target.files[0]);
	};

	const handleRemoveImage = () => {
		setUser((prev) => ({ ...prev, ['profileImageUrl']: '' }));
	};

	function imageUploaded(file) {
		let base64String = '';
		let reader = new FileReader();

		reader.onload = function () {
			base64String = reader.result.replace('data:', '').replace(/^.+,/, '');
			setUser((prev) => ({ ...prev, profileImageUrl: base64String }));
		};
		reader.readAsDataURL(file);
	}

	return (
		<div className="h-full overflow-y-auto bg-[#505458] p-4">
			<div className="max-w-md mx-auto mt-12 p-6 bg-white shadow-xl rounded-2xl">
				<h2 className="text-2xl font-bold mb-4 text-center">Modifica Profilo</h2>

				<div className="relative w-24 h-24 mx-auto">
					<img
						src={
							img.startsWith('http')
								? img.replace(/'+$/, '')
								: img.startsWith('/default-avatar.png')
								? img
								: 'data:image/*;base64,' + img
						}
						alt="Profile image"
						className="w-24 h-24 rounded-full object-cover"
					/>
					<button
						onClick={() => setEditingImage(true)}
						className="absolute top-0 right-0 bg-white bg-opacity-75 rounded-full p-1 text-[#505458] hover:text-[#363540] shadow-md"
						aria-label="Modifica immagine"
					>
						<FontAwesomeIcon icon={faEdit} />
					</button>

					<button
						onClick={handleRemoveImage} 
						className="absolute right-0 top-10 bg-white bg-opacity-75 rounded-full p-1 text-[#505458] hover:text-[#363540] shadow-md"
						aria-label="Rimuovi immagine"
					>
						<FontAwesomeIcon icon={faClose} />
					</button>
				</div>

				{editingImage && (
						<div className="py-4">
							<Input
								onChange={handleChangeImage}
								ref={imgInput}
								label={<label className="block text-sm font-medium mb-1">Modifica immagine</label>}
								name="immagine"
								type="file"
								accept="image/*"
								rigthComponent={
									<Button
									custom="!bg-transparent !hover:bg-black/50 !border-none mt-[0.15rem]"
									onClick={() => {
										imgInput.current.value = '';
										setEditingImage(false);
									}}
									text={<FontAwesomeIcon icon={faClose} />}
									/>
								}
							/>
						</div>
						)
					}

				<form onSubmit={handleSubmit} className="space-y-4">
					<div>
						<label className="block text-sm font-medium mb-1">Username</label>
						<input
							type="text"
							name="username"
							value={user.username}
							onChange={handleChange}
							className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
							required
						/>
					</div>
					<div>
						<label className="block text-sm font-medium mb-1">Email</label>
						<input
							type="email"
							name="email"
							value={user.email}
							onChange={handleChange}
							className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
							required
						/>
					</div>
					<div>
						<label className="block text-sm font-medium mb-1">Name</label>
						<input
							type="text"
							name="name"
							value={user.name}
							onChange={handleChange}
							className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
						/>
					</div>
					<div>
						<label className="block text-sm font-medium mb-1">Surname</label>
						<input
							type="text"
							name="surname"
							value={user.surname}
							onChange={handleChange}
							className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring focus:border-blue-300"
						/>
					</div>
					{message && <p className="text-green-600 text-sm">{message}</p>}
					{error && <p className="text-red-600 text-sm">{error}</p>}
					<button
						type="submit"
						className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg"
					>	
					Salva modifiche
					</button>
				</form>
			</div>
		</div>
	);
}

export default UserEditPage;
