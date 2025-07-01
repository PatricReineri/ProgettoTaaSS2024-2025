import React from "react";
import EventCard from "../Card/EventCard"

const EventList = ({ events = [] }) => {
  return (
    <div className="p-6">
      {events.map((event, idx) => (
        <EventCard key={idx} {...event} />
      ))}
    </div>
  );
};

export default EventList;
