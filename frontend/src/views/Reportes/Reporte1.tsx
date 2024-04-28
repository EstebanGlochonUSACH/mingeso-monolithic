import { useState, type FC, useEffect } from "react";
import type { AxiosError } from "axios";
import { getReporteReparacionTipo } from "../../services/Reportes/Reportes";
import { type ReparacionTipoDetail, getAllReparacionTipo, getReparacionDetail } from "../../services/Reparaciones/Reparaciones";
import type { AutoTipo } from "../../services/Autos/Autos";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { numberWithCommas } from "../../utils/utils";

type ReporteAutoTipo = { tipo: AutoTipo, countVehiculos: number, montoTotal: number };

type ReporteReparacionTipo = { detail: ReparacionTipoDetail, autos: ReporteAutoTipo[] };

interface State {
	loading: boolean,
	error: string|null,
	reportes: ReporteReparacionTipo[],
};

const reparacionesTipo = getAllReparacionTipo();

const ViewReporte1: FC = () => {
	const [state, setState] = useState<State>({
		loading: true,
		error: null,
		reportes: [],
	});

	useEffect(() => {
		setState(state => ({ ...state, loading: true }));
		getReporteReparacionTipo()
		.then(reportes => {
			if(reportes && reportes.length > 0){
				const reportesTipo: any = {};
				for(const reporte of reportes){
					if(!(reporte.tipoReparacion in reportesTipo)){
						const tipoDetail = getReparacionDetail(reporte.tipoReparacion);
						const tipoObj: ReporteReparacionTipo = { detail: tipoDetail, autos: [] };
						reportesTipo[reporte.tipoReparacion] = tipoObj;
					}

					const tipoObj: ReporteReparacionTipo = reportesTipo[reporte.tipoReparacion];
					if(!(reporte.tipoAuto in tipoObj)){
						tipoObj.autos.push({
							tipo: reporte.tipoAuto,
							countVehiculos: reporte.countVehiculos,
							montoTotal: reporte.montoTotal,
						});
					}
				}

				const reposteList: ReporteReparacionTipo[] = [];
				for(const tipo of reparacionesTipo){
					reposteList.push(reportesTipo[tipo]);
				}

				setState(state => ({ ...state, loading: false, error: null, reportes: reposteList }));
			}
			else{
				setState(state => ({ ...state, loading: false, error: null, reportes: [] }));
			}
		})
		.catch((err: AxiosError) => setState(state => ({ ...state, loading: false, reportes: [], error: err.message })));
	}, []);

	return (
		<Card>
			<Card.Header>
				<b>Reporte</b>: Tipos de Reparaciones vs el número de Tipos de autos
			</Card.Header>
			{state.loading ? (
				<Card.Body className="text-center fst-italic p-4">Cargando Reportes...</Card.Body>
			):((state.error ? (
				<Card.Body className="text-center fst-italic p-5">
					<FontAwesomeIcon icon={faWarning} /> {state.error}
				</Card.Body>
			):((state.reportes.length === 0) ? (
				<Card.Body className="text-center fst-italic p-4">No hay datos</Card.Body>
			):(
				<Card.Body>
					<Row>
						{state.reportes.map(reporte => (
							<Col lg="6" key={reporte.detail.code}>
								<h6>{reporte.detail.label}</h6>
								<Table>
									<thead>
										<tr>
											<th>Tipo de Auto</th>
											<th>Cantidad</th>
											<th>Monto Total</th>
										</tr>
									</thead>
									<tbody>
										{reporte.autos.map(auto => (
											<tr key={auto.tipo}>
												<td>{auto.tipo}</td>
												<td>{auto.countVehiculos}</td>
												<td>{numberWithCommas(auto.montoTotal)}</td>
											</tr>
										))}
									</tbody>
								</Table>
							</Col>
						))}
					</Row>
				</Card.Body>
			))))}
		</Card>
	);
};

export default ViewReporte1;