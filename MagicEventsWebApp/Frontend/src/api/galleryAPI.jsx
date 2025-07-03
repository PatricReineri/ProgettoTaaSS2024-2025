import { url } from '../utils/utils';

export function getImages(eventID, pageNumber) {
	return fetch(
		`http://${url}:8085/gallery/getGallery/${eventID}/${pageNumber}?userMagicEventsTag=${
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
		`http://${url}:8085/gallery/getGalleryPopular/${eventID}/${pageNumber}?userMagicEventsTag=${
			JSON.parse(sessionStorage.getItem('user')).magicEventTag
		}`,
		{
			method: 'GET',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		}
	);
}
