import Button from '../buttons/Button';
import MessageCard from '../Card/MessageCard';
import Input from '../inputs/Input';

const MexssageList = ({ messages }) => {
	if (messages === undefined || messages.length === 0) {
		return <p className="p-6 text-current/30 font-bold text-center w-full ">No message in board</p>;
	}
	const board = document.getElementById('board');
	const items = messages;
	const listItems = items.map((mex) => <MessageCard message={mex} />);

	return (
		<div id="board" className="flex p-6  flex-col    overflow-y-auto flex-auto pb-24  ">
			<div className="absolute border-1 border-[#363540]  bottom-4 right-4 flex bg-[#505458]/50 backdrop-blur-2xl sm:w-[calc(100%-18rem)] w-[calc(100%-2rem)] rounded-full shadow-2xl  h-fit p-2">
				<Input
					name="message"
					customClassContainer="flex-auto"
					customClass="!border-none !outline-none !ring-none text-[#E4DCEF]"
				/>
				<Button
					onClick={() => board.scrollTo({ left: 0, top: board.scrollHeight, behavior: 'smooth' })}
					custom="!rounded-full"
					text="Invia"
				/>
			</div>
			<div>{listItems}</div>
		</div>
	);
};

export default MexssageList;
