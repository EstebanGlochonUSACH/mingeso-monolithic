import { useState, type FC } from "react";
import { Formik } from 'formik';
import { useMarcasService } from "../../services/Marcas/Marcas";
import { autoMotores, autoTipos } from "../../services/Autos/Autos";
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Alert from 'react-bootstrap/Alert';
import axios, { type AxiosError } from "axios";

const RegexpAuto1 = /^[A-Z]{2}[1-9]{1}[0-9]{3}$/;
const RegexpAuto2 = /^[BCDFGHJKLPRSTVWXYZ]{4}[0-9]{2}$/;

const currentYearPlus1 = (new Date()).getFullYear() + 1;

const formValidator = (values: any) => {
	const errors: any = {};
	// Patente
	if (!values.patente) {
		errors.patente = 'Campo requerido';
	} else if (!RegexpAuto1.test(values.patente) && !RegexpAuto2.test(values.patente)) {
		errors.patente = 'Código patente inválido';
	}
	// Marca
	if (!values.marca) {
		errors.marca = 'Campo requerido';
	}
	// Modelo
	if (!values.modelo) {
		errors.modelo = 'Campo requerido';
	}
	// Tipo
	if (!values.tipo) {
		errors.tipo = 'Campo requerido';
	} else if (!autoTipos.includes(values.tipo)) {
		errors.tipo = 'Este tipo es un valor inválido';
	}
	// Motor
	if (!values.motor) {
		errors.motor = 'Campo requerido';
	} else if (!autoMotores.includes(values.motor)) {
		errors.motor = 'Este tipo de motor es un valor inválido';
	}
	// Año
	if (!('anio' in values) || typeof(values.anio) !== 'number') {
		errors.anio = 'Campo requerido';
	}
	else if(values.anio < 1900 || values.anio > currentYearPlus1){
		errors.anio = 'Valor inválido';
	}
	// Asientos
	if (!('asientos' in values) || typeof(values.asientos) !== 'number') {
		errors.asientos = 'Campo requerido';
	}
	else if(values.asientos < 1 || values.asientos > 100){
		errors.asientos = 'Valor inválido';
	}
	// Kilometraje
	if (!('kilometraje' in values) || typeof(values.kilometraje) !== 'number') {
		errors.kilometraje = 'Campo requerido';
	}
	else if(values.kilometraje < 0){
		errors.kilometraje = 'Valor inválido';
	}
	return errors;
};

const CreateAuto: FC = () => {
	const { loading:marcasLoading, marcas } = useMarcasService();
	const [state, setState] = useState({
		loading: false,
		response: false,
		isError: false,
		message: '',
	});

	const handleSubmit = (values: any, { setSubmitting }: any) => {
		const autoData = { ...values };
		const marcaId = parseInt(values.marca);
		autoData.marca = marcas.find(m => m.id === marcaId);
		if(!autoData.marca) return;

		setState(s => ({ ...s, loading: true, response: false }));
		axios.post('/api/autos', autoData)
		.then(() => {
			setSubmitting(false);
			setState(state => ({
				...state,
				loading: false,
				response: true,
				isError: false,
				message: 'Auto creado correctamente.',
			}));
		})
		.catch((err: AxiosError) => {
			setSubmitting(false);
			if(err.response?.data){
				const message = (err.response?.data as any).message;
				setState(state => ({
					...state,
					loading: false,
					response: true,
					isError: true,
					message,
				}));
			}
			else{
				setState(state => ({
					...state,
					loading: false,
					response: true,
					isError: true,
					message: `Ocurrió un error. No se pudo registrar el auto.`,
				}));
			}
		})
	};

	const defaultValues = {
		patente: '',
		marca: (marcas && marcas.length > 0) ? marcas[0].id : undefined,
		modelo: '',
		anio: 2024,
		tipo: autoTipos[0],
		motor: autoMotores[0],
		asientos: 4,
		kilometraje: 0,
	};

	return (
		<Container>
			<Card className="my-2">
				<Card.Header>Registrar Vehículo</Card.Header>
				{marcasLoading ? (
					<Card.Body className="text-center fst-italic p-4">Cargando Marcas...</Card.Body>
				):(
					<Card.Body>
						<Formik initialValues={defaultValues} validate={formValidator} onSubmit={handleSubmit}>
						{({
							values,
							errors,
							touched,
							handleChange,
							handleBlur,
							handleSubmit:onSubmit,
							isSubmitting,
						}) => (
							<Form onSubmit={onSubmit}>
								<Form.Group as={Row} className="mb-3" controlId="patente">
									<Form.Label column sm="2">Patente</Form.Label>
									<Col sm="10">
										<Form.Control
											type="text"
											name="patente"
											placeholder="AB1234"
											isInvalid={!!errors.patente && !!touched.patente}
											value={values.patente}
											disabled={isSubmitting || marcasLoading || state.loading}
											onChange={handleChange}
											onBlur={handleBlur}
										/>
										{(errors.patente && touched.patente) && (
											<Form.Text className="text-danger">{errors.patente}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="marca">
									<Form.Label column sm="2">Marca</Form.Label>
									<Col sm="10">
										<Form.Select
											name="marca"
											value={values.marca}
											onChange={handleChange}
											onBlur={handleBlur}
											isInvalid={!!errors.marca && !!touched.marca}
											disabled={isSubmitting || marcasLoading || state.loading}
										>
											{marcas.map(marca => (
												<option key={marca.id} value={marca.id}>{marca.nombre}</option>
											))}
										</Form.Select>
										{(errors.marca && touched.marca) && (
											<Form.Text className="text-danger">{errors.marca}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="modelo">
									<Form.Label column sm="2">Modelo</Form.Label>
									<Col sm="10">
										<Form.Control
											type="text"
											name="modelo"
											placeholder="Nombre Modelo"
											isInvalid={!!errors.modelo && !!touched.modelo}
											value={values.modelo}
											disabled={isSubmitting || marcasLoading || state.loading}
											onChange={handleChange}
											onBlur={handleBlur}
										/>
										{(errors.modelo && touched.modelo) && (
											<Form.Text className="text-danger">{errors.modelo}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="anio">
									<Form.Label column sm="2">Año</Form.Label>
									<Col sm="10">
										<Form.Control
											type="number"
											name="anio"
											placeholder="2024"
											isInvalid={!!errors.anio && !!touched.anio}
											value={values.anio}
											disabled={isSubmitting || marcasLoading || state.loading}
											onChange={handleChange}
											onBlur={handleBlur}
										/>
										{(errors.anio && touched.anio) && (
											<Form.Text className="text-danger">{errors.anio}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="tipo">
									<Form.Label column sm="2">Tipo</Form.Label>
									<Col sm="10">
										<Form.Select
											name="tipo"
											value={values.tipo}
											onChange={handleChange}
											onBlur={handleBlur}
											isInvalid={!!errors.tipo && !!touched.tipo}
											disabled={isSubmitting || marcasLoading || state.loading}
										>
											<option disabled>- Seleccionar Tipo -</option>
											{autoTipos.map(tipo => (
												<option key={tipo} value={tipo}>{tipo}</option>
											))}
										</Form.Select>
										{(errors.tipo && touched.tipo) && (
											<Form.Text className="text-danger">{errors.tipo}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="motor">
									<Form.Label column sm="2">Motor</Form.Label>
									<Col sm="10">
										<Form.Select
											name="motor"
											value={values.motor}
											onChange={handleChange}
											onBlur={handleBlur}
											isInvalid={!!errors.motor && !!touched.motor}
											disabled={isSubmitting || marcasLoading || state.loading}
										>
											<option disabled>- Seleccionar Tipo Motor -</option>
											{autoMotores.map(motor => (
												<option key={motor} value={motor}>{motor}</option>
											))}
										</Form.Select>
										{(errors.motor && touched.motor) && (
											<Form.Text className="text-danger">{errors.motor}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="asientos">
									<Form.Label column sm="2">Nro. de Asientos</Form.Label>
									<Col sm="10">
										<Form.Control
											type="number"
											name="asientos"
											placeholder="4"
											min={1}
											max={100}
											step={1}
											isInvalid={!!errors.asientos && !!touched.asientos}
											value={values.asientos}
											disabled={isSubmitting || marcasLoading || state.loading}
											onChange={handleChange}
											onBlur={handleBlur}
										/>
										{(errors.asientos && touched.asientos) && (
											<Form.Text className="text-danger">{errors.asientos}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<Form.Group as={Row} className="mb-3" controlId="kilometraje">
									<Form.Label column sm="2">Kilometraje</Form.Label>
									<Col sm="10">
										<Form.Control
											type="number"
											name="kilometraje"
											placeholder="4"
											min={0}
											step={1}
											isInvalid={!!errors.kilometraje && !!touched.kilometraje}
											value={values.kilometraje}
											disabled={isSubmitting || marcasLoading || state.loading}
											onChange={handleChange}
											onBlur={handleBlur}
										/>
										{(errors.kilometraje && touched.kilometraje) && (
											<Form.Text className="text-danger">{errors.kilometraje}</Form.Text>
										)}
									</Col>
								</Form.Group>
								<div>
									{state.response && (
										<Alert variant={state.isError ? 'danger' : 'success'}>{state.message}</Alert>
									)}
									<Button type="submit" disabled={isSubmitting || marcasLoading || state.loading}>Registrar</Button>
								</div>
							</Form>
						)}
						</Formik>
					</Card.Body>
				)}
			</Card>
		</Container>
	)
};

export default CreateAuto;