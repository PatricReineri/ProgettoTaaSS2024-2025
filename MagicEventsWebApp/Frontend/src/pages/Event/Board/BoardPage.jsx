import { useEffect, useRef, useState } from 'react';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

import MexssageList from '../../../components/Lists/List';
import { getMessages } from '../../../api/boardApi';
import { useIntersection } from '../../../util/hook';

const BoardPage = ({ eventID }) => {
	const [messages, setMessages] = useState([]);
	const [title, setTitle] = useState('');
	const [description, setDescription] = useState('');
	const board = document.getElementById('board');
	const board2 = document.getElementById('board2');
	const [stompClient, setStompClient] = useState(null);
	const [connected, setConnected] = useState(false);

	const [page, setPage] = useState(0);
	const [messageFinish, setMessageFinish] = useState(false);

	async function loadMore() {
		if (messageFinish) {
			return;
		}
		setPage((prev) => prev + 1);

		console.log(page);

		let res = await getMessages(eventID, page);

		if (!res.ok) throw new Error('Error on load more messages');
		const data = await res.json();
		console.log(data);
		if (data.messages.length === 0) {
			setMessageFinish(true);
			return;
		}
		setMessages((prev) => [...prev, ...data.messages]);
	}

	useEffect(() => {
		if (board === null || board2 == null) {
			return;
		}

		setTimeout(() => {
			board?.scrollTo({ left: 0, top: board.scrollHeight, behavior: 'smooth' });
			board2?.scrollTo({ left: 0, top: board2.scrollHeight, behavior: 'smooth' });
		}, 500);
	}, [board, board?.scrollHeight, board2, board2?.scrollHeight]);

	useEffect(() => {
		async function fetchAPI() {
			let res = await getMessages(eventID, 0);

			if (!res.ok) throw new Error('Credential invalid');
			setPage(1);
			const data = await res.json();
			console.log(data);
			setTitle(data.title);
			setDescription(data.description);
			setMessages(data.messages);

			//sessionStorage.setItem('user', JSON.stringify({ username: 'Xandro01' }));
		}

		if (!eventID) return;

		connect();
		fetchAPI();

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
						setMessages((prev) => [receivedMessage, ...prev.filter((item) => !(hash(item) === hash(receivedMessage)))]);
					} catch (error) {
						console.error('Error parsing message:', error);
					}
				});

				// const deleteSubscription = client.subscribe(`/topic/chat/deleteMessage/${eventID}`, (message) => {
				// 	const deletedMessage = JSON.parse(message.body);
				// 	console.log('Messaggio cancellato:', deletedMessage);
				// 	var hash = require('object-hash');
				// 	setMessages((prev) => prev.filter((item) => !(hash(item) === hash(deletedMessage))));
				// });

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

	const deleteMessage = (mex) => {
		if (!stompClient || !connected || !stompClient.connected) {
			console.log('Not connected to WebSocket');
			return;
		}

		for (let index = 0; index < sessionStorage.length; index++) {
			const element = sessionStorage.key(index);
			console.log(element);
		}
	};

	const sendMessage = (content) => {
		if (!stompClient || !connected || !stompClient.connected) {
			console.log('Not connected to WebSocket');
			return;
		}

		let user = JSON.parse(sessionStorage.getItem('user'));

		const chatMessage = {
			content: content,
			username: user.username,
			dateTime: new Date().toISOString(),
			eventID: eventID,
		};

		console.log('Sending message:', chatMessage);

		try {
			stompClient.send(`/app/chat/sendMessage/${eventID}`, {}, JSON.stringify(chatMessage));
		} catch (error) {
			console.log('Error sending message:', error);
		}
	};

	return (
		<div className="h-full bg-[#363540] relative bg-gradient-to-r  to-[#363540]  from-[#E4DCEF] flex flex-row ">
			<div className="w-64 mt-4 shadow-2xl h-fit rounded-r-2xl bg-[#363540] text-[#E4DCEF] p-4 max-sm:hidden ">
				<h1 className="font-bold">{title}</h1>
				<p className="text-xs">{description}</p>
			</div>
			<MexssageList
				displayOnloadMore={!messageFinish}
				onLoadMore={loadMore}
				onSend={(value) => sendMessage(value)}
				messages={messages}
				onDelete={deleteMessage}
			/>
		</div>
	);
};

export default BoardPage;
