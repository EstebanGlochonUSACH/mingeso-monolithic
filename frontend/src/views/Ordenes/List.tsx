import { useReducer, type FC, useEffect, useCallback, type ChangeEventHandler, type FormEventHandler } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getOrdenesWithPatente, type Orden } from "../../services/Ordenes/Ordenes";
import type { ReducerAction } from "../../types/Reducer";
import type { Pagination as PaginationType } from "../../types/Pagination";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { faFilter } from '@fortawesome/free-solid-svg-icons/faFilter';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Pagination from 'react-bootstrap/Pagination';
import InputGroup from 'react-bootstrap/InputGroup';
import { Badge } from "react-bootstrap";

interface ReducerState {
	loading: boolean,
	error: string|null,
	ordenes: PaginationType<Orden>|null,
	page: number,
	patente: string,
	patenteBuffer: string,
};

const DEFAULT_STATE: ReducerState = {
	loading: true,
	error: null,
	ordenes: null,
	page: 0,
	patente: '',
	patenteBuffer: '',
};

const actions = {
	FETCHING: Symbol.for('FETCHING'),
	FETCH_SUCCESS: Symbol.for('FETCH_SUCCESS'),
	FETCH_FAILED: Symbol.for('FETCH_FAILED'),
	CHANGE_PAGE: Symbol.for('CHANGE_PAGE'),
	UPDATE_PATENTE: Symbol.for('UPDATE_PATENTE'),
	UPDATE_PARAMS: Symbol.for('UPDATE_PARAMS'),
};

const reducerHandler = (state: ReducerState, action: ReducerAction): ReducerState => {
	if(action.type === actions.FETCHING){
		return { ...state, loading: true };
	}
	else if(action.type === actions.FETCH_SUCCESS){
		return { ...state, loading: false, error: null, ordenes: action.ordenes };
	}
	else if(action.type === actions.FETCH_FAILED){
		return { ...state, loading: false, ordenes: null, error: action.error };
	}
	else if(action.type === actions.CHANGE_PAGE){
		return { ...state, page: action.page };
	}
	else if(action.type === actions.UPDATE_PATENTE){
		if('patenteBuffer' in action){
			return { ...state, patenteBuffer: action.patenteBuffer };
		}
		else if('patente' in action){
			return { ...state, patente: action.patente };
		}
	}
	else if(action.type === actions.UPDATE_PARAMS){
		if(action.page !== state.page || action.patente !== state.patente){
			return {
				...state,
				page: action.page,
				patente: action.patente,
			};
		}
	}
	return state;
};

const reducerInit = (searchParams: URLSearchParams): ReducerState => {
	let page = 0;
	let valuePage = searchParams.get('page');
	if(valuePage){
		page = parseInt(valuePage) - 1;
	}
	let patente = '';
	let valuePatente = searchParams.get('patente');
	if(valuePatente){
		patente = valuePatente;
	}
	return { ...DEFAULT_STATE, page, patente: patente, patenteBuffer: patente };
};

const ListOrdenes: FC = () => {
	const [searchParams, setSearchParams] = useSearchParams();
	const [state, dispatch] = useReducer(reducerHandler, searchParams, reducerInit);

	useEffect(() => {
		dispatch({ type: actions.FETCHING });
		getOrdenesWithPatente(state.page, state.patente).then(ordenes => {
			dispatch({ type: actions.FETCH_SUCCESS, ordenes });
		})
		.catch((err: Error) => {
			dispatch({ type: actions.FETCH_FAILED, error: err.message });
		});
	}, [state.page, state.patente]);

	useEffect(() => {
		let page = 0;
		let valuePage = searchParams.get('page');
		if(valuePage){
			page = parseInt(valuePage) - 1;
		}
		let patente = '';
		let valuePatente = searchParams.get('patente');
		if(valuePatente){
			patente = valuePatente;
		}
		dispatch(({ type: actions.UPDATE_PARAMS, page, patente }));
	}, [searchParams])

	const handlePageClick = useCallback((pageNumber: number) => {
		setSearchParams(sp => {
			sp.set('page', (pageNumber + 1).toString());
			return sp;
		});
		dispatch({ type: actions.CHANGE_PAGE, page: pageNumber });
	}, [setSearchParams]);

	const handleChangePatenteBuffer: ChangeEventHandler<HTMLInputElement> = useCallback((event) =>
		dispatch({ type: actions.UPDATE_PATENTE, patenteBuffer: event.target.value }), []);

	const handleSearchSubmit: FormEventHandler<HTMLFormElement> = (event) => {
		event.preventDefault();
		setSearchParams(sp => {
			if(state.patenteBuffer){
				sp.set('patente', state.patenteBuffer);
			}
			else if(sp.has('patente')){
				sp.delete('patente');
			}
			return sp;
		});
		dispatch({ type: actions.UPDATE_PATENTE, patente: state.patenteBuffer });
	};

	let pageItems = [];
	if(state.ordenes){
		for (let number = 0; number < state.ordenes.totalPages; number++) {
			pageItems.push(
				(number === state.ordenes.number) ? (
					<Pagination.Item key={number} active={number === state.ordenes.number}>
						{number+1}
					</Pagination.Item>
				):(
					<Pagination.Item key={number} onClick={() => handlePageClick(number)}>
						{number+1}
					</Pagination.Item>
				)
			);
		}
	}

	return (
		<Container className="py-2">
			<Card>
				<Card.Header>
					<div>Lista de Ordenes</div>
				</Card.Header>
				<Card.Header className="p-2">
					<Form onSubmit={handleSearchSubmit}>
						<InputGroup>
							<Form.Control
								value={state.patenteBuffer}
								onChange={handleChangePatenteBuffer}
								placeholder="Filtrar por patente"
								aria-label="Patente"
								aria-describedby="buscar-orden"
							/>
							<Button id="button-search-orden" type="submit" variant="outline-secondary">
								<FontAwesomeIcon icon={faFilter} /> Filtrar
							</Button>
						</InputGroup>
					</Form>
				</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Ordenes...</Card.Body>
				):((state.error) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> {state.error}
					</Card.Body>
				):((!state.ordenes) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> Ocurri&oacute; un error
					</Card.Body>
				):((state.ordenes.numberOfElements === 0) ? (
					<Card.Body className="text-center fst-italic p-4">No hay datos</Card.Body>
				):(
					<Card.Body>
						<Table striped bordered hover>
							<thead>
								<tr>
									<th>Código</th>
									<th>Patente</th>
									<th>Bono</th>
									<th>Fecha Ingreso</th>
									<th>Fecha Salida</th>
									<th>Fecha Entrega</th>
									<th>Atraso</th>
								</tr>
							</thead>
							<tbody>
								{state.ordenes.content.map(orden => (
									<tr key={orden.id}>
										<td>
											<Link to={"/orden/" + orden.id}>Orden #{orden.id}</Link>
										</td>
										<td>
											<Link to={"/auto/" + orden.auto.id}>{orden.auto.patente}</Link>
                                        </td>
										<td>{(orden.bono) ? (<Badge pill>{orden.bono.marca.nombre}#{orden.bono.id}</Badge>) : (<em className="opacity-50">N/A</em>)}</td>
										<td>{orden.fechaIngreso ? orden.fechaIngreso : (<em className="opacity-25">N/A</em>)}</td>
										<td>{orden.fechaSalida ? orden.fechaSalida : (<em className="opacity-25">N/A</em>)}</td>
										<td>{orden.fechaEntrega ? orden.fechaEntrega : (<em className="opacity-25">N/A</em>)}</td>
										<td>{orden.atraso ? `${orden.atraso} días` : (<em className="opacity-25">N/A</em>)}</td>
									</tr>
								))}
							</tbody>
						</Table>
						<div className="d-flex">
							<Pagination className="mb-0 mx-auto" size="sm">
								{pageItems}
							</Pagination>
						</div>
					</Card.Body>
				))))}
			</Card>
		</Container>
	);
};

export default ListOrdenes;