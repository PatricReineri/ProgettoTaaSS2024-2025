import clsx from 'clsx';

const Button = ({ secondary = false, text = 'Button', onClick, link = false, custom = '', disabled = false }) => {
	return (
		<button
			disabled={disabled}
			onClick={onClick}
			className={clsx({
				' bg-[#EE0E51] border-2 border-[#EE0E51] text-[#E4DCEF] p-2 px-4 rounded-md hover:shadow-2xl hover:bg-[#ee0e51d5]  ':
					!link && !secondary,
				' text-[#E8F2FC] hover:underline text-xs w-fit': link,
				[custom]: true,
				' text-[#EE0E51]  border-2 border-[#EE0E51]  rounded-md p-2 px-4 hover:shadow-2xl hover:bg-[#ee0e51d5] hover:text-[#E4DCEF]':
					secondary,
				'cursor-pointer': true,
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
