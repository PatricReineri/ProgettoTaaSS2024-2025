import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Button from '../buttons/Button';
import { faHeart } from '@fortawesome/free-solid-svg-icons';
import clsx from 'clsx';

const ImageCard = ({ mex }) => {
	return (
		<div className="snap-start hover:scale-110 hover:border-2 hover:z-100 border-[#E8F2FC] shadow-xl relative min-w-[12rem] ">
			<div className="absolute items-center bottom-2 left-0 px-2 py-0 h-fit flex flex-row gap-4 justify-between w-full">
				<div className=" text-[#E8F2FC] max-w-[10rem] bg-[#505458]/20 p-1 px-4 rounded-md text-clip  line-clamp-1 backdrop-blur-xl ">
					{mex.title}
				</div>
				<div className="flex flex-row gap-1 items-center">
					<p className=" translate-x-0">{mex.likes}</p>
					<Button
						custom=" !rounded-full bg-transparent !border-none justify-center group flex items-center"
						text={
							<FontAwesomeIcon
								className={clsx({
									'text-2xl !px-0  group-hover:text-[#E8F2FC] ': true,
									'text-[#EE0E51]': mex.userLike,
									'text-[#505458]': !mex.userLike,
								})}
								icon={faHeart}
							/>
						}
					></Button>
				</div>
			</div>

			<div className=" h-full flex">
				<img className=" object-cover aspect-4/5  " src={mex.base64Image} alt="post"></img>
			</div>
		</div>
	);
};

export default ImageCard;
