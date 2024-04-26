import { useReducer, type FC, useEffect, useCallback, type ChangeEventHandler, type FormEventHandler } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getAutos, type Auto } from "../../services/Autos/Autos";
import type { ReducerAction } from "../../types/Reducer";
import type { Pagination as PaginationType } from "../../types/Pagination";
import { numberWithCommas } from "../../utils/utils";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { faFilter } from '@fortawesome/free-solid-svg-icons/faFilter';
import { faSquarePlus } from '@fortawesome/free-solid-svg-icons/faSquarePlus';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Pagination from 'react-bootstrap/Pagination';
import InputGroup from 'react-bootstrap/InputGroup';

interface ReducerState {
	loading: boolean,
	error: string|null,
	autos: PaginationType<Auto>|null,
	page: number,
	patente: string,
	patenteBuffer: string,
};

const DEFAULT_STATE: ReducerState = {
	loading: true,
	error: null,
	autos: null,
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
		return { ...state, loading: false, error: null, autos: action.autos };
	}
	else if(action.type === actions.FETCH_FAILED){
		return { ...state, loading: false, autos: null, error: action.error };
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

const ListAutos: FC = () => {
	const [searchParams, setSearchParams] = useSearchParams();
	const [state, dispatch] = useReducer(reducerHandler, searchParams, reducerInit);

	useEffect(() => {
		dispatch({ type: actions.FETCHING });
		getAutos(state.page, state.patente).then(autos => {
			dispatch({ type: actions.FETCH_SUCCESS, autos });
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
	if(state.autos){
		for (let number = 0; number < state.autos.totalPages; number++) {
			pageItems.push(
				(number === state.autos.number) ? (
					<Pagination.Item key={number} active={number === state.autos.number}>
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
		<Container>
			<div className="d-flex justify-content-between align-items-center mt-3 mb-1">
				<h3 className="m-0">Lista de Autos</h3>
				<Button>
					<FontAwesomeIcon icon={faSquarePlus} /> Registrar Vehículo
				</Button>
			</div>
			<Card className="my-2">
				<Card.Header className="p-2">
					<Form onSubmit={handleSearchSubmit}>
						<InputGroup>
							<Form.Control
								value={state.patenteBuffer}
								onChange={handleChangePatenteBuffer}
								placeholder="Filtrar por patente"
								aria-label="Patente"
								aria-describedby="buscar-auto"
							/>
							<Button id="button-search-auto" type="submit">
								<FontAwesomeIcon icon={faFilter} /> Filtrar
							</Button>
						</InputGroup>
					</Form>
				</Card.Header>
				{state.loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):((state.error) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> {state.error}
					</Card.Body>
				):((state.autos === null) ? (
					<Card.Body className="text-center fst-italic p-5">
						<FontAwesomeIcon icon={faWarning} /> Ocurri&oacute; un error
					</Card.Body>
				):(
					<Card.Body>
						<Table striped bordered hover>
							<thead>
								<tr>
									<th>Patente</th>
									<th>A&ntilde;o</th>
									<th>Marca</th>
									<th>Modelo</th>
									<th>Asientos</th>
									<th>Motor</th>
									<th>Tipo</th>
									<th>Kilometraje</th>
								</tr>
							</thead>
							<tbody>
								{state.autos.content.map(auto => (
									<tr key={auto.id}>
										<td>
											<Link to={"/auto/" + auto.id}>{auto.patente}</Link>
										</td>
										<td align="right">{auto.anio}</td>
										<td>{auto.marca.nombre}</td>
										<td>{auto.modelo}</td>
										<td align="right">{auto.asientos}</td>
										<td>{auto.motor}</td>
										<td>{auto.tipo}</td>
										<td align="right">{numberWithCommas(auto.kilometraje)}</td>
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
				)))}
			</Card>
		</Container>
	);
};

export default ListAutos;