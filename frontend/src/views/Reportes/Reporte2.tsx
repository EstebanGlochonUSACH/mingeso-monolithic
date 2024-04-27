import { useState, type FC, useEffect } from "react";
import humanizeDuration from "humanize-duration";
import type { AxiosError } from "axios";
import { type ReporteTiempoMarca, getReporteTiempoReparacion } from "../../services/Reportes/Reportes";
import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";

interface State {
	loading: boolean,
	error: string|null,
	reportes: ReporteTiempoMarca[],
};

const ViewReporte2: FC = () => {
	const [state, setState] = useState<State>({
		loading: true,
		error: null,
		reportes: [],
	});

	useEffect(() => {
		setState(state => ({ ...state, loading: true }));
		getReporteTiempoReparacion()
		.then(reportes => {
			reportes = reportes.map(reporte => {
				const milisecs = Math.round(reporte.avgRepairTime) * 1000;
				const avgRepairTimeText = humanizeDuration(milisecs, {
					language: 'es',
					fallbacks: ['en'],
					units: ['d', 'h', 'm'],
					round: true,
				});
				return { ...reporte, avgRepairTimeText };
			})
			setState(state => ({ ...state, loading: false, error: null, reportes: reportes }));
		})
		.catch((err: AxiosError) => setState(state => ({ ...state, loading: false, reportes: [], error: err.message })));
	}, []);

	return (
		<Card>
			<Card.Header>
				<b>Reporte</b>: Tiempos promedio de reparación por cada una de las marcas de vehículos
			</Card.Header>
			<Card.Body>
				<Table>
					<thead>
						<tr>
							<th>Marca</th>
							<th>Tiempo Promedio</th>
						</tr>
					</thead>
					<tbody>
						{state.reportes.map(reporte => (
							<tr key={reporte.marca}>
								<td>{reporte.marca}</td>
								<td>{reporte.avgRepairTimeText}</td>
							</tr>
						))}
					</tbody>
				</Table>
			</Card.Body>
		</Card>
	);
};

export default ViewReporte2;