import { useEffect, type FC, useState, useCallback } from "react";
import moment from 'moment';
import 'moment/locale/es';
import Card from "react-bootstrap/Card";
import type { Auto } from "../services/Autos/Autos";
import { type Orden, getOrdenes, type Reparacion } from "../services/Ordenes/Ordenes";
import type { Pagination as PaginationType } from "../types/Pagination";
import { getReparacionDetail } from "../services/Reparaciones/Reparaciones";
import { numberWithCommas } from "../utils/utils";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { faScrewdriverWrench } from '@fortawesome/free-solid-svg-icons/faScrewdriverWrench';
import { faFileLines } from '@fortawesome/free-solid-svg-icons/faFileLines';
import Pagination from 'react-bootstrap/Pagination';
import Badge from 'react-bootstrap/Badge';
import Accordion from 'react-bootstrap/Accordion';
import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';

moment.locale('es');

const ReparacionRow: FC<{ reparacion: Reparacion }> = ({ reparacion }) => {
	const detail = getReparacionDetail(reparacion.tipo);
	return (
		<tr >
			<td>
				<div>
					<span className="me-2"><FontAwesomeIcon icon={faScrewdriverWrench} /></span> {detail.label}
				</div>
				<div className="fs-small opacity-50">{detail.description}</div>
			</td>
			<td align="right">+{numberWithCommas(reparacion.monto)}</td>
		</tr>
	);
};

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
				ordenes.content = ordenes.content.map(orden => {
					let fechaIngreso = moment(orden.fechaIngreso);
					orden.fechaIngreso = fechaIngreso.format('LLL');

					orden.atraso = null;

					if(orden.fechaEntrega && orden.fechaSalida){
						let fechaEntrega = moment(orden.fechaEntrega);
						let fechaSalida = moment(orden.fechaSalida);

						orden.atraso = fechaEntrega.diff(fechaSalida, 'days');

						orden.fechaEntrega = fechaEntrega.format('LLL');
						orden.fechaSalida = fechaSalida.format('LLL');

					}
					else if(orden.fechaEntrega){
						let fechaEntrega = moment(orden.fechaEntrega);
						orden.fechaEntrega = fechaEntrega.format('LLL');
					}
					else if(orden.fechaSalida){
						let fechaSalida = moment(orden.fechaSalida);
						orden.fechaSalida = fechaSalida.format('LLL');
					}

					return orden;
				});
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
							<Table bordered className="mt-3">
								<thead>
									<tr>
										<th>Reparaciones, descuentos y recargos</th>
										<th>Monto</th>
									</tr>
								</thead>
								<tbody>
									{orden.reparaciones.map((reparacion, index) => (
										<ReparacionRow key={index} reparacion={reparacion} />
									))}
									{(orden.montoReparaciones !== null) ? (
										<tr>
											<td align="right">
												<b>Total Reparaciones</b>
											</td>
											<td align="right">{numberWithCommas(orden.montoReparaciones)}</td>
										</tr>
									):(
										<></>
									)}
									{(orden.descuentoReparaciones && orden.descuentoReparaciones > 0) ? (
										<tr>
											<td>
												<div>Descuentos por n&uacute;mero de reparaciones:</div>
												<div className="fs-small opacity-50">Descuentos aplicado según tipo de auto y cantidad de d&iacute;as de atraso.</div>
											</td>
											<td align="right">-{numberWithCommas(orden.descuentoReparaciones)}</td>
										</tr>
									):(
										<></>
									)}
									{(orden.descuentoDiaAtencion && orden.descuentoDiaAtencion > 0) ? (
										<tr>
											<td>
												<div>Descuento por d&iacute;a de Atenci&oacute;n</div>
												<div className="fs-small opacity-50">10% de descuento sobre el monto total de reparación si el vehículo ingresa al taller los lunes y jueves entre las 09:00 hrs y las 12:00 hrs.</div>
											</td>
											<td align="right">-{numberWithCommas(orden.descuentoDiaAtencion)}</td>
										</tr>
									):(
										<></>
									)}
									{orden.bono ? (
										<tr>
											<td>
												<div>Descuento por bono "{orden.bono.marca.nombre}"</div>
												<div className="fs-small opacity-50">Bono por convenio de la concesionaria TopCar.</div>
											</td>
											<td align="right">-{numberWithCommas(orden.bono.monto)}</td>
										</tr>
									):(
										<></>
									)}
									{(orden.recargaKilometraje && orden.recargaKilometraje > 0) ? (
										<tr>
											<td>
												<div>Recargo por kilometraje</div>
												<div className="fs-small opacity-50">Recargo efectuado en base al valor del kilometraje y el tipo del vehículo.</div>
											</td>
											<td align="right">+{numberWithCommas(orden.recargaKilometraje)}</td>
										</tr>
									):(
										<></>
									)}
									{(orden.recargaAntiguedad && orden.recargaAntiguedad > 0) ? (
										<tr>
											<td>
												<div>Recargo por antigüedad del vehículo</div>
												<div className="fs-small opacity-50">Recargo efectuado en base a la antigüedad y tipo del vehículo.</div>
											</td>
											<td align="right">+{numberWithCommas(orden.recargaAntiguedad)}</td>
										</tr>
									):(
										<></>
									)}
									{(orden.recargaAtraso && orden.recargaAtraso > 0) ? (
										<tr>
											<td>
												<div>Recargo por Retraso en la Recogida del Vehículo</div>
												<div className="fs-small opacity-50">Recargo proporcional a la cantidad de días de atraso en la recogida del vehículo.</div>
											</td>
											<td align="right">+{numberWithCommas(orden.recargaAtraso)}</td>
										</tr>
									):(
										<></>
									)}
									{(orden.montoTotal && orden.montoTotal > 0 && orden.valorIva && orden.valorIva > 0) && (
										<>
											<tr>
												<td align="right">
													<b>Monto Total</b>
												</td>
												<td align="right">{numberWithCommas(orden.montoTotal)}</td>
											</tr>
											<tr>
												<td align="right">
													<b>IVA</b>
												</td>
												<td align="right">+{numberWithCommas(orden.valorIva)}</td>
											</tr>
											<tr>
												<td align="right">
													<b>Total + IVA</b>
												</td>
												<td align="right">{numberWithCommas(orden.montoTotal + orden.valorIva)}</td>
											</tr>
										</>
									)}
								</tbody>
							</Table>
							<Button>
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