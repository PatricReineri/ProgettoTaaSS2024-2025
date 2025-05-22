import { Map, Marker } from '@vis.gl/react-google-maps';
import { useEffect, useState } from 'react';

export default function SpikeMap({ address }) {
	const [lat, setLat] = useState(0);
	const [lng, setLng] = useState(0);
	useEffect(() => {
		if (!address) return;

		console.log(address);
		setLat(address.lat);
		setLng(address.lng);
		//setReady(address.lat + '-' + address.lng);
	}, [address, lat, lng]);

	return (
		<Map
			key={lat + '- Patric è un clown -' + lng}
			style={{ width: '400px', height: '400px' }}
			defaultCenter={{ lat: lat, lng: lng }}
			onCenterChanged={(value) => console.log('Center changed!:' + value.detail.center)}
			defaultZoom={15}
			gestureHandling={'greedy'}
			disableDefaultUI={true}
		>
			<Marker position={{ lat: lat ? lat : 0, lng: lng ? lng : 0 }}></Marker>
		</Map>
	);
}
