import { useReducer, type FC, useEffect } from "react";
import { getReparacionDetail, getTipoReparacion, type Reparacion, type ReparacionTipoDetail } from "../services/Reparaciones/Reparaciones";
import type { ReducerAction } from "../types/Reducer";
import type { Orden } from "../services/Ordenes/Ordenes";
import Dropdown from 'react-bootstrap/Dropdown';

interface ReducerState {
	loading: boolean,
	tipos: ReparacionTipoDetail[],
	filtered: ReparacionTipoDetail[],
};

const DEFAULT_STATE: ReducerState = {
	loading: true,
	tipos: [],
	filtered: [],
};

const actions = {
	FETCHING: Symbol.for('FETCHING'),
	FETCH_DONE: Symbol.for('FETCH_DONE'),
	UPDATE: Symbol.for('UPDATE'),
};

const reducerHandler = (state: ReducerState, action: ReducerAction): ReducerState => {
	if(action.type === actions.FETCHING){
		return { ...state, loading: true };
	}
	else if(action.type === actions.FETCH_DONE){
		const tipos: ReparacionTipoDetail[] = action.tipos;
		const reparaciones: Reparacion[] = action.reparaciones;

		let filtered = tipos;
		if(reparaciones.length > 0 && tipos.length > 0){
			const codes = reparaciones.map(r => r.tipo);
			filtered = tipos.filter(tipo => !codes.includes(tipo.code));
		}

		return {
			...state,
			loading: false,
			tipos,
			filtered,
		};
	}
	else if(action.type === actions.UPDATE){
		const tipos = state.tipos;
		const reparaciones: Reparacion[] = action.reparaciones;

		let filtered = tipos;
		if(reparaciones.length > 0 && tipos.length > 0){
			const codes = reparaciones.map(r => r.tipo);
			filtered = tipos.filter(tipo => !codes.includes(tipo.code));
		}

		return {
			...state,
			loading: false,
			filtered,
		};
	}
	return state;
};

interface DropdownReparacionesProps {
	loading: boolean,
	orden: Orden,
	reparaciones: Reparacion[],
	onSelect: (code: string) => void,
};

const DropdownReparaciones: FC<DropdownReparacionesProps> = ({ loading, orden, reparaciones, onSelect }) => {
	const [state, dispatch] = useReducer(reducerHandler, DEFAULT_STATE);

	useEffect(() => {
		if(orden){
			if(state.loading){
				dispatch({ type: actions.FETCHING });
				getTipoReparacion(orden)
				.then(codes => {
					const tipos: ReparacionTipoDetail[] = codes.map(code => getReparacionDetail(code));
					dispatch({ type: actions.FETCH_DONE, tipos, reparaciones });
				})
				.catch(() => dispatch({ type: actions.FETCH_DONE, tipos: [], reparaciones }));
			}
			else{
				dispatch({ type: actions.UPDATE, reparaciones });
			}
		}
	}, [orden, reparaciones, state.loading]);

	const handleSelect = (eventKey: string|null) => {
		if(eventKey){
			onSelect(eventKey);
		}
	};

	return (
		<Dropdown drop="start" onSelect={handleSelect}>
			<Dropdown.Toggle
				id="add-reparacion"
				disabled={state.loading || loading || state.filtered.length == 0}
			>
				Agregar Reparación
			</Dropdown.Toggle>
			<Dropdown.Menu>
				{state.loading ? (
					<Dropdown.ItemText>Cargando Reparaciones...</Dropdown.ItemText>
				):(
					state.filtered.map(tipo => (
						<Dropdown.Item as="button" key={tipo.code} eventKey={tipo.code}>{tipo.label}</Dropdown.Item>
					))
				)}
			</Dropdown.Menu>
		</Dropdown>
	);
};

export default DropdownReparaciones;