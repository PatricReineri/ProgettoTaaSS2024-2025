import { useState } from 'react';
import Button from '../buttons/Button';
import MessageCard from '../Card/MessageCard';
import Input from '../inputs/Input';

const MexssageList = ({ messages, onSend = (value) => alert(value) }) => {
	const [value, setValue] = useState('');
	const board = document.getElementById('board');
	const items = messages;
	const listItems = items.map((mex, index) => (
		<MessageCard
			isSendbyMe={mex.username === JSON.parse(sessionStorage.getItem('user')).username}
			key={index}
			message={mex}
		/>
	));

	if (messages === undefined || messages.length === 0) {
		return (
			<div id="board" className="flex p-6  flex-col    overflow-y-auto flex-auto pb-24  ">
				<div className="absolute border-1 border-[#363540]  bottom-4 right-4 flex bg-[#505458]/50 backdrop-blur-2xl sm:w-[calc(100%-18rem)] w-[calc(100%-2rem)] rounded-full shadow-2xl  h-fit p-2">
					<Input
						name="message"
						value={value}
						onChange={(e) => setValue(e.target.value)}
						customClassContainer="flex-auto"
						customClass="!border-none !outline-none !ring-none text-[#E4DCEF]"
					/>
					<Button
						onClick={() => {
							board.scrollTo({ left: 0, top: board.scrollHeight, behavior: 'smooth' });
							onSend(value);
						}}
						custom="!rounded-full"
						text="Invia"
					/>
				</div>
				<p className="p-6 text-current/30 font-bold text-center w-full ">No message in board</p>;
			</div>
		);
	}

	return (
		<div id="board" className="flex p-6  flex-col     overflow-y-auto flex-auto pb-24  ">
			<div className="absolute border-1 border-[#363540]  bottom-4 right-4 flex bg-[#505458]/50 backdrop-blur-2xl sm:w-[calc(100%-18rem)] w-[calc(100%-2rem)] rounded-full shadow-2xl  h-fit p-2">
				<Input
					name="message"
					value={value}
					onChange={(e) => setValue(e.target.value)}
					customClassContainer="flex-auto"
					customClass="!border-none !outline-none !ring-none text-[#E4DCEF]"
				/>
				<Button
					onClick={() => {
						board.scrollTo({ left: 0, top: board.scrollHeight, behavior: 'smooth' });
						onSend(value);
					}}
					custom="!rounded-full"
					text="Invia"
				/>
			</div>
			<div className=" flex flex-col ">{listItems}</div>
		</div>
	);
};

export default MexssageList;
