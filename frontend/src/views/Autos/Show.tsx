import { useReducer, type FC, useEffect } from "react";
import type { AxiosError } from "axios";
import { Params, useNavigate, useParams } from "react-router-dom";
import { getAuto, type Auto, updateAuto } from "../../services/Autos/Autos";
import type { ReducerAction } from "../../types/Reducer";
import { createOrden, type Orden } from "../../services/Ordenes/Ordenes";
import type { Pagination } from "../../types/Pagination";
import type { ResponseObject } from "../../types/ResponseObject";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { faFileLines } from '@fortawesome/free-solid-svg-icons/faFileLines';
import Badge from "react-bootstrap/Badge";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import AutoInfoTable from "../../components/AutoInfoTable";
import AutoOrdenes from "../../components/AutoOrdenes";

interface ReducerState {
	loading: boolean,
	error: string|null,
	auto: Auto|null,
	id_auto: number|null,
	page: number,
	ordenes: Pagination<Orden>|null,
};

const DEFAULT_STATE: ReducerState = {
	loading: true,
	error: null,
	auto: null,
	id_auto: null,
	page: 0,
	ordenes: null,
};

const actions = {
	FETCHING: Symbol.for('FETCHING'),
	FETCH_SUCCESS: Symbol.for('FETCH_SUCCESS'),
	FETCH_FAILED: Symbol.for('FETCH_FAILED'),
	CHANGE_PAGE: Symbol.for('CHANGE_PAGE'),
	UPDATE_ID: Symbol.for('UPDATE_ID'),
};

const reducerHandler = (state: ReducerState, action: ReducerAction): ReducerState => {
	if(action.type === actions.FETCHING){
		return { ...state, loading: true };
	}
	else if(action.type === actions.FETCH_SUCCESS){
		return { ...state, loading: false, error: null, auto: action.auto };
	}
	else if(action.type === actions.FETCH_FAILED){
		return { ...state, loading: false, auto: null, error: action.error };
	}
	else if(action.type === actions.UPDATE_ID){
		if(state.id_auto !== action.id_auto){
			return { ...state, id_auto: action.id_auto };
		}
	}
	return state;
};

const reducerInit = (params: Params): ReducerState => {
	let id_auto = null;
	if(params.id){
		id_auto = parseInt(params.id);
	}
	return { ...DEFAULT_STATE, id_auto };
};

const ShowAuto: FC = () => {
	const params = useParams();
	const [state, dispatch] = useReducer(reducerHandler, params, reducerInit);
	const navigate = useNavigate();

	useEffect(() => {
		if(params.id){
			dispatch({ type: actions.UPDATE_ID, id_auto: parseInt(params.id) })
		}
	}, [params]);

	useEffect(() => {
		if(state.id_auto){
			dispatch({ type: actions.FETCHING });
			getAuto(state.id_auto)
				.then(auto => dispatch({ type: actions.FETCH_SUCCESS, auto }))
				.catch((err: Error) => dispatch({ type: actions.FETCH_FAILED, error: err.message }));
		}
	}, [state.id_auto]);

	const handleChangeAuto = (auto: Auto) => {
		dispatch({ type: actions.FETCHING });
		updateAuto(auto)
			.then(auto => dispatch({ type: actions.FETCH_SUCCESS, auto }))
			.catch((err: Error) => dispatch({ type: actions.FETCH_FAILED, error: err.message }));
	};

	const handleCreateOrden = (auto: Auto|null) => {
		if(auto){
			createOrden(auto).then(resObj => {
				if(resObj.error){
					alert(resObj.message);
				}
				else if(resObj.entity){
					const orden = resObj.entity;
					navigate('/orden/' + orden.id);
				}
			})
			.catch((err: AxiosError) => {
				if(err.response?.data){
					alert((err.response?.data as ResponseObject<null>).message);
				}
				else{
					alert('Ocurrió un error. No se pudo crear la orden.')
				}
			});
		}
	};

	return (
		<Container className="py-2">
			<Card className="mb-2">
				<Card.Header>
					Informaci&oacute;n del Auto <Badge>#{state.id_auto}</Badge>
				</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):((state.error) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> {state.error}
					</Card.Body>
				):((state.auto === null) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> No hay datos
					</Card.Body>
				):(
					<Card.Body>
						<AutoInfoTable
							auto={state.auto}
							onChange={handleChangeAuto}
						/>
						<Button className="mt-2" onClick={() => handleCreateOrden(state.auto)} disabled={state.auto == null}>
							<FontAwesomeIcon icon={faFileLines} /> Crear Orden
						</Button>
					</Card.Body>
				)))}
			</Card>
			<Card>
				<Card.Header>
					Historial de Ordenes y Reparaciones
				</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):((state.auto == null) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> No hay auto...
					</Card.Body>
				):(
					<AutoOrdenes auto={state.auto} />
				))}
			</Card>
		</Container>
	);
};

export default ShowAuto;