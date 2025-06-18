import Button from '../../../components/buttons/Button';
import MessageCard from '../../../components/Card/MessageCard';
import Input from '../../../components/inputs/Input';

const BoardPage = () => {
	const board = document.getElementById('board');

	return (
		<div className="h-full bg-[#363540] relative bg-gradient-to-r  to-[#363540]  from-[#E4DCEF] flex flex-row ">
			<div className="w-64 mt-4 shadow-2xl h-fit rounded-r-2xl bg-[#363540] text-[#E4DCEF] p-4 max-sm:hidden ">
				<h1 className="font-bold">Titolo Bacheca</h1>
				<p className="text-xs">
					Descizione della bacheca, piu o meno abbastanza lunga, forse anche più lunga, ma non riesco a scrivere di piu,
					quindi concludo qua
				</p>
			</div>
			<div id="board" className="flex p-8 flex-col gap-4  overflow-y-auto flex-auto pb-24  ">
				<div className="absolute border-1 border-[#363540]  bottom-4 right-4 flex bg-[#505458]/50 backdrop-blur-2xl w-[calc(100%-18rem)] rounded-full shadow-2xl  h-fit p-2">
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
				<MessageCard
					message={{
						id: 1,
						content:
							'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
						username: 'Alix99',
						time: '12:50 22/05/25',
					}}
				/>
				<MessageCard
					message={{
						id: 1,
						content:
							'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
						username: 'Alix99',
						time: '12:50 22/05/25',
					}}
					isSendbyMe={true}
				/>
				<MessageCard
					message={{
						id: 1,
						content:
							'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
						username: 'Alix99',
						time: '12:50 22/05/25',
					}}
				/>
				<MessageCard
					message={{
						id: 1,
						content:
							'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
						username: 'Alix99',
						time: '12:50 22/05/25',
					}}
				/>
				<MessageCard
					message={{
						id: 1,
						content:
							'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
						username: 'Alix99',
						time: '12:50 22/05/25',
					}}
					isSendbyMe
				/>
			</div>
		</div>
	);
};

export default BoardPage;
