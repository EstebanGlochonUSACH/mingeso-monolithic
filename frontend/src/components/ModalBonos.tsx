import { useState, type FC, useEffect } from "react";
import { type Bono, getBonos } from "../services/Bonos/Bonos";
import type { Marca } from "../services/Marcas/Marcas";
import Modal from "react-bootstrap/Modal";
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import { numberWithCommas } from "../utils/utils";

interface ModalBonosProps {
	show: boolean,
	marca: Marca,
	fecha: string|null,
	onSelect: (bono: Bono) => void,
	onHide: () => void,
};

const ModalBonos: FC<ModalBonosProps> = ({ show, marca, fecha, onSelect, onHide }) => {
	const [bonos, setBonos] = useState<Bono[]>([]);

	useEffect(() => {
		getBonos(marca, fecha)
		.then(bonos => setBonos(bonos))
		.catch(() => setBonos([]));
	}, []);

	return (
		<Modal show={show} onHide={onHide}>
			<Modal.Header>Buscar Bonos</Modal.Header>
			<Modal.Body>
				<Table>
					<thead>
						<tr>
							<th>Bono</th>
							<th>Monto</th>
							<th>Usar</th>
						</tr>
					</thead>
					<tbody>
						{(bonos.length == 0) ? (
							<tr>
								<td colSpan={3} className="text-center fst-italic p-4">No hay bonos</td>
							</tr>
						):(
							bonos.map(bono => (
								<tr key={bono.id}>
									<td>{bono.marca.nombre}#{bono.id}</td>
									<td>{numberWithCommas(bono.monto)}</td>        
									<td align="right">
										<Button size="sm" onClick={() => onSelect(bono)}>
											Usar
										</Button>
									</td>                        
								</tr>
							))
						)}
					</tbody>
				</Table>
			</Modal.Body>
		</Modal>
	)
};

export default ModalBonos;