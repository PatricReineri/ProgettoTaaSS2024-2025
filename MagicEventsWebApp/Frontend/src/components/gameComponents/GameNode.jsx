import { useState } from 'react';
import Button from '../buttons/Button';

const GameNode = ({ startingNode }) => {
	const [node, setNode] = useState(startingNode);

	return node ? (
		<div className="flex flex-col gap-4  h-full items-center justify-center ">
			{node.leftNode && node.rightNode ? (
				<>
					<h1 className=" bg-[#363540]/70 backdrop-blur-2xl p-4 rounded-md "> {node.splitFeatureQuestion} </h1>
					{!node.leftNode?.leftNode || !node.leftNode?.leftNode?.leftNode ? (
						<p className=" bg-[#363540]/70 backdrop-blur-2xl p-4 rounded-md ">Ci sono quasi</p>
					) : (
						''
					)}
					<div className="flex flex-row gap-4">
						<Button onClick={() => setNode((prev) => prev.leftNode)} text="SI" />
						<Button onClick={() => setNode((prev) => prev.rightNode)} text="NO" />
					</div>
				</>
			) : (
				<>
					<p className=" bg-[#363540]/70 backdrop-blur-2xl p-4 rounded-md ">
						Credo che sia: <span className="font-bold">{node.splitFeatureQuestion}</span>
					</p>
					<Button onClick={() => setNode(startingNode)} text="Ricomincia" />
				</>
			)}
		</div>
	) : (
		<div className=" bg-[#363540]/70 backdrop-blur-2xl p-4 rounded-md ">Penso alla possibile risposta</div>
	);
};

export default GameNode;
