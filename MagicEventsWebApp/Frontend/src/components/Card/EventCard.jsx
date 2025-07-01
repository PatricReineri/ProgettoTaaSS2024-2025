import React from 'react';
import Button from '../buttons/Button';
import clsx from 'clsx';
import { useNavigate } from 'react-router-dom';
import { deleteEvent, getEventId } from '../../api/eventAPI';
import { useCoordinatesConverter } from '../../utils/coordinatesConverter';

const EventCard = ({ localDataTime, day, month, eventName, time, location, description }) => {
	const navigate = useNavigate();
	const address = useCoordinatesConverter(location);

	return (
		<div
			onClick={async () => {
				try {
					const res = await getEventId(eventName, localDataTime);
					const id = await res.text();
					navigate(`/${id[1]}`);
				} catch (err) {
					console.error('Error contacting server:', err);
				}
			}}
			className={clsx(
				'flex flex-col rounded-xl p-4 mb-6 shadow-md bg-white',
				'hover:shadow-lg transition-shadow duration-300'
			)}
		>
			{/* Data */}
			<div className="text-sm font-bold text-gray-600 mb-2">
				{month} {day}
			</div>

			{/* Contenuto */}
			<div className="flex items-center gap-4">

				{/* Info Evento */}
				<div className="flex flex-col">
					<h3 className="text-lg font-semibold">{eventName}</h3>
					<p className="text-sm text-gray-600 mt-1">{description}</p>

					<div className="flex items-center gap-4 text-sm text-gray-500 mt-2">
						<span>🕒 {time}</span>
						{address}
					</div>
				</div>
			</div>

			{/* Pulsante */}
			<div className="mt-4 self-end flex flex-row space-x-1">
				<Button 
					text="Modifica evento" 
					onClick={async (e) => {
						e.stopPropagation();
						try {
							const res = await getEventId(eventName, localDataTime);
							const id = await res.text();
							navigate(`/modifyevent/${id[1]}`);
						} catch (err) {
							console.error('Error contacting server:', err);
						}
				}}>
				</Button>
				<Button 
					text="Elimina evento" 
					onClick={async (e) => {
						e.stopPropagation();
						try {
							const res = await getEventId(eventName, localDataTime);
							const id = await res.text();
							await deleteEvent(id[1])
						} catch (err) {
							console.error('Error contacting server:', err);
						}
				}}>
				</Button>
			</div>
		</div>
	);
};

export default EventCard;
