import { useState } from 'react';
import Button from '../../../components/buttons/Button';
import BinaryRadio from '../../../components/inputs/BinaryCheckbox';
import Input from '../../../components/inputs/Input';

const GameForm = () => {
	const [formData, setFormData] = useState({
		isMen: null,
		age: 30,
		isHostFamilyMember: null,
		isHostAssociate: null,
		haveBeard: null,
		isBald: null,
		haveGlasses: null,
		haveDarkHair: null,
		userMagicEventsTag: JSON.parse(sessionStorage.getItem('user')).magicEventTag,
	});

	const handleForm = async (e) => {
		e.preventDefault();
		console.log(formData);

		const res = null;
	};

	const handleChange = (name, value) => {
		setFormData((prev) => ({ ...prev, [name]: value }));
	};

	return (
		<div className="h-full overflow-y-auto  bg-[#505458] ">
			<form onSubmit={handleForm} className="p-2 flex flex-col gap-2 h-full ">
				<BinaryRadio onChange={(v) => handleChange('isMen', v)} question={'Genere: '} labels={['Maschio', 'Femmina']} />
				<BinaryRadio
					onChange={(v) => handleChange('isHostFamilyMember', v)}
					question={"Sei un familiare del creatore/i dell'evento?"}
				/>
				<div className="text-[#E8F2FC] flex gap-4  items-center justify-between ">
					<h1 className="font-semibold bg-[#363540] text-[#E8F2FC] p-2 px-8 rounded-md ">Quanti anni hai?</h1>
					<Input
						value={formData.age}
						onChange={(e) => handleChange('age', Number(e.target.value))}
						customClassContainer="!min-w-[11rem]"
						name={'età'}
						type="number"
						min={0}
						max={130}
					/>
				</div>

				<BinaryRadio
					onChange={(v) => handleChange('isHostAssociate', v)}
					question={"Sei un collega di uno dei creatori dell'evento?"}
				/>
				<BinaryRadio onChange={(v) => handleChange('haveBeard', v)} question={'Hai la barba?'} />
				<BinaryRadio onChange={(v) => handleChange('haveGlasses', v)} question={'Porti gli occhiali?'} />
				<BinaryRadio onChange={(v) => handleChange('isBald', v)} question={'Sei pelato?'} />
				<BinaryRadio onChange={(v) => handleChange('haveDarkHair', v)} question={'Hai i capelli scuri?'} />
				<div className="flex-auto"></div>
				<Button text="Invia"></Button>
			</form>
		</div>
	);
};

export default GameForm;
