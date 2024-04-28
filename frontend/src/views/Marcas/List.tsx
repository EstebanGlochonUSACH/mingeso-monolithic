import { useState, type FC, type FormEventHandler } from "react";
import { type Marca, useMarcasService } from "../../services/Marcas/Marcas";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning';
import { faXmark } from '@fortawesome/free-solid-svg-icons/faXmark';
import Container from "react-bootstrap/Container";
import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Form from "react-bootstrap/Form";
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import Alert from "react-bootstrap/Alert";

const ListMarcas: FC = () => {
	const { loading, error, marcas, add, remove } = useMarcasService();
	const [value, setValue] = useState('');

	const handleSubmit: FormEventHandler = (ev) => {
		ev.preventDefault();
		if(add) add(value);
	};

	const handleRemove = (marca: Marca) => {
		if(remove) remove(marca);
	};

	return (
		<Container className="py-2">
			<Card>
				<Card.Header>Marcas</Card.Header>
				{loading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Autos...</Card.Body>
				):(
					<Card.Body>
						<Table style={{ tableLayout: 'auto' }}>
							<thead>
								<tr>
									<th>Nombre</th>
									<th>Autos</th>
									<th className="min-width-cell">Eliminar</th>
								</tr>
							</thead>
							<tbody>
								{(marcas.length === 0) ? (
									<tr>
										<td colSpan={3} className="text-center fst-italic p-4">No hay datos</td>
									</tr>
								):(
									marcas.map(marca => (
										<tr key={marca.id}>
											<td width="70%">{marca.nombre}</td>
											<td width="30%">{marca.totalAutos || 0}</td>
											<td className="min-width-cell" align="center">
												<Button size="sm" onClick={() => handleRemove(marca)}>
													<FontAwesomeIcon icon={faXmark} />
												</Button>
											</td>
										</tr>
									))
								)}
							</tbody>
						</Table>
						<Form onSubmit={handleSubmit}>
							<InputGroup className="mb-2">
								<Form.Control
									type="text"
									placeholder="Nombre de la Marca"
									value={value}
									onChange={ev => setValue(ev.target.value)}
								/>
								<Button type="submit">Agregar</Button>
							</InputGroup>
							{error && (
								<Alert className="mb-0" variant="danger">
									<FontAwesomeIcon icon={faWarning} /> {error}
								</Alert>
							)}
						</Form>
					</Card.Body>
				)}
			</Card>
		</Container>
	)
};

export default ListMarcas;