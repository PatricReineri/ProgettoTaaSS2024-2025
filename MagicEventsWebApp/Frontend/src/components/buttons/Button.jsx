import clsx from 'clsx';

const Button = ({ text = 'Button', onClick, link = false, custom = '' }) => {
	return (
		<button
			onClick={onClick}
			className={clsx({
				' bg-[#EE0E51] text-[#E4DCEF] p-2 px-4 rounded-md hover:shadow-2xl hover:bg-[#ee0e51d5]  ': !link,
				' text-[#E8F2FC] hover:underline font-xs': link,
				[custom]: true,
			})}
		>
			{text}
		</button>
	);
};

export default Button;

// primary: #363540
// secondary: #505458
// tertiary: #E4DCEF
// actions: #EE0E51
// on primary or secondary: #E8F2FC
// on tertiary: #363540
// on actions: #E4DCEF
// background: #E4DCEF
