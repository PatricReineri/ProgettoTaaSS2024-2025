import { useState } from 'react';
import GameNode from '../../../components/gameComponents/GameNode';
import { useEffect } from 'react';
import { getGame } from '../../../api/gameAPI';
import { useNavigate, useParams } from 'react-router-dom';

const GamePage = () => {
	const { eventId } = useParams();
	const navigate = useNavigate();

	const tree = {
		root: {
			splitFeatureQuestion: 'Hai capelli biondi?',
			leftNode: {
				splitFeatureQuestion: 'Ha più di 30 anni?',
				leftNode: {
					splitFeatureQuestion: 'Porta gli occhiali?',
					leftNode: {
						splitFeatureQuestion: 'Alessandro Borghese',
						leftNode: null,
						rightNode: null,
					},
					rightNode: {
						splitFeatureQuestion: 'Massimiliano Allegri',
						leftNode: null,
						rightNode: null,
					},
				},
				rightNode: {
					splitFeatureQuestion: 'Giovanni rana',
					leftNode: null,
					rightNode: null,
				},
			},
			rightNode: {
				splitFeatureQuestion: 'Ha meno di 10 anni?',
				leftNode: {
					splitFeatureQuestion: 'Fedez',
					leftNode: null,
					rightNode: null,
				},
				rightNode: {
					splitFeatureQuestion: 'Chiara Ferragni',
					leftNode: null,
					rightNode: null,
				},
			},
		},
		accuracy: 99.9,
		totalInstances: 0,
	};

	useEffect(() => {
		async function fetchAPI() {
			const isInGameAPI = false;

			if (!isInGameAPI) {
				navigate(`/${eventId}/game/form`);
			}

			const res = await getGame(eventId);
		}

		if (!eventId) {
			return;
		}

		fetchAPI();
	}, [eventId]);

	return (
		<div className="h-full overflow-y-auto bg-gradient-to-r from-[#EE0E51]  to-[#E4DCEF] relative   ">
			<div className=" gameBackground  w-full h-full">
				<GameNode startingNode={tree.root} />
			</div>
		</div>
	);
};

export default GamePage;
