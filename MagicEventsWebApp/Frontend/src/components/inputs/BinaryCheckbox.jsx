import { useEffect, useState } from 'react';
import Input from './Input';

const BinaryRadio = ({ question, onChange, labels = ['SI', 'NO'] }) => {
	const [inputValue, setInputValue] = useState(null);

	useEffect(() => {
		onChange(inputValue);
	}, [inputValue]);

	return (
		<div className="text-[#E8F2FC] flex gap-4  items-center justify-between ">
			<h1 className="font-semibold bg-[#363540] p-2 px-8 rounded-md ">{question}</h1>
			<div className=" flex flex-row gap-2 items-center ">
				<Input onChange={(e) => setInputValue(true)} name={question} type="radio" label={labels[0]} />
				<Input onChange={(e) => setInputValue(false)} name={question} type="radio" label={labels[1]} />
			</div>
		</div>
	);
};

export default BinaryRadio;
