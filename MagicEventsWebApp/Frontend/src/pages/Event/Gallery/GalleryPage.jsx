import { useEffect, useRef, useState } from 'react';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

import { useParams } from 'react-router-dom';
import { getImages, getImagesPopular } from '../../../api/galleryAPI';
import { send, subscribe } from '../../../util/WebSocket';
import ImageList from '../../../components/Lists/ImageList';
import ImageGrid from '../../../components/imagesComponent/ImageGrid';
import Button from '../../../components/buttons/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import ImageDropImage from '../../../components/popup/ImageDropImage';

const GalleryPage = () => {
	const [images, setImages] = useState([]);
	const [imagesPopular, setImagesPopular] = useState([]);
	const [title, setTitle] = useState('');
	const gallery = document.getElementById('gallery');
	const gallery2 = document.getElementById('gallery2');
	const [stompClient, setStompClient] = useState(null);
	const [connected, setConnected] = useState(false);

	const [page, setPage] = useState(0);
	const [pagep, setPagep] = useState(0);
	const [messageFinish, setMessageFinish] = useState(false);
	const [messageFinishp, setMessageFinishp] = useState(false);

	const { eventId } = useParams();

	async function loadMore() {
		if (messageFinish) {
			return;
		}
		setPage((prev) => prev + 1);
		let res = await getImages(eventId, page);
		if (!res.ok) throw new Error('Error on load more images');
		const data = await res.json();
		if (data.images.length === 0) {
			setMessageFinish(true);
			return;
		}
		setImages((prev) => [...prev, ...data.images]);
	}

	async function loadMorep() {
		if (messageFinishp) {
			return;
		}
		setPagep((prev) => prev + 1);
		let res = await getImagesPopular(eventId, page);
		if (!res.ok) throw new Error('Error on load more popular images');
		const data = await res.json();
		if (data.images.length === 0) {
			setMessageFinishp(true);
			return;
		}
		setImagesPopular((prev) => [...prev, ...data.images]);
	}

	useEffect(() => {
		if (gallery === null || gallery2 == null) {
			return;
		}

		setTimeout(() => {
			gallery?.scrollTo({ left: 0, top: gallery.scrollHeight, behavior: 'smooth' });
			gallery2?.scrollTo({ left: 0, top: gallery2.scrollHeight, behavior: 'smooth' });
		}, 500);
	}, [gallery, gallery?.scrollHeight, gallery2, gallery2?.scrollHeight]);

	useEffect(() => {
		async function fetchAPI() {
			let res = await getImages(eventId, 0);
			let resp = await getImagesPopular(eventId, 0);

			if (!res.ok) throw new Error('Error on load images');
			if (!resp.ok) throw new Error('Error on load popular images');
			setPage(1);
			setPagep(1);
			const data = await res.json();
			const datap = await resp.json();
			setTitle(data.title);
			setImages(data.images);
			setImagesPopular(datap.images);
		}

		if (!eventId) return;

		connect();
		fetchAPI();

		// Cleanup on unmount
		return () => {
			if (stompClient) {
				stompClient.disconnect();
			}
		};
	}, [eventId]);

	const connect = () => {
		if (!eventId || connected) return;
		setConnected(true);
		const socket = new SockJS('http://localhost:8085/gallery');
		const client = Stomp.over(socket);
		// Disable debug output (optional)
		client.debug = null;
		client.connect(
			{},
			(frame) => {
				setStompClient(client);
				setConnected(true);
				// Subscribe to the topic with the correct path format
				subscribe(client, `/topic/gallery/${eventId}`, (receivedImage, hash) => {
					setImages((prev) => [receivedImage, ...prev.filter((item) => !(hash(item) === hash(receivedImage)))]);
				});
				subscribe(client, `/topic/gallery/deleteImage/${eventId}`, (deletedMessage, hash) => {
					setImages((prev) => prev.filter((item) => !(hash(item) === hash(deletedMessage))));
				});
				subscribe(client, `/topic/gallery/imageLike/${eventId}`, (receivedImageLike, hash) => {
					console.log('Subscribe!!!');

					setImages((prev) =>
						prev.map((item) =>
							item.id === receivedImageLike.imageID
								? {
										...item,
										likes: receivedImageLike.likedCount,
										userLike: receivedImageLike.like,
								  }
								: item
						)
					);
					setImagesPopular((prev) =>
						prev.map((item) =>
							item.id === receivedImageLike.imageID
								? {
										...item,
										likes: receivedImageLike.likedCount,
										userLike: receivedImageLike.like,
								  }
								: item
						)
					);
				});
				client.onclose = () => {
					console.log('Client disconesso');
				};
			},
			(error) => {
				setConnected(false);
			}
		);
	};

	const deleteImage = (mex) => {
		if (!stompClient || !connected || !stompClient.connected) {
			console.log('Not connected to WebSocket');
			return;
		}
		let user = JSON.parse(sessionStorage.getItem('user'));
		const galleryImage = {
			deletedBy: user.username,
			eventID: eventId,
			imageID: mex.id,
			magiceventstag: user.magicEventTag.toString(),
		};
		try {
			send(stompClient, `/app/gallery/deleteImage/${eventId}`, galleryImage);
		} catch (error) {
			console.log('Error sending message:', error);
		}
	};

	const sendImage = (title, image) => {
		if (!stompClient || !connected || !stompClient.connected) {
			console.log('Not connected to WebSocket');
			return;
		}

		let user = JSON.parse(sessionStorage.getItem('user'));
		const galleryImage = {
			title: title,
			uploadedBy: user.username,
			base64Image: image,
			dateTime: new Date().toISOString(),
			eventID: eventId,
			magiceventstag: user.magicEventTag.toString(),
		};
		try {
			send(stompClient, `/app/gallery/sendImage/${eventId}`, galleryImage);
		} catch (error) {
			console.log('Error sending message:', error);
		}
	};

	const likeImage = (image) => {
		if (!stompClient || !connected || !stompClient.connected) {
			console.log('Not connected to WebSocket');
			return;
		}

		let user = JSON.parse(sessionStorage.getItem('user'));
		const galleryImage = {
			userMagicEventsTag: user.magicEventTag.toString(),
			like: !image.userLike,
			imageID: image.id,
			eventID: eventId,
			likedCount: 0,
		};
		try {
			send(stompClient, `/app/gallery/imageLike/${eventId}`, galleryImage);
		} catch (error) {
			console.log('Error sending message:', error);
		}
	};

	return (
		<div className="h-full  bg-[#363540]  bg-gradient-to-r   p-2 to-[#363540] gap-1 from-[#E4DCEF] flex flex-col overflow-y-auto ">
			<div className=" mt-4  h-fit rounded-r-2xl text-[#363540] p-4 max-sm:hidden ">
				<h1 className="font-bold text-2xl">{title ? title : 'Nessun titolo'}</h1>
			</div>
			<ImageList onLike={(img) => likeImage(img)} images={imagesPopular} />
			<h1>Galleria</h1>
			<ImageGrid onLike={(img) => likeImage(img)} images={images} />
			<Button
				custom="absolute right-4 bottom-4  shadow-[20rem] !rounded-full "
				text={<FontAwesomeIcon icon={faPlus} />}
			></Button>
			<ImageDropImage onSend={(title, image) => sendImage(title, image)} />
		</div>
	);
};

export default GalleryPage;
