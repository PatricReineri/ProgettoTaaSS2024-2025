import { APIProvider, useMapsLibrary } from '@vis.gl/react-google-maps';
import { useEffect, useState } from 'react';
import logo from './logo.svg';
import { useForm } from 'react-hook-form';
import SpikeMap from './components/Maps';

function App() {
	let result;

	useEffect(() => {
		console.log('Result from APP: ' + result);
	}, [result]);
	return (
		// old: AIzaSyACkJF2yKhiXVEEfPdWZjIbZaogCu2VxP0
		<APIProvider apiKey={'AIzaSyCsKyFbFFxOb4S8luivSquBE4Y3t36rznI'}>
			<div className="App">
				<header className="App-header">
					<Geocoding geocodingResult={result} />
					<h></h>
				</header>
			</div>
		</APIProvider>
	);
}

function Geocoding() {
	const {
		register,
		handleSubmit,
		// eslint-disable-next-line no-unused-vars
		formState: { errors },
	} = useForm();

	const geocodingAPILoaded = useMapsLibrary('geocoding');
	const [geocodingService, setGeocodingService] = useState();
	const [geocodingResult, setGeocodingResult] = useState();

	useEffect(() => {
		if (!geocodingAPILoaded) return;
		console.log('APILoaded: ' + geocodingAPILoaded);

		setGeocodingService(new window.google.maps.Geocoder());
	}, [geocodingAPILoaded]);

	const onSubmit = (data) => {
		let address = data.indirizzo;
		console.log('Service: ' + geocodingService);

		if (!geocodingService || !address) return; // errore o impossibile fare la chiamata
		console.log('Sto per chiamare geocodingService...');

		geocodingService.geocode({ address }, (results, status) => {
			console.log('Sono dentro geocodingService.geocode');

			if (results && status === 'OK') {
				setGeocodingResult(results[0]);
				if (!geocodingResult) return;
				console.log('Result:' + geocodingResult.formatted_address);
				console.log('Lat:' + geocodingResult.geometry.location.lat());
				console.log('Lng:' + geocodingResult.geometry.location.lng());
			}
		});
	};

	return (
		<div>
			<form onSubmit={handleSubmit(onSubmit)}>
				<input defaultValue="" placeholder="Inserisci indirizzo" {...register('indirizzo')}></input>
				<input type="submit" />
			</form>
			<SpikeMap
				address={{
					lat: geocodingResult ? geocodingResult.geometry.location.lat() : 0,
					lng: geocodingResult ? geocodingResult.geometry.location.lng() : 0,
				}}
			/>
		</div>
	);
}

export default App;
