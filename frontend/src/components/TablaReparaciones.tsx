import type { FC } from "react";
import { type Reparacion, getReparacionDetail } from "../services/Reparaciones/Reparaciones";
import type { Orden } from "../services/Ordenes/Ordenes";
import { numberWithCommas } from "../utils/utils";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faScrewdriverWrench } from '@fortawesome/free-solid-svg-icons/faScrewdriverWrench';
import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";

interface ReparacionRowProps {
	reparacion: Reparacion,
	deletable?: boolean,
	onDelete?: (reparacion: Reparacion) => void,
};

const ReparacionRow: FC<ReparacionRowProps> = ({ reparacion, deletable = false, onDelete }) => {
	const detail = getReparacionDetail(reparacion.tipo);
	const handleDelete = () => {
		if(deletable && onDelete) onDelete(reparacion);
	};
	return (
		<tr >
			<td>
				{deletable ? (
					<div className="reparacion-row-grid">
						<div>
							<div>
								<span className="me-2"><FontAwesomeIcon icon={faScrewdriverWrench} /></span> {detail.label}
							</div>
							<div className="fs-small opacity-50">{detail.description}</div>
						</div>
						<div>
							<Button size="sm" onClick={handleDelete}>Eliminar</Button>
						</div>
					</div>
				):(
					<>
						<div>
							<span className="me-2"><FontAwesomeIcon icon={faScrewdriverWrench} /></span> {detail.label}
						</div>
						<div className="fs-small opacity-50">{detail.description}</div>
					</>
				)}
			</td>
			<td align="right">+{numberWithCommas(reparacion.monto)}</td>
		</tr>
	);
};

interface TablaReparacionesProps {
	orden: Orden,
	reparaciones: Reparacion[],
	deletable?: boolean,
	onDelete?: (reparacion: Reparacion) => void,
};

const TablaReparaciones: FC<TablaReparacionesProps> = ({ orden, reparaciones, deletable = false, onDelete }) => {
	return (
		<Table bordered className="mt-3">
			<thead>
				<tr>
					<th>Reparaciones, descuentos y recargos</th>
					<th>Monto</th>
				</tr>
			</thead>
			<tbody>
				{(reparaciones.length > 0) ? (
					reparaciones.map((reparacion, index) => (
						<ReparacionRow
							key={index}
							reparacion={reparacion}
							deletable={deletable}
							onDelete={onDelete}
						/>
					))
				):(
					<tr>
						<td colSpan={2} className="text-center fst-italic p-4">
							No hay Reparaciones...
						</td>
					</tr>
				)}
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
				{(orden.fechaEntrega && orden.recargaAtraso && orden.recargaAtraso > 0) ? (
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
				{(orden.fechaEntrega && orden.montoTotal && orden.montoTotal > 0 && orden.valorIva && orden.valorIva > 0) && (
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
	)
};

export default TablaReparaciones;