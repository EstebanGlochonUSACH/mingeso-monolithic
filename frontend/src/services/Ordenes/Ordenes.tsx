import axios from 'axios';
import moment from 'moment';
import 'moment/locale/es';
import type { Pagination } from '../../types/Pagination';
import type { Auto } from '../Autos/Autos';
import type { Bono } from '../Bonos/Bonos';
import type { Reparacion } from '../Reparaciones/Reparaciones';
import type { ResponseObject } from '../../types/ResponseObject';

moment.locale('es');

export interface Orden {
	id: number,
	id_orden?: number,
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

function parseOrden(orden: Orden){
	orden.atraso = null;
	if(orden.fechaIngreso){
		let fechaIngreso = moment(orden.fechaIngreso);
		orden.fechaIngreso = fechaIngreso.format('LLL');
	}
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
};

function parseOrdenesResponse(pagination: Pagination<Orden>){
	pagination.content = pagination.content.map(orden => parseOrden(orden));
	return pagination;
}

export const getOrdenes = async (page: number, auto?: Auto) => {
	if(auto){
		return axios.get('/api/ordenes', { params: { page, auto: auto.id } })
			.then(res => parseOrdenesResponse(res.data as Pagination<Orden>));
	}
	return axios.get('/api/ordenes', { params: { page } })
		.then(res => parseOrdenesResponse(res.data as Pagination<Orden>));
};

export const getOrdenesWithPatente = async (page: number, patente?: string) => {
	if(patente){
		return axios.get('/api/ordenes', { params: { page, patente } }).then(res => parseOrdenesResponse(res.data as Pagination<Orden>));
	}
	return axios.get('/api/ordenes', { params: { page } })
		.then(res => parseOrdenesResponse(res.data as Pagination<Orden>));
};

export const getOrden = (id: number) =>
	axios.get('/api/ordenes/'+id).then(res => (res.data as Orden));

export const updateOrden = (orden: Orden) =>
	axios.put('/api/ordenes/'+orden.id, orden).then(res => (res.data as ResponseObject<Orden>));

export const createOrden = async (auto: Auto) => {
	return axios.post('/api/ordenes/create', { auto })
		.then(res => {
			const resObj = (res.data as ResponseObject<Orden>);
			if(resObj.entity){
				resObj.entity = parseOrden(resObj.entity);
			}
			return resObj;
		});
};
