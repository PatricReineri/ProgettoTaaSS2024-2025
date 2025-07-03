import { url } from '../utils/utils';

const galleryUrl = url === 'localhost' ? `${url}:8085` : `${url}/api/galleries`;

export function getImages(eventID, pageNumber) {
	return fetch(
		`http://${galleryUrl}/gallery/getGallery/${eventID}/${pageNumber}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}

export function getImagesPopular(eventID, pageNumber) {
	return fetch(
		`http://${galleryUrl}/gallery/getGalleryPopular/${eventID}/${pageNumber}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
