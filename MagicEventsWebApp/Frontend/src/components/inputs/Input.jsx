import clsx from 'clsx';
import Button from '../buttons/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClose } from '@fortawesome/free-solid-svg-icons';

const Input = ({
	accept = '',
	ref = null,
	customClassContainer = '',
	customClass = '',
	customClassLabel = '',
	type = 'text',
	value,
	onChange,
	name,
	minLength = 0,
	required = false,
	label,
	min = 0,
	max = 130,
	onEnterPress = () => '',
	rigthComponent,
}) => {
	return (
		<div
			className={clsx({
				' relative flex flex-col space-y-1 ': true,
				[customClassContainer]: true,
				'!flex-row gap-2 p-2 items-center  ': type === 'radio',
			})}
		>
			<div className="absolute top-5 right-0">{rigthComponent}</div>
			{label ? (
				<label className={clsx({ 'text-xs font-bold ms-1': true, [customClassLabel]: true, '!m-0': type === 'radio' })}>
					{label}
				</label>
			) : (
				<></>
			)}
			<input
				min={min}
				max={max}
				accept={accept}
				ref={ref}
				onKeyDown={(e) => (e.key === 'Enter' ? onEnterPress() : '')}
				minLength={minLength}
				type={type}
				className={customClass}
				style={{ padding: 10, borderRadius: 4, border: '1px solid #ccc' }}
				name={name}
				placeholder={'Inserisci ' + name + '...'}
				value={value}
				onChange={onChange}
				required
			/>
		</div>
	);
};

export default Input;
