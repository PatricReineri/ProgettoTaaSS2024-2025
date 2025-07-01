import { useState, useEffect } from 'react';
import { useMapsLibrary } from '@vis.gl/react-google-maps';

export function useCoordinatesConverter(coordinates) {
    const [latStr, lngStr] = coordinates.split('-');
    const lat = parseFloat(latStr);
    const lng = parseFloat(lngStr);

    const geocodingAPILoaded = useMapsLibrary('places');
    const [geocodingService, setGeocodingService] = useState();
    const [address, setAddress] = useState('');

    useEffect(() => {
        if (!geocodingAPILoaded) return;
        setGeocodingService(new window.google.maps.Geocoder());
    }, [geocodingAPILoaded]);

    useEffect(() => {
        if (!geocodingService || lat == null || lng == null) return;
        const latLng = new window.google.maps.LatLng(lat, lng);
        const geocodePromise = () =>
        new Promise((resolve, reject) => {
            geocodingService.geocode({ location: latLng }, (results, status) => {
            if (status === 'OK' && results && results[0]) {
                resolve(results[0].formatted_address);
            } else {
                reject('Errore nel reverse geocoding');
            }
            });
        });

        setAddress('Caricamento...');

        geocodePromise()
        .then((formattedAddress) => {
            setAddress(formattedAddress);
        })
        .catch((error) => {
            console.error(error);
            setAddress('Errore nella codifica della location');
        });
    }, [geocodingService, lat, lng]);

    return (
        <span>📍{address}</span>
    );
}
