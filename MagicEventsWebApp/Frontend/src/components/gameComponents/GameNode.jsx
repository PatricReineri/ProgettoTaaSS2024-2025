import { useState } from 'react';
import Button from '../buttons/Button';

const GameNode = ({ startingNode }) => {
	const [node, setNode] = useState(startingNode);

	return node ? (
		<div className="flex z-100 flex-col gap-4  h-full items-center justify-center ">
			{node.leftNode && node.rightNode ? (
				<>
					{!node.leftNode?.leftNode || !node.leftNode?.leftNode?.leftNode ? (
						<p className=" bg-[#363540]/70 text-[#E8F2FC]  backdrop-blur-2xl p-4 rounded-md ">Ci sono quasi</p>
					) : (
						''
					)}
					<div className="flex flex-row justify-between gap-4 w-full px-4 ">
						<Button
							custom=" !p-4 !bg-[#E8F2FC] !text-[#363540] hover:!shadow-2xl hover:!shadow-[#E8F2FC]  "
							onClick={() => setNode((prev) => prev.leftNode)}
							text="SI"
						/>
						<h1 className=" bg-[#363540]/70 text-[#E8F2FC]  backdrop-blur-2xl p-4 rounded-md ">
							{node.splitFeatureQuestion}
						</h1>
						<Button
							custom=" !p-4 !bg-[#E8F2FC] !text-[#363540]  hover:!shadow-2xl hover:!shadow-[#E8F2FC] "
							onClick={() => setNode((prev) => prev.rightNode)}
							text="NO"
						/>
					</div>
				</>
			) : (
				<>
					<p className=" bg-[#363540]/70 text-[#E8F2FC]  backdrop-blur-2xl p-4 rounded-md ">
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
