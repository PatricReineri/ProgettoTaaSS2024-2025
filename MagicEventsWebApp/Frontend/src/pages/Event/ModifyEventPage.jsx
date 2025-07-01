import { faClose, faGamepad, faImages, faMapMarker } from '@fortawesome/free-solid-svg-icons';
import Button from '../../components/buttons/Button';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getEvent, getEventService, updatePartecipants, updateAdmins, removePartecipant } from '../../api/eventAPI';
import ErrorContainer from '../../components/Error/ErrorContainer';
import Input from '../../components/inputs/Input';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const ModifyEventPage = () => {
    const { eventId } = useParams();
	const [event, setEvent] = useState(null);
	const [eventServices, setEventServices] = useState(null);
    const [partecipantInput, setPartecipantInput] = useState('');
    const [lat, setLat] = useState(0);
    const [lng, setLng] = useState(0);
	const [eventDetail, setEventDetail] = useState({
		participants: [],
		admins: [],
	});

    const [message, setMessage] = useState(null);
	const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchAPI() {
            const res = await getEvent(eventId);
            if (!res.ok) {
                setEvent(null);
                return;
            }
            const data = await res.json();
            console.log(data);
            setEvent(data);
            if (data.location) {
                const coordinates = data.location.split('-');
                setLat(Number(coordinates[0]));
                setLng(Number(coordinates[1]));
            }
        }

        async function fetchAPIServices() {
            const res = await getEventService(eventId);
            if (!res.ok) {
                setEvent(null);
                return;
            }
            const data = await res.json();
            console.log(data);
            setEventServices(data);
        }

        fetchAPI();

        fetchAPIServices();
    }, [eventId]);

    return !event ? (
		<ErrorContainer errorMessage={'Nessun evento trovato'} to="/home" />
	) : (
        <div className="flex flex-col gap-1 h-full">
            <h1>Partecipanti</h1>
            <div className="flex flex-auto flex-col gap-1 overflow-y-auto bg-[#505458] p-1  rounded-md">
                {event.partecipants.map((p) => (
                    <div className="p-2 flex flex-row items-center justify-between px-8 bg-[#363540]/75 text-[#E8F2FC] rounded-full text-center ">
                        <p className="p-2">{p}</p>
                        <Button
                            onClick={async () => {
                                setError(null);
                                setMessage(null);
                                try {
                                    const res = await removePartecipant(eventId, p);
                                    if(res === 'Error' || res.status != 200){
                                        setError('Errore durante la cancellazione :(');
                                    }else{
                                        setMessage('Modifica riuscita');
                                    }
                                } catch (err) {
                                    setError(err.message);
                                }
                            }}
                            link
                            custom="cursor-pointer"
                            text={<FontAwesomeIcon icon={faClose} />}
                        ></Button>
                    </div>
                ))}
            </div>
            <div className="text-[#363540] border-2 border-[#363540] p-2 bg-[#e4dcefb7] flex-auto rounded-md  gap-1 flex flex-col">
                <Input
                    onEnterPress={() => {
                        setEventDetail((prev) => ({ ...prev, participants: [...prev.participants, partecipantInput] }));
                        setPartecipantInput('');
                    }}
                    onChange={(e) => setPartecipantInput(e.target.value)}
                    value={partecipantInput}
                    customClass="bg-[#363540] text-[#E8F2FC]"
                    name="email utente da invitare"
                />
                <div className=" h-[19rem] flex flex-col gap-1 overflow-y-auto">
                    {eventDetail.participants.length === 0 ? <p className="text-center">Nessun utente aggiunto</p> : ''}
                    {eventDetail.participants.map((item) => (
                        <div className="p-2 flex flex-row items-center justify-between px-8 bg-[#363540]/75 text-[#E8F2FC] rounded-full text-center ">
                            <p>{item}</p>
                            <Button
                                onClick={() => {
                                    setEventDetail((prev) => ({
                                        ...prev,
                                        participants: prev.participants.filter((p) => p !== item),
                                    }));
                                }}
                                link
                                custom="cursor-pointer"
                                text={<FontAwesomeIcon icon={faClose} />}
                            ></Button>
                        </div>
                    ))}
                </div>
                <Button 
					text="Aggiungi partecipanti" 
					onClick={async () => {
                        setError(null);
		                setMessage(null);
						try {
							const res = await updatePartecipants(eventId, eventDetail.participants);
							if(res === 'Error' || res.status != 200){
                                setError('Errore durante la modifica dei partecipanti :(');
                            }else{
                                setMessage('Modifica riuscita');
                            }
						} catch (err) {
							setError(err.message);
						}
				}}>
				</Button>
                {message && <p className="text-green-600 text-sm">{message}</p>}
				{error && <p className="text-red-600 text-sm">{error}</p>}
            </div>
        </div>
    )
};

export default ModifyEventPage;