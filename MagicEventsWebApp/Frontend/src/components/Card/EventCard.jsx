import React from "react";
import Button from "../buttons/Button";
import clsx from "clsx";
import { useNavigate } from 'react-router-dom';
import { getEventId } from "../../api/eventAPI";

const EventCard = ({
    localDataTime,
    day,
    month,
    eventName,
    eventImage,
    time,
    location,
    description
}) => {
  const navigate = useNavigate();
  
  return (
    <div
      onClick={async ()=> {
            try {
                const res = await getEventId(eventName, localDataTime)
                const id = await res.json();
                navigate(`/${id}`)
            }catch (err) {
                console.error('Error contacting server:', err);
            }
        }
      }
      className={clsx(
        "flex flex-col rounded-xl p-4 mb-6 shadow-md bg-white",
        "hover:shadow-lg transition-shadow duration-300"
      )}
    >
        {/* Data */}
        <div className="text-sm font-bold text-gray-600 mb-2">
        {month} {day}
        </div>

        {/* Contenuto */}
        <div className="flex items-center gap-4">
        {/* Immagine */}
        <img
            src={eventImage}
            alt={eventName}
            className="w-16 h-16 object-cover rounded-lg"
        />

        {/* Info Evento */}
        <div className="flex flex-col">
            <h3 className="text-lg font-semibold">{eventName}</h3>
            <p className="text-sm text-gray-600 mt-1">{description}</p>

            <div className="flex items-center gap-4 text-sm text-gray-500 mt-2">
            <span>🕒 {time}</span>
            <span>📍 {location}</span>
            </div>
        </div>
        </div>

        {/* Pulsante */}
        <div className="mt-4 self-end">
        <Button text="Modifica evento"></Button>
        </div>
    </div>
  );
};

export default EventCard;
