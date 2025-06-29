import { faClose, faMap, faUsers } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import clsx from 'clsx';
import Button from '../buttons/Button';
import { useState } from 'react';

const Menu = ({ open, onClose, onOpen, direction = 'right', tabs }) => {
	const [content, setContent] = useState(null);

	return open ? (
		<div
			className={clsx({
				' absolute flex flex-col p-2 gap-2  top-0  h-full bg-[#363540] min-w-[10rem] border border-[#505458] shadow-xl ': true,
				hidden: content === null,
				'left-0': direction === 'left',
				'right-0': direction !== 'left',
			})}
		>
			<Button
				onClick={onClose}
				text={<FontAwesomeIcon className="text-2xl cursor-pointer " icon={faClose} />}
				link
			></Button>

			{content}
		</div>
	) : (
		<div
			className={clsx({
				' absolute rounded-full flex flex-row items-center justify-evenly p-2 gap-4  top-6  min-h-[3rem] bg-[#363540]  border border-[#505458] shadow-xl ': true,
				'left-6': direction === 'left',
				'right-6': direction !== 'left',
			})}
		>
			{tabs.map((item) => (
				<Button
					custom="hover:bg-[#505458] p-2 rounded-full "
					link
					text={<FontAwesomeIcon className="text-2xl" icon={item.action} />}
					onClick={() => {
						setContent(item.content);
						onOpen();
					}}
				></Button>
			))}
		</div>
	);
};

export default Menu;
