import { useReducer, type FC, useEffect, useCallback, ChangeEventHandler } from "react";
import type { AxiosError } from "axios";
import moment from "moment";
import { type Params, useParams } from "react-router-dom";
import type { ReducerAction } from "../../types/Reducer";
import { getOrden, updateOrden, type Orden } from "../../services/Ordenes/Ordenes";
import { type Reparacion, createReparacion, deleteReparacion } from "../../services/Reparaciones/Reparaciones";
import type { Bono } from "../../services/Bonos/Bonos";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import Badge from "react-bootstrap/Badge";
import Card from "react-bootstrap/Card";
import Container from "react-bootstrap/Container";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Alert from 'react-bootstrap/Alert';
import InputGroup from 'react-bootstrap/InputGroup';
import BadgeInput from "../../components/BadgeInput";
import ModalBonos from "../../components/ModalBonos";
import TablaReparaciones from "../../components/TablaReparaciones";
import DropdownReparaciones from "../../components/DropdownReparaciones";

interface ReducerState {
	loading: boolean,
	error: string|null,
	orden: Orden|null,
	idOrden: number|null,
	showModal: boolean,
	updateLoading: boolean,
	updateIsError: boolean,
	updateMessage: string,
	signal: Symbol,
};

const DEFAULT_STATE: ReducerState = {
	loading: true,
	error: null,
	orden: null,
	idOrden: null,
	showModal: false,
	updateLoading: false,
	updateIsError: false,
	updateMessage: '',
	signal: Symbol(),
};

const actions = {
	FETCHING: Symbol.for('FETCHING'),
	FETCH_SUCCESS: Symbol.for('FETCH_SUCCESS'),
	FETCH_FAILED: Symbol.for('FETCH_FAILED'),
	UPDATING: Symbol.for('UPDATING'),
	UPDATE_SUCCESS: Symbol.for('UPDATE_SUCCESS'),
	UPDATE_FAILED: Symbol.for('UPDATE_FAILED'),
	TOGGLE_MODAL: Symbol.for('TOGGLE_MODAL'),
	CHANGE_PAGE: Symbol.for('CHANGE_PAGE'),
	UPDATE_ID: Symbol.for('UPDATE_ID'),
	UPDATE_BONO: Symbol.for('UPDATE_BONO'),
	UPDATE_FECHA: Symbol.for('UPDATE_FECHA'),
	SIGNAL: Symbol.for('SIGNAL'),
};

const reducerHandler = (state: ReducerState, action: ReducerAction): ReducerState => {
	if(action.type === actions.FETCHING){
		return { ...state, loading: true, error: null };
	}
	else if(action.type === actions.FETCH_SUCCESS){
		return { ...state, loading: false, error: null, orden: action.orden };
	}
	else if(action.type === actions.FETCH_FAILED){
		return { ...state, loading: false, orden: null, error: action.error };
	}
	if(action.type === actions.UPDATING){
		return { ...state, updateLoading: true, updateIsError: false, updateMessage: '' };
	}
	else if(action.type === actions.UPDATE_SUCCESS){
		return {
			...state,
			updateLoading: false,
			updateIsError: false,
			updateMessage: 'La orden se editó correctamente',
			orden: action.orden,
		};
	}
	else if(action.type === actions.UPDATE_FAILED){
		return {
			...state,
			updateLoading: false,
			updateIsError: true,
			updateMessage: action.error,
		};
	}
	else if(action.type === actions.TOGGLE_MODAL){
		return { ...state, showModal: !state.showModal };
	}
	else if(action.type === actions.SIGNAL){
		return { ...DEFAULT_STATE, signal: Symbol() };
	}
	else if(action.type === actions.UPDATE_ID){
		if(state.idOrden !== action.idOrden){
			return { ...state, idOrden: action.idOrden };
		}
	}
	else if(action.type === actions.UPDATE_BONO){
		if(state.orden){
			return {
				...state,
				orden: { ...state.orden, bono: action.bono },
				showModal: false,
			};
		}
	}
	else if(action.type === actions.UPDATE_FECHA){
		if(state.orden){
			let nextState: ReducerState = state;
			if('fechaIngreso' in action){
				nextState = { ...nextState, orden: { ...state.orden, fechaIngreso: action.fechaIngreso } };
			}
			else if('fechaSalida' in action){
				nextState = { ...nextState, orden: { ...state.orden, fechaSalida: action.fechaSalida } };
			}
			else if('fechaEntrega' in action){
				nextState = { ...nextState, orden: { ...state.orden, fechaEntrega: action.fechaEntrega } };
			}
			return nextState;
		}
	}
	return state;
};

const reducerInit = (params: Params): ReducerState => {
	let idOrden = null;
	if(params.id){
		idOrden = parseInt(params.id);
	}
	return { ...DEFAULT_STATE, idOrden };
};

const ShowOrden: FC = () => {
	const params = useParams();
	const [state, dispatch] = useReducer(reducerHandler, params, reducerInit);

	useEffect(() => {
		if(params.id){
			dispatch({ type: actions.UPDATE_ID, idOrden: parseInt(params.id) })
		}
	}, [params]);

	useEffect(() => {
		if(state.idOrden){
			dispatch({ type: actions.FETCHING });
			getOrden(state.idOrden)
				.then(orden => dispatch({ type: actions.FETCH_SUCCESS, orden }))
				.catch((err: Error) => dispatch({ type: actions.FETCH_FAILED, error: err.message }));
		}
	}, [state.idOrden, state.signal]);

	const toggleModal = useCallback(() => dispatch({ type: actions.TOGGLE_MODAL }), []);

	const handleSelectBono = useCallback((bono: Bono|null) => dispatch({ type: actions.UPDATE_BONO, bono }), []);

	const handleChangeFechaIngreso: ChangeEventHandler<HTMLInputElement> = useCallback((event) => {
		dispatch({ type: actions.UPDATE_FECHA, fechaIngreso: event.target.value });
	}, []);

	const handleChangeFechaSalida: ChangeEventHandler<HTMLInputElement> = useCallback((event) => {
		dispatch({ type: actions.UPDATE_FECHA, fechaSalida: event.target.value });
	}, []);

	const handleChangeFechaEntrega: ChangeEventHandler<HTMLInputElement> = useCallback((event) => {
		dispatch({ type: actions.UPDATE_FECHA, fechaEntrega: event.target.value });
	}, []);

	const handleSaveChanges = () => {
		if(state.orden){
			const orden: Orden = { ...state.orden };
			if(!orden.fechaIngreso){
				orden.fechaSalida = null;
				orden.fechaEntrega = null;
			}
			else if(!orden.fechaSalida){
				orden.fechaEntrega = null;
			}

			if(orden.fechaIngreso){
				orden.fechaIngreso = moment(orden.fechaIngreso).toISOString();
			}
			if(orden.fechaSalida){
				orden.fechaSalida = moment(orden.fechaSalida).toISOString();
			}
			if(orden.fechaEntrega){
				orden.fechaEntrega = moment(orden.fechaEntrega).toISOString();
			}

			dispatch({ type: actions.UPDATING });
			updateOrden(orden)
			.then(res => {
				if(res.error){
					dispatch({ type: actions.UPDATE_FAILED, error: res.message });
				}
				else if(res.entity){
					dispatch({ type: actions.UPDATE_SUCCESS, orden: res.entity });
				}
			})
			.catch((err: AxiosError) => {
				if(err.response?.data){
					const message = (err.response?.data as any).message;
					dispatch({ type: actions.UPDATE_FAILED, error: message });
				}
				else{
					dispatch({ type: actions.UPDATE_FAILED, error: 'Ocurrió un error. No se pudo editar la orden.' });
				}
			});
		}
	};

	const handleSelectReparacion = useCallback((tipo: string) => {
		if(state.orden){
			dispatch({ type: actions.UPDATING });
			createReparacion(tipo, state.orden)
			.then(orden => {
				dispatch({ type: actions.UPDATE_SUCCESS, orden });
			})
			.catch((err: AxiosError) => {
				if(err.response?.data){
					const message = (err.response?.data as any).message;
					dispatch({ type: actions.UPDATE_FAILED, error: message });
				}
				else{
					dispatch({ type: actions.UPDATE_FAILED, error: 'Ocurrió un error. No se pudo editar la orden.' });
				}
			});
		}
	}, [state.orden]);

	const handleDeleteReparacion = useCallback((reparacion: Reparacion) => {
		if(state.orden){
			dispatch({ type: actions.UPDATING });
			deleteReparacion(reparacion)
			.then(orden => {
				dispatch({ type: actions.UPDATE_SUCCESS, orden });
			})
			.catch((err: AxiosError) => {
				if(err.response?.data){
					const message = (err.response?.data as any).message;
					dispatch({ type: actions.UPDATE_FAILED, error: message });
				}
				else{
					dispatch({ type: actions.UPDATE_FAILED, error: 'Ocurrió un error. No se pudo editar la orden.' });
				}
			});
		}
	}, [state.orden]);

	return (
		<Container className="py-2">
			<Card className="mb-2">
				<Card.Header>
					Detalle de la Orden <Badge>#{state.idOrden}</Badge>
				</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):((state.error) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> {state.error}
					</Card.Body>
				):((state.orden === null) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> No hay datos
					</Card.Body>
				):(
					<Card.Body>
						<Form.Group as={Row} className="mb-3" controlId="bono">
							<Form.Label column sm="2">Auto</Form.Label>
							<Col sm="10">
								<InputGroup>
									<Form.Control readOnly value={state.orden.auto.patente} />
									<Button href={'/auto/' + state.orden.auto.id} target="_blank">Ver</Button>
								</InputGroup>
							</Col>
						</Form.Group>
						<Form.Group as={Row} className="mb-3" controlId="bono">
							<Form.Label column sm="2">Bono</Form.Label>
							<Col sm="10">
								<InputGroup>
									<BadgeInput
										value={state.orden.bono ? `${state.orden.bono.marca.nombre}#${state.orden.bono.id}` : undefined}
										placeholder="Seleccionar Bono"
									/>
									<Button variant="outline-primary" onClick={() => handleSelectBono(null)} disabled={state.updateLoading}>Eliminar</Button>
									<Button onClick={toggleModal} disabled={state.updateLoading}>Buscar</Button>
								</InputGroup>
							</Col>
						</Form.Group>
						<Form.Group as={Row} className="mb-3" controlId="fecha-ingreso">
							<Form.Label column sm="2">Fecha Ingreso</Form.Label>
							<Col sm="10">
								<InputGroup>
									<Form.Control
										type="datetime-local"
										value={state.orden.fechaIngreso || undefined}
										onChange={handleChangeFechaIngreso}
										disabled={state.updateLoading || state.orden.reparaciones.length === 0}
									/>
								</InputGroup>
							</Col>
						</Form.Group>
						<Form.Group as={Row} className="mb-3" controlId="fecha-salida">
							<Form.Label column sm="2">Fecha Salida</Form.Label>
							<Col sm="10">
								<InputGroup>
									<Form.Control
										type="datetime-local"
										value={state.orden.fechaSalida || undefined}
										onChange={handleChangeFechaSalida}
										disabled={state.updateLoading || !state.orden.fechaIngreso}
									/>
								</InputGroup>
							</Col>
						</Form.Group>
						<Form.Group as={Row} className="mb-3" controlId="fecha-entrega">
							<Form.Label column sm="2">Fecha Entrega</Form.Label>
							<Col sm="10">
								<InputGroup>
									<Form.Control
										type="datetime-local"
										value={state.orden.fechaEntrega || undefined}
										onChange={handleChangeFechaEntrega}
										disabled={state.updateLoading || !state.orden.fechaSalida}
									/>
								</InputGroup>
							</Col>
						</Form.Group>
						{state.updateMessage && (
							<Alert variant={state.updateIsError ? 'danger' : 'success'}>{state.updateMessage}</Alert>
						)}
						<div className="d-flex justify-content-end">
							<Button onClick={handleSaveChanges} disabled={state.updateLoading}>Guardar</Button>
						</div>
					</Card.Body>
				)))}
			</Card>
			<Card>
				<Card.Header>Reparaciones</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):((state.orden == null) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> No hay auto...
					</Card.Body>
				):(
					<Card.Body>
						{(!state.orden.fechaIngreso) && (
							<div className="d-flex justify-content-end">
								<DropdownReparaciones
									loading={state.loading || state.updateLoading}
									orden={state.orden}
									reparaciones={state.orden.reparaciones}
									onSelect={handleSelectReparacion}
								/>
							</div>
						)}
						<TablaReparaciones
							orden={state.orden}
							reparaciones={state.orden.reparaciones}
							deletable={!state.orden.fechaIngreso}
							onDelete={handleDeleteReparacion}
						/>
					</Card.Body>
				))}
			</Card>
			{state.orden && (
				<ModalBonos
					show={state.showModal}
					marca={state.orden.auto.marca}
					fecha={state.orden.fechaIngreso}
					onSelect={handleSelectBono}
					onHide={toggleModal}
				/>
			)}
		</Container>
	);
};

export default ShowOrden;