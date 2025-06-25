import { faClose, faGamepad, faImagePortrait, faImages, faMapMarker } from '@fortawesome/free-solid-svg-icons';
import Button from '../../components/buttons/Button';
import ServiceCard from '../../components/Card/ServiceCard';
import Input from '../../components/inputs/Input';
import InputArea from '../../components/inputs/InputArea';
import { useState } from 'react';
import clsx from 'clsx';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

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
		partecipants: ['alescicolone01@gmail.com', 'admin@test.it', 'test@yahoo.it'],
		admins: [],
		image: '',
		gameEnabled: false,
		galleryEnabled: false,
		boardTitle: '',
		boardDescription: '',
		gameDescription: '',
	});

	function handleChange(e) {
		const { name, value } = e.target;
		setEventDetail((prev) => ({ ...prev, [name]: value }));
	}

	const handleChangeService = (name) => {
		setEventDetail((prev) => ({ ...prev, [name]: !prev[name] }));
	};

	return (
		<div className="h-full bg-[#363540] flex flex-col ">
			<div className="flex-auto flex flex-row p-4 gap-2 ">
				<div className=" border border-[#E8F2FC]/60 text-[#E8F2FC] rounded-md p-4 gap-2 flex flex-col ">
					<h1 className="font-semibold mb-4">What event are you thinking about?</h1>
					<Input onChange={handleChange} value={eventDetail.title} label="Event Title" name="title" />

					<div className="flex flex-row gap-2 ">
						<Input
							onChange={handleChange}
							value={eventDetail.starting}
							type="date"
							customClassContainer="flex-auto"
							label="Start in"
							name="starting"
						/>
						<Input
							onChange={handleChange}
							value={eventDetail.ending}
							type="date"
							customClassContainer="flex-auto"
							label="End in"
							name="ending"
						/>
					</div>
					<InputArea
						onChange={handleChange}
						value={eventDetail.description}
						name="description"
						label="Description"
						customClass="flex-auto "
						customClassContainer="flex-auto"
					/>
				</div>
				<div className=" border border-[#E8F2FC]/60 text-[#E8F2FC] bg-[#505458] rounded-md p-4 gap-2 flex flex-col ">
					<h1 className="font-semibold mb-4">Info for board</h1>
					<Input onChange={handleChange} value={eventDetail.boardTitle} label="Board Title" name="title" />
					<InputArea
						onChange={handleChange}
						value={eventDetail.boardDescription}
						name="boardDescription"
						label="Description"
						customClass="flex-auto "
						customClassContainer="flex-auto"
					/>
				</div>
				<div className=" border border-[#363540]/60 text-[#363540] bg-[#E4DCEF] flex-auto rounded-md p-4 gap-1 flex flex-col ">
					<div className=" !mb-4 gap-2 flex flex-row justify-evenly ">
						<Button
							text="Services"
							link
							custom={clsx({
								' !font-semibold hover:bg-current/20 p-2 w-full !text-lg !text-[#363540]': true,
								'bg-current/20': tab === 'services',
							})}
							onClick={() => setTab('services')}
						/>
						<Button
							text="Users"
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
								name="Map"
							/>
							<ServiceCard
								onChange={() => handleChangeService('galleryEnabled')}
								icon={faImages}
								value={eventDetail.galleryEnabled}
								name="Gallery"
							/>
							<ServiceCard
								onChange={() => handleChangeService('gameEnabled')}
								icon={faGamepad}
								value={eventDetail.gameEnabled}
								name="Guest Game"
							/>
							<div></div>
							{mapEnabled ? (
								<Input
									onChange={handleChange}
									value={eventDetail.location}
									label="Location"
									customClass="bg-[#363540] text-[#E8F2FC]"
									name="location"
								/>
							) : (
								''
							)}
							{eventDetail.gameEnabled ? (
								<InputArea
									onChange={handleChange}
									value={eventDetail.gameDescription}
									label="Game Description"
									customClass="bg-[#363540] text-[#E8F2FC]"
									name="gameDescription"
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
								name="email for partecipating"
							/>
							<div className=" h-[19rem] flex flex-col gap-1 overflow-y-auto">
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
								name="email for admin"
							/>
							<div className=" h-[19rem] flex flex-col gap-1 overflow-y-auto">
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
				<Button text="Cancel" secondary></Button>
				<Button text="Create"></Button>
			</div>
		</div>
	);
};

export default CreationEventPage;
