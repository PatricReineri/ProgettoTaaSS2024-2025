import clsx from 'clsx';

const MessageCard = ({ message, isSendbyMe }) => {
	return (
		<div
			className={clsx({
				'  w-fit max-w-[32rem] p-4 rounded-4xl shadow-2xs  hover:shadow-2xl shadow-[#505458] ': true,
				'bg-[#EE0E51] text-white self-end rounded-br-none': isSendbyMe,
				'bg-[#E4DCEF] text-[#363540] rounded-bl-none    ': !isSendbyMe,
			})}
		>
			{isSendbyMe ? '' : <h1 className="font-bold text-[#EE0E51]">{message.username}</h1>}
			<p className="text-xs">{message.content}</p>
			<p className="text-xs mt-2 text-end font-light text-current/50  ">{message.time}</p>
		</div>
	);
};

export default MessageCard;
