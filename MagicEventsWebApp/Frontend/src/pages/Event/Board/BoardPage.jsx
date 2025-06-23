// import { useEffect, useState } from 'react';
// import Button from '../../../components/buttons/Button';
// import Stomp from 'stompjs';
// import SockJS from 'sockjs-client';

// import MexssageList from '../../../components/Lists/List';

// const BoardPage = ({ eventID }) => {
// 	let board = document.getElementById('board');
// 	const [messages, setMessages] = useState([]);
// 	const [message, setMessage] = useState('');
// 	const [stompClient, setStompClient] = useState(null);

// 	// useEffect(() => {
// 	// 	if (!eventID) return;

// 	// 	const socket = new SockJS('http://localhost:8081/chat');
// 	// 	const client = Stomp.over(socket);

// 	// 	console.log('Connecting...');

// 	// 	client.connect(
// 	// 		{},
// 	// 		(frame) => {
// 	// 			console.log('Subscibing...', frame);

// 	// 			setStompClient(client);

// 	// 			client.subscribe(`/topic/chat/${eventID}`, (message) => {
// 	// 				const reciviedMessage = JSON.parse(message.body);
// 	// 				setMessages((prev) => [...prev, reciviedMessage]);
// 	// 			});
// 	// 		},
// 	// 		() => {
// 	// 			console.log('Errore');
// 	// 		}
// 	// 	);

// 	// 	return () => {
// 	// 		client.disconnect();
// 	// 	};
// 	// }, [eventID]);

// 	const connect = () => {
// 		if (!eventID) return;

// 		const socket = new SockJS('http://localhost:8081/chat');
// 		const client = Stomp.over(socket);

// 		console.log('Connecting...');

// 		client.connect(
// 			{},
// 			(frame) => {
// 				console.log('Subscibing...', frame);

// 				setStompClient(client);

// 				client.subscribe(`/topic/chat/${eventID}`, (message) => {
// 					const reciviedMessage = JSON.parse(message.body);
// 					setMessages((prev) => [...prev, reciviedMessage]);
// 				});
// 			},
// 			() => {
// 				console.log('Errore');
// 			}
// 		);
// 	};

// 	const sendMessage = () => {
// 		const chatMessage = {
// 			id: 1,
// 			content:
// 				'Ciao!! Bellissimo evento ci sarò sicuramente! Ma ci saranno tante persone? e quande di preciso? avrei altre domande mando sempre qui? perche ci sarebbe anhe mio fratello, per voi andrebbe bene se viene anche lui? lo invitiamo?',
// 			username: 'Alix99',
// 			time: '12:50 22/05/25',
// 		};

// 		stompClient.send('/app/chat/sendmessage/1', {}, JSON.stringify(chatMessage));
// 	};
import { useEffect, useState } from 'react';
import Button from '../../../components/buttons/Button';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

import MexssageList from '../../../components/Lists/List';
import { useAuth } from '../../../auth/AuthContext';

const BoardPage = ({ eventID }) => {
	const [messages, setMessages] = useState([]);
	const [message, setMessage] = useState('');
	const [stompClient, setStompClient] = useState(null);
	const [connected, setConnected] = useState(false);
	const { user } = useAuth();

	useEffect(() => {
		if (!eventID) return;

		connect();

		// Cleanup on unmount
		return () => {
			if (stompClient) {
				stompClient.disconnect();
			}
		};
	}, []);

	const connect = () => {
		if (!eventID || connected) return;
		setConnected(true);
		const socket = new SockJS('http://localhost:8081/chat');
		const client = Stomp.over(socket);

		// Disable debug output (optional)
		client.debug = null;

		console.log('Connecting...');

		client.connect(
			{},
			(frame) => {
				console.log('Connected:', frame);
				console.log('Client pre: ', stompClient);

				setStompClient(client);
				console.log('Client post: ', stompClient);
				setConnected(true);

				// Subscribe to the topic with the correct path format
				const subscription = client.subscribe(`/topic/chat/${eventID}`, (message) => {
					console.log('Message received:', message);
					try {
						var hash = require('object-hash');
						const receivedMessage = JSON.parse(message.body);
						setMessages((prev) => [...prev.filter((item) => !(hash(item) === hash(receivedMessage))), receivedMessage]);
					} catch (error) {
						console.error('Error parsing message:', error);
					}
				});

				client.onclose = () => {
					console.log('Client disconesso');
				};

				console.log('Subscribed to:', `/topic/chat/${eventID}`);
			},
			(error) => {
				console.log('Connection error:', error);
				setConnected(false);
			}
		);
	};

	const disconnect = () => {
		if (stompClient && connected) {
			stompClient.disconnect(() => {
				console.log('Disconnected');
				setStompClient(null);
				setConnected(false);
			});
		}
	};

	const sendMessage = (content) => {
		if (!stompClient || !connected || !stompClient.connected) {
			console.log('Not connected to WebSocket');
			return;
		}

		let user = JSON.parse(sessionStorage.getItem('user'));

		console.log(Object.getOwnPropertyNames(user));

		const chatMessage = {
			content: content,
			username: user.username,
			dateTime: new Date().toISOString(),
			eventID: eventID,
		};

		console.log('Sending message:', chatMessage);

		try {
			stompClient.send(`/app/chat/sendMessage/${eventID}`, {}, JSON.stringify(chatMessage));
			setMessage(''); // Clear input after sending
		} catch (error) {
			console.log('Error sending message:', error);
		}
	};

	return (
		<div className="h-full bg-[#363540] relative bg-gradient-to-r  to-[#363540]  from-[#E4DCEF] flex flex-row ">
			<div className="w-64 mt-4 shadow-2xl h-fit rounded-r-2xl bg-[#363540] text-[#E4DCEF] p-4 max-sm:hidden ">
				<h1 className="font-bold">Titolo Bacheca</h1>
				<p className="text-xs">
					Descizione della bacheca, piu o meno abbastanza lunga, forse anche più lunga, ma non riesco a scrivere di piu,
					quindi concludo qua
				</p>
			</div>
			<MexssageList onSend={(value) => sendMessage(value)} messages={messages} />
		</div>
	);
};

export default BoardPage;
