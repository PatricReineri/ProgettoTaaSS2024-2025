import { useEffect, useState } from 'react';
import ImageCard from '../imagesComponent/ImageCard';

const ImageList = ({
	onDelete = (image) => alert('delete: ', image.id),
	onLoadMore = () => alert('load more; '),
	onLike = (image) => alert('like: ', image.id),
	displayOnloadMore = true,
	images,
}) => {
	const items = images;
	const listItems = items.map((mex, index) => <ImageCard onLike={onLike} key={index} mex={mex}></ImageCard>);

	return (
		<div className="p-2 flex flex-col  !h-fit ">
			<h1 className="font-bold text-xl">Popolari</h1>
			{listItems.length > 0 ? (
				<div className=" snap-x w-full  flex flex-row gap-2 p-4 overflow-x-auto   ">{listItems}</div>
			) : (
				<div className=" bg-[#505458]/50 backdrop-blur-4xl snap-x w-full   text-[#E8F2FC] rounded-md  flex flex-row gap-2 p-2 overflow-x-auto ">
					Nessauna immaggine popolare
				</div>
			)}
		</div>
	);
};

export default ImageList;
