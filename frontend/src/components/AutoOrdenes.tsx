import { useEffect, type FC, useState, useCallback } from "react";
import moment from 'moment';
import 'moment/locale/es';
import Card from "react-bootstrap/Card";
import type { Auto } from "../services/Autos/Autos";
import { type Orden, getOrdenes } from "../services/Ordenes/Ordenes";
import type { Pagination as PaginationType } from "../types/Pagination";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { faFileLines } from '@fortawesome/free-solid-svg-icons/faFileLines';
import Pagination from 'react-bootstrap/Pagination';
import Badge from 'react-bootstrap/Badge';
import Accordion from 'react-bootstrap/Accordion';
import Button from 'react-bootstrap/Button';
import TablaReparaciones from "./TablaReparaciones";

moment.locale('es');

interface AutoOrdenesProps {
	auto: Auto;
};

interface AutoOrdenesState {
	loading: boolean,
	error: string|null,
	ordenes: PaginationType<Orden>|null,
	page: number,
}

const DEFAULT_INIT: AutoOrdenesState = {
	loading: true,
	error: null,
	ordenes: null,
	page: 0,
};

const AutoOrdenes: FC<AutoOrdenesProps> = ({ auto }) => {
	const [state, setState] = useState(DEFAULT_INIT);

	useEffect(() => {
		setState(state => ({ ...state, loading: true }));
		getOrdenes(state.page, auto)
			.then(ordenes => {
				setState(state => ({ ...state, loading: false, error: null, ordenes }));
			})
			.catch((err: Error) => setState(state => ({ ...state, loading: false, ordenes: null, error: err.message })));
	}, [state.page, auto]);

	const handlePageClick = useCallback((page: number) => setState(state => ({ ...state, page })), [setState]);

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

	if(state.loading){
		return (<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>);
	}
	else if(state.error){
		return (
			<Card.Body className="text-center fst-italic p-5">
				<FontAwesomeIcon icon={faWarning} /> {state.error}
			</Card.Body>
		);
	}
	else if(state.ordenes === null){
		return (
			<Card.Body className="text-center fst-italic p-5">
				<FontAwesomeIcon icon={faWarning} /> Ocurri&oacute; un error
			</Card.Body>
		);
	}
	else if(state.ordenes.numberOfElements === 0){
		return (<Card.Body className="text-center fst-italic p-4">No hay Ordenes para este Auto</Card.Body>);
	}

	return (
		<Card.Body>
			<Accordion className="mb-3" defaultActiveKey={state.ordenes.content[0].id.toString()}>
				{state.ordenes.content.map(orden => (
					<Accordion.Item key={orden.id} eventKey={orden.id.toString()}>
						<Accordion.Header>
							<div>
								<span className="fw-bold">Orden</span> <Badge bg="secondary" pill>#{orden.id}</Badge>
							</div>
						</Accordion.Header>
						<Accordion.Body>
							<div className="d-flex flex-wrap">
								<div className="w-50">
									<b>Fecha de Ingreso:</b> {orden.fechaIngreso ? orden.fechaIngreso : (<em>N/A</em>)}
								</div>
								<div className="w-50">
									<b>Fecha de Salida:</b> {orden.fechaSalida ? orden.fechaSalida : (<em>N/A</em>)}
								</div>
								<div className="w-50">
									<b>Fecha de Entrega:</b> {orden.fechaEntrega ? orden.fechaEntrega : (<em>N/A</em>)}
								</div>
								<div className="w-50">
									<b>Atraso:</b> {orden.atraso ? `${orden.atraso} días` : (<em>N/A</em>)}
								</div>
							</div>
							<TablaReparaciones orden={orden} reparaciones={orden.reparaciones} />
							<Button href={"/orden/" + orden.id}>
								<FontAwesomeIcon icon={faFileLines} /> Ver Orden
							</Button>
						</Accordion.Body>
					</Accordion.Item>
				))}
			</Accordion>
			<div className="d-flex">
				<Pagination className="mb-0 mx-auto" size="sm">
					{pageItems}
				</Pagination>
			</div>
		</Card.Body>
	)
};

export default AutoOrdenes;