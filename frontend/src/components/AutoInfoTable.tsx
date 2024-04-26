import { useState, type FC, useEffect, type ChangeEventHandler, type FormEventHandler } from "react";
import type { Auto } from "../services/Autos/Autos";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit } from '@fortawesome/free-solid-svg-icons/faEdit';
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import Form from 'react-bootstrap/Form';

interface AutoInfoTableProps {
	auto: Auto;
	onChange: (auto: Auto) => void,
}

const DEFAULT_STATE = {
	editKilometraje: false,
	valueKilometraje: 0,
};

const AutoInfoTable: FC<AutoInfoTableProps> = ({ auto, onChange }) => {
	const [state, setState] = useState(DEFAULT_STATE);

	useEffect(() => {
		setState(state => {
			if(state.valueKilometraje !== auto.kilometraje){
				return { ...state, valueKilometraje: auto.kilometraje };
			}
			return state;
		});
	}, [auto]);

	const toggleEditKilometraje = () => {
		setState(state => ({ ...state, editKilometraje: !state.editKilometraje }));
	};

	const handleChangeKilometraje: ChangeEventHandler<HTMLInputElement> = (event) => {
		setState(state => ({ ...state, valueKilometraje: parseInt(event.target.value) }));
	};

	const handleSubmitEditKilometraje: FormEventHandler<HTMLFormElement> = (event) => {
		event.preventDefault();
		setState(state => ({ ...state, editKilometraje: false }));
		onChange({ ...auto, kilometraje: state.valueKilometraje });
	};

	return (
		<Table bordered striped className="mb-0">
			<tbody>
				<tr>
					<td width="20%">Patente</td>
					<td>{auto.patente}</td>
					<td width="120"></td>
				</tr>
				<tr>
					<td>Marca</td>
					<td>{auto.marca.nombre}</td>
					<td></td>
				</tr>
				<tr>
					<td>Modelo</td>
					<td>{auto.modelo}</td>
					<td></td>
				</tr>
				<tr>
					<td>A&ntilde;o</td>
					<td>{auto.anio}</td>
					<td></td>
				</tr>
				<tr>
					<td>Motor</td>
					<td>{auto.motor}</td>
					<td></td>
				</tr>
				<tr>
					<td>Tipo</td>
					<td>{auto.tipo}</td>
					<td></td>
				</tr>
				<tr>
					<td>Asientos</td>
					<td>{auto.asientos}</td>
					<td></td>
				</tr>
				<tr>
					<td>Kilometraje</td>
					<td>
						{state.editKilometraje ? (
							<Form onSubmit={handleSubmitEditKilometraje}>
								<Form.Control
									type="number"
									autoFocus
									placeholder="Kilometraje"
									value={state.valueKilometraje}
									onChange={handleChangeKilometraje}
								/>
							</Form>
						):(
							<span>{state.valueKilometraje}</span>
						)}
					</td>
					<td className="text-center">
						<Button size="sm" onClick={toggleEditKilometraje}>
							<FontAwesomeIcon icon={faEdit} /> {state.editKilometraje ? "Guardar" : "Editar"}
						</Button>
					</td>
				</tr>
			</tbody>
		</Table>
	)
};

export default AutoInfoTable;