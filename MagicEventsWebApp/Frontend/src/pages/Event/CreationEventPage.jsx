import { faClose, faGamepad, faImagePortrait, faImages, faMapMarker } from '@fortawesome/free-solid-svg-icons';
import Button from '../../components/buttons/Button';
import ServiceCard from '../../components/Card/ServiceCard';
import Input from '../../components/inputs/Input';
import InputArea from '../../components/inputs/InputArea';
import { useRef, useState } from 'react';
import clsx from 'clsx';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useNavigate } from 'react-router-dom';
import { createEvent } from '../../api/eventAPI';

const CreationEventPage = () => {
	const [partecipantInput, setPartecipantInput] = useState('');
	const [adminInput, setAdminInput] = useState('');
	const [mapEnabled, setMapEnabled] = useState(false);
	const [tab, setTab] = useState('users');
	const [eventDetail, setEventDetail] = useState({
		title: '',
		description: '',
		starting: '',
		ending: '',
		location: '',
		creatorEmail: JSON.parse(sessionStorage.getItem('user')).email,
		creatorMagicEventsTag: JSON.parse(sessionStorage.getItem('user')).magicEventTag,
		partecipants: [],
		admins: [],
		image: '',
		gameEnabled: false,
		galleryEnabled: false,
		boardTitle: '',
		boardDescription: '',
		gameDescription: '',
	});

	const navigate = useNavigate();

	function handleChange(e, name) {
		const { value } = e.target;
		setEventDetail((prev) => ({ ...prev, [name]: value }));
	}

	const handleChangeService = (name) => {
		setEventDetail((prev) => ({ ...prev, [name]: !prev[name] }));
	};

	const imgInput = useRef(null);
	const handleChangeImage = (e) => {
		alert('Handled');
		imageUploaded(e.target.files[0]);
	};

	const handleRemoveIMG = () => {
		imgInput.current.value = '';
		setEventDetail((prev) => ({ ...prev, image: '' }));
	};

	function imageUploaded(file) {
		let base64String = '';

		let reader = new FileReader();
		console.log('next');

		reader.onload = function () {
			base64String = reader.result.replace('data:', '').replace(/^.+,/, '');

			setEventDetail((prev) => ({ ...prev, image: base64String }));
			console.log(base64String);
		};
		reader.readAsDataURL(file);
	}

	const [error, setError] = useState('');
	async function handleCreate() {
		console.log(eventDetail);
		if (!eventDetail.title) {
			setError('Inserisci il titolo del evento');
			return;
		}
		if (!eventDetail.boardTitle) {
			setError('Inserisci il titolo della bacheca');
			return;
		}
		if (!eventDetail.starting || !eventDetail.ending) {
			setError('Inserisci la data di inizio e fine');
			return;
		}
		setError('');
		if (eventDetail.location) {
			// eventDetail.location = googleAPI()
		}
		const res = await createEvent(eventDetail);
	}

	return (
		<div className="h-full bg-[#363540] flex flex-col ">
			<div className="flex-auto flex flex-row p-4 gap-2 ">
				<div className=" border border-[#E8F2FC]/60 text-[#E8F2FC] rounded-md p-4 gap-2 flex flex-col ">
					<h1 className="font-semibold mb-4">What event are you thinking about?</h1>
					<Input
						onChange={(e) => handleChange(e, 'title')}
						value={eventDetail.title}
						label="Titolo del evento"
						name="titolo"
					/>

					<div className="flex flex-row gap-2 ">
						<Input
							onChange={(e) => handleChange(e, 'starting')}
							value={eventDetail.starting}
							type="date"
							customClassContainer="flex-auto"
							label="Inizia il"
							name="starting"
						/>
						<Input
							onChange={(e) => handleChange(e, 'ending')}
							value={eventDetail.ending}
							type="date"
							customClassContainer="flex-auto"
							label="Finisce il"
							name="ending"
						/>
					</div>
					<InputArea
						onChange={(e) => handleChange(e, 'description')}
						value={eventDetail.description}
						name="descrizione"
						label="Descrizione"
						customClass="flex-auto "
						customClassContainer="flex-auto"
					/>
					<Input
						onChange={handleChangeImage}
						ref={imgInput}
						label={'Immagine del evento'}
						name="immagine"
						type="file"
						accept="image/*"
						rigthComponent={
							<Button
								custom="!bg-transparent !hover:bg-black/50 !border-none mt-[0.15rem]"
								onClick={handleRemoveIMG}
								text={<FontAwesomeIcon icon={faClose} />}
							></Button>
						}
					></Input>
				</div>
				<div className=" border border-[#E8F2FC]/60 text-[#E8F2FC] bg-[#505458] rounded-md p-4 gap-2 flex flex-col ">
					<h1 className="font-semibold mb-4">Info for board</h1>
					<Input
						onChange={(e) => handleChange(e, 'boardTitle')}
						value={eventDetail.boardTitle}
						label="Titolo della bacheca"
						name="titolo"
					/>
					<InputArea
						onChange={(e) => handleChange(e, 'boardDescription')}
						value={eventDetail.boardDescription}
						name="Descrizione Bacheca"
						label="Descrizione"
						customClass="flex-auto "
						customClassContainer="flex-auto"
					/>
				</div>
				<div className=" border border-[#363540]/60 text-[#363540] bg-[#E4DCEF] flex-auto rounded-md p-4 gap-1 flex flex-col ">
					<div className=" !mb-4 gap-2 flex flex-row justify-evenly ">
						<Button
							text="Servizi"
							link
							custom={clsx({
								' !font-semibold hover:bg-current/20 p-2 w-full !text-lg !text-[#363540]': true,
								'bg-current/20': tab === 'services',
							})}
							onClick={() => setTab('services')}
						/>
						<Button
							text="Partecipanti"
							link
							custom={clsx({
								' !font-semibold hover:bg-current/20 p-2 w-full !text-lg !text-[#363540]': true,
								'bg-current/20': tab === 'users',
							})}
							onClick={() => setTab('users')}
						/>
						<Button
							text="Admins"
							link
							custom={clsx({
								' !font-semibold hover:bg-current/20 p-2 w-full !text-lg !text-[#363540]': true,
								'bg-current/20': tab === 'admins',
							})}
							onClick={() => setTab('admins')}
						/>
					</div>
					{tab === 'services' ? (
						<div className="text-[#363540] bg-[#e4dcefb7] flex-auto rounded-md  gap-1 flex flex-col">
							<ServiceCard
								onChange={() => setMapEnabled((prev) => !prev)}
								icon={faMapMarker}
								value={mapEnabled}
								name="Maps"
							/>
							<ServiceCard
								onChange={() => handleChangeService('galleryEnabled')}
								icon={faImages}
								value={eventDetail.galleryEnabled}
								name="Galleria"
							/>
							<ServiceCard
								onChange={() => handleChangeService('gameEnabled')}
								icon={faGamepad}
								value={eventDetail.gameEnabled}
								name="Mystery Guest Game"
							/>
							<div></div>
							{mapEnabled ? (
								<Input
									onChange={(e) => handleChange(e, 'location')}
									value={eventDetail.location}
									label="Indirizzo"
									customClass="bg-[#363540] text-[#E8F2FC]"
									name="indirizzo"
								/>
							) : (
								''
							)}
							{eventDetail.gameEnabled ? (
								<InputArea
									onChange={(e) => handleChange(e, 'gameDescription')}
									value={eventDetail.gameDescription}
									label="Descrizione del gioco"
									customClass="bg-[#363540] text-[#E8F2FC]"
									name="descrizione"
								/>
							) : (
								''
							)}{' '}
						</div>
					) : tab === 'users' ? (
						<div className="text-[#363540] border-2 border-[#363540] p-2 bg-[#e4dcefb7] flex-auto rounded-md  gap-1 flex flex-col">
							<Input
								onEnterPress={() => {
									setEventDetail((prev) => ({ ...prev, partecipants: [...prev.partecipants, partecipantInput] }));
									setPartecipantInput('');
								}}
								onChange={(e) => setPartecipantInput(e.target.value)}
								value={partecipantInput}
								customClass="bg-[#363540] text-[#E8F2FC]"
								name="Email del utenta da invitare"
							/>
							<div className=" h-[19rem] flex flex-col gap-1 overflow-y-auto">
								{eventDetail.partecipants.length === 0 ? <p className="text-center">Nessun utente invitato</p> : ''}
								{eventDetail.partecipants.map((item) => (
									<div className="p-2 flex flex-row items-center justify-between px-8 bg-[#363540]/75 text-[#E8F2FC] rounded-full text-center ">
										<p>{item}</p>

										<Button
											onClick={() => {
												setEventDetail((prev) => ({
													...prev,
													partecipants: prev.partecipants.filter((p) => p !== item),
												}));
											}}
											link
											custom="cursor-pointer"
											text={<FontAwesomeIcon icon={faClose} />}
										></Button>
									</div>
								))}
							</div>
						</div>
					) : (
						<div className="text-[#363540] border-2 border-[#363540] p-2 bg-[#e4dcefb7] flex-auto rounded-md  gap-1 flex flex-col">
							<Input
								onEnterPress={() => {
									setEventDetail((prev) => ({ ...prev, admins: [...prev.admins, adminInput] }));
									setAdminInput('');
								}}
								onChange={(e) => setAdminInput(e.target.value)}
								value={adminInput}
								customClass="bg-[#363540] text-[#E8F2FC]"
								name="Email del Admin"
							/>
							<div className=" h-[19rem] flex flex-col gap-1 overflow-y-auto">
								{eventDetail.admins.length === 0 ? (
									<p className="text-center">Nessun utente invitato come admin</p>
								) : (
									''
								)}
								{eventDetail.admins.map((item) => (
									<div className="p-2 flex flex-row items-center justify-between px-8 bg-[#363540]/75 text-[#E8F2FC] rounded-full text-center ">
										<p>{item}</p>
										<Button
											onClick={() => {
												setEventDetail((prev) => ({
													...prev,
													admins: prev.admins.filter((p) => p !== item),
												}));
											}}
											link
											custom="cursor-pointer"
											text={<FontAwesomeIcon icon={faClose} />}
										></Button>
									</div>
								))}
							</div>
						</div>
					)}
				</div>
			</div>
			<div className="flex flex-row gap-4 items-center p-4 justify-end px-8">
				<p className="text-[#EE0E51]">{error}</p>
				<Button onClick={() => navigate('/')} text="Annulla" secondary></Button>
				<Button onClick={handleCreate} text="Crea Evento"></Button>
			</div>
		</div>
	);
};

export default CreationEventPage;
