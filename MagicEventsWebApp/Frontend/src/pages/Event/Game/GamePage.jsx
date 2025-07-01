import { useState } from 'react';
import GameNode from '../../../components/gameComponents/GameNode';

const GamePage = () => {
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

	return (
		<div className="h-full overflow-y-auto bg-[#505458] gameBackground ">
			<GameNode startingNode={tree.root} />
		</div>
	);
};

export default GamePage;
