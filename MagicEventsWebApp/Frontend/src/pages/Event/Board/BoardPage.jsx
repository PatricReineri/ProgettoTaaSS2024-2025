import { useEffect, useState } from 'react';
import Button from '../../../components/buttons/Button';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

import MexssageList from '../../../components/Lists/List';

const BoardPage = () => {
	let board = document.getElementById('board');
	const [messages, setMessages] = useState([]);
	const [message, setMessage] = useState('');
	const [stompClient, setStompClient] = useState(null);

	useEffect(() => {
		const socket = new SockJS('http://localhost:8081/chat');
		const client = Stomp.over(socket);

		console.log('Connecting...');

		client.connect(
			{},
			() => {
				console.log('Subscibing...');

				client.subscribe('/topic/chat/1', (message) => {
					const reciviedMessage = JSON.parse(message.body);
					setMessages((prev) => [...prev, reciviedMessage]);
				});
			},
			() => {
				console.log('Errore');
			}
		);

		setStompClient(client);

		return () => {
			client.disconnect();
		};
	}, []);

	const sendMessage = () => {
		const chatMessage = {
			id: 1,
			content:
				'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
			username: 'Alix99',
			time: '12:50 22/05/25',
		};

		stompClient.send('/app/chat/sendmessage/1', {}, JSON.stringify(chatMessage));
	};

	return (
		<div className="h-full bg-[#363540] relative bg-gradient-to-r  to-[#363540]  from-[#E4DCEF] flex flex-row ">
			<div className="w-64 mt-4 shadow-2xl h-fit rounded-r-2xl bg-[#363540] text-[#E4DCEF] p-4 max-sm:hidden ">
				<h1 className="font-bold">Titolo Bacheca</h1>
				<p className="text-xs">
					Descizione della bacheca, piu o meno abbastanza lunga, forse anche più lunga, ma non riesco a scrivere di piu,
					quindi concludo qua
				</p>
				<Button text="Aggiungi messaggio" onClick={() => sendMessage()}></Button>
			</div>
			<MexssageList messages={messages} />
		</div>
	);
};

export default BoardPage;
