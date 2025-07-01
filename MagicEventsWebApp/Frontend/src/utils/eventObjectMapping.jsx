export function mapEventDTOtoCardProps(eventDTO) {
  const start = new Date(eventDTO.starting);
  const end = new Date(eventDTO.ending);

  // Estraggo giorno e mese
  const day = start.getDate().toString().padStart(2, "0");
  const month = start.toLocaleDateString("it-IT", { month: "short" }).toUpperCase();

  // Formattazione orario
  const time = `${start.getHours().toString().padStart(2, "0")}:${start.getMinutes().toString().padStart(2, "0")} - ${end.getHours().toString().padStart(2, "0")}:${end.getMinutes().toString().padStart(2, "0")}`;

  return {
    localDataTime: eventDTO.starting,
    day,
    month,
    eventName: eventDTO.title,
    eventImage: eventDTO.image,
    time,
    location: eventDTO.location,
    description: eventDTO.description
  };
}
