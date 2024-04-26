import axios from 'axios';
import type { Pagination } from '../../types/Pagination';
import type { Auto } from '../Autos/Autos';

export interface Bono {
	id: number,
	fechaInicio: string,
	fechaTermino: string,
	marca: {
		id: number,
		nombre: string
	},
	monto: number,
	usado: boolean,
};

export interface Reparacion {
	tipo: string,
	monto: number,
}

export interface Orden {
	id: number,
	auto: Auto,
	bono: Bono|null,
	montoReparaciones: number|null,
	descuentoDiaAtencion: number|null,
	descuentoReparaciones: number|null,
	recargaAntiguedad: number|null,
	recargaKilometraje: number|null,
	recargaAtraso: number|null,
	fechaIngreso: string|null,
	fechaSalida: string|null,
	fechaEntrega: string|null,
	atraso: number|null,
	montoTotal: number|null,
	valorIva: number|null,
	reparaciones: Reparacion[],
};

export const getOrdenes = async (page: number, auto?: Auto) => {
	if(auto){
		return axios.get(`/api/ordenes?page=${page}&auto=${auto.id}`).then(res => (res.data as Pagination<Orden>));
	}
	return axios.get('/api/ordenes?page='+page).then(res => (res.data as Pagination<Orden>));
};

export const getOrden = (id: number) =>
	axios.get('/api/ordenes/'+id).then(res => (res.data as Orden));

export const updateOrden = (orden: Orden) =>
	axios.put('/api/ordenes/'+orden.id, orden).then(res => (res.data as Orden));
