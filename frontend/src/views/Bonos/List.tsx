import { useState, type FC, useEffect, ChangeEventHandler, FormEventHandler } from "react";
import type { AxiosError } from "axios";
import { type BonoGroup, getBonosByGroup, createBonos } from "../../services/Bonos/Bonos";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { type Marca, useMarcasService } from "../../services/Marcas/Marcas";
import { numberWithCommas } from "../../utils/utils";
import Container from "react-bootstrap/Container";
import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Form from "react-bootstrap/Form";
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import Alert from "react-bootstrap/Alert";

interface State {
	loading: boolean,
	error: string|null,
	bonos: BonoGroup[],
	inputLoading: boolean,
	inputError: string|null,
	inputMarca: Marca|undefined,
	inputCantidad: number,
	inputMonto: number,
	signal: Symbol,
}

const ListBonos: FC = () => {
	const { marcas, loading:marcasLoading } = useMarcasService();
	const [state, setState] = useState<State>({
		loading: true,
		error: null,
		bonos: [],
		inputLoading: false,
		inputError: null,
		inputMarca: undefined,
		inputCantidad: 1,
		inputMonto: 1000,
		signal: Symbol(),
	});

	useEffect(() => {
		setState(state => ({ ...state, loading: true }));
		getBonosByGroup().then(bonos => {
			setState(state => ({ ...state, loading: false, error: null, bonos }));
		})
		.catch((err: AxiosError) => {
			if(err.response?.data){
				const message = (err.response?.data as any).message;
				setState(state => ({
					...state,
					loading: false,
					error: message,
				}));
			}
			else{
				setState(state => ({
					...state,
					loading: false,
					error: 'Ocurrió un error. No se pudo registrar el auto.',
				}));
			}
		})
	}, [state.signal]);

	useEffect(() => {
		if(marcas && marcas.length > 0){
			setState(state => {
				if(state.inputMarca === undefined){
					return { ...state, inputMarca: marcas[0] };
				}
				return state;
			});
		}
	}, [marcas]);

	const handleChangeMarca: ChangeEventHandler<HTMLSelectElement> = (event) => {
		if(marcas && marcas.length > 0){
			setState(state => {
				const marcaId = parseInt(event.target.value);
				const marca = marcas.find(m => m.id === marcaId);
				return { ...state, inputMarca: marca };
			});
		}
	};

	const handleChangeMonto: ChangeEventHandler<HTMLInputElement> = (event) => {
		setState(state => {
			const monto = parseInt(event.target.value);
			return { ...state, inputMonto: monto };
		});
	};

	const handleChangeCantidad: ChangeEventHandler<HTMLInputElement> = (event) => {
		setState(state => {
			const cantidad = parseInt(event.target.value);
			return { ...state, inputCantidad: cantidad };
		});
	};

	const handleSubmit: FormEventHandler = (ev) => {
		ev.preventDefault();
		if(state.inputMarca){
			setState(state => ({ ...state, inputLoading: false, inputError: null }));
			createBonos(state.inputMarca.id, state.inputMonto, state.inputCantidad)
			.then(() => {
				setState(state => ({ ...state, inputLoading: false, inputError: null, signal: Symbol() }));
			})
			.catch((err: AxiosError) => {
				if(err.response?.data){
					const message = (err.response?.data as any).message;
					setState(state => ({
						...state,
						inputLoading: false,
						inputError: message,
					}));
				}
				else{
					setState(state => ({
						...state,
						inputLoading: false,
						inputError: `Ocurrió un error. No se pudo registrar el auto.`,
					}));
				}
			});
		}
	};

	return (
		<Container className="py-2">
			<Card>
				<Card.Header>Bonos</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):((state.error ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> {state.error}
					</Card.Body>
				) : (
					<Card.Body>
						<Table style={{ tableLayout: 'auto' }}>
							<thead>
								<tr>
									<th>Marca</th>
									<th>Monto</th>
									<th>Fecha Inicio</th>
									<th>Fecha Término</th>
									<th>Cantidad</th>
								</tr>
							</thead>
							<tbody>
							{(state.bonos.length === 0) ? (
								<tr>
									<td colSpan={5} className="text-center fst-italic p-4">
										No hay bonos...
									</td>
								</tr>
							):(
								state.bonos.map((bono, index) => (
									<tr key={index}>
										<td>{bono.marca.nombre}</td>
										<td align="right">{numberWithCommas(bono.monto)}</td>
										<td>{bono.fechaInicio}</td>
										<td>{bono.fechaTermino}</td>
										<td align="right">{bono.count}</td>
									</tr>
								))
							)}
							</tbody>
						</Table>
						{state.inputError && (
							<Alert variant="danger">
								<FontAwesomeIcon icon={faWarning} /> {state.inputError}
							</Alert>
						)}
						<Form onSubmit={handleSubmit}>
							<InputGroup>
						        <InputGroup.Text id="input-marca">Marca:</InputGroup.Text>
								<Form.Select
									value={state.inputMarca?.id}
									onChange={handleChangeMarca}
									disabled={state.inputLoading || state.loading || marcasLoading}
									aria-describedby="input-marca"
								>
									{marcas.map(marca => (
										<option key={marca.id} value={marca.id}>{marca.nombre}</option>
									))}
								</Form.Select>
						        <InputGroup.Text id="input-monto">Monto:</InputGroup.Text>
								<Form.Control
									type="number"
									placeholder="Monto"
									value={state.inputMonto}
									onChange={handleChangeMonto}
									disabled={state.inputLoading || state.loading || marcasLoading}
									aria-describedby="input-monto"
								/>
						        <InputGroup.Text id="input-cantidad">Cantidad:</InputGroup.Text>
								<Form.Control
									type="number"
									placeholder="Cantidad"
									value={state.inputCantidad}
									onChange={handleChangeCantidad}
									disabled={state.inputLoading || state.loading || marcasLoading}
									aria-describedby="input-cantidad"
								/>
								<Button type="submit" disabled={state.inputLoading || state.loading || marcasLoading}>
									Agregar
								</Button>
							</InputGroup>
						</Form>
					</Card.Body>
				)))}
			</Card>
		</Container>
	)
};

export default ListBonos;