import '../../index.js';

function XButton({ text, onClick, type, startIcon, className = 'button' }) {
	return (
		<button onClick={onClick} type={type} className={className}>
			{startIcon}
			{text}
		</button>
	);
}

export default XButton;
