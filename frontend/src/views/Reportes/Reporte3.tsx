import { useState, type FC, useEffect } from "react";
import type { AxiosError } from "axios";
import { getReporteReparacionMotor } from "../../services/Reportes/Reportes";
import { type ReparacionTipoDetail, getAllReparacionTipo, getReparacionDetail } from "../../services/Reparaciones/Reparaciones";
import type { AutoMotor } from "../../services/Autos/Autos";
import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { numberWithCommas } from "../../utils/utils";

type ReporteAutoMotor = { tipo: AutoMotor, countVehiculos: number, montoTotal: number };

type ReporteReparacionTipo = { detail: ReparacionTipoDetail, autos: ReporteAutoMotor[] };

interface State {
	loading: boolean,
	error: string|null,
	reportes: ReporteReparacionTipo[],
};

const reparacionesTipo = getAllReparacionTipo();

const ViewReporte3: FC = () => {
	const [state, setState] = useState<State>({
		loading: true,
		error: null,
		reportes: [],
	});

	useEffect(() => {
		setState(state => ({ ...state, loading: true }));
		getReporteReparacionMotor()
		.then(reportes => {
			const reportesTipo: any = {};
			for(const reporte of reportes){
				if(!(reporte.tipoReparacion in reportesTipo)){
					const tipoDetail = getReparacionDetail(reporte.tipoReparacion);
					const tipoObj: ReporteReparacionTipo = { detail: tipoDetail, autos: [] };
					reportesTipo[reporte.tipoReparacion] = tipoObj;
				}

				const tipoObj: ReporteReparacionTipo = reportesTipo[reporte.tipoReparacion];
				if(!(reporte.tipoMotor in tipoObj)){
					tipoObj.autos.push({
						tipo: reporte.tipoMotor,
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
		})
		.catch((err: AxiosError) => setState(state => ({ ...state, loading: false, reportes: [], error: err.message })));
	}, []);

	return (
		<Card>
			<Card.Header>
				<b>Reporte</b>: Tipos de Reparaciones vs el número de vehículos según Tipo de Motor
			</Card.Header>
			<Card.Body>
				<Row>
					{state.reportes.map(reporte => (
						<Col lg="6" key={reporte.detail.code}>
							<h6>{reporte.detail.label}</h6>
							<Table>
								<thead>
									<tr>
										<th>Tipo de Motor</th>
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
		</Card>
	);
};

export default ViewReporte3;