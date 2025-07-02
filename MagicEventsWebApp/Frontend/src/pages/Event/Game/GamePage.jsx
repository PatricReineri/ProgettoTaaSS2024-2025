import { useState } from 'react';
import GameNode from '../../../components/gameComponents/GameNode';
import { useEffect } from 'react';
import { getGame, isDataInGame } from '../../../api/gameAPI';
import { useNavigate, useParams } from 'react-router-dom';

const GamePage = () => {
	const { eventId } = useParams();
	const navigate = useNavigate();

	const [tree, setTree] = useState(null);

	useEffect(() => {
		async function fetchAPI() {
			const isInGameAPI = await isDataInGame(eventId);
			const res1 = await isInGameAPI.json();

			if (!res1) {
				navigate(`/${eventId}/game/form`);
				return;
			}

			const res = await getGame(eventId);
			const res2 = await res.json();

			setTree(res2.root);
		}

		if (!eventId) {
			return;
		}

		fetchAPI();
	}, [eventId]);

	return (
		<div className="h-full overflow-y-auto bg-gradient-to-r from-[#EE0E51]  to-[#E4DCEF] relative   ">
			{tree ? (
				<div className=" gameBackground  w-full h-full">
					<GameNode startingNode={tree} />
				</div>
			) : (
				<p>Caricamento...</p>
			)}
		</div>
	);
};

export default GamePage;
