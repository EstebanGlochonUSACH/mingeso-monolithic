import axios from "axios";
import type { Marca } from "../Marcas/Marcas";

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

export interface BonoGroup {
	fechaInicio: string,
	fechaTermino: string,
	marca: {
		id: number,
		nombre: string
	},
	monto: number,
	count: number,
};

export const getBonos = async (marca: Marca, fecha: string|null) => {
	if(marca && fecha){
		return axios.get('/api/bonos', { params: { marcaId: marca.id, fecha } }).then(res => (res.data as Bono[]));
	}
	else if(marca){
		return axios.get(`/api/bonos`, { params: { marcaId: marca.id } }).then(res => (res.data as Bono[]));
	}
	return axios.get('/api/bonos').then(res => (res.data as Bono[]));
};

export const getBonosByGroup = async () => {
	return axios.get('/api/bonos/grouped-by-fecha-inicio').then(res => (res.data as BonoGroup[]));
};

export const createBonos = async (marcaId: number, monto: number, cantidad: number) => {
	return axios.post('/api/bonos/create', null, { params: { marcaId, monto, cantidad } })
		.then(res => (res.data as Bono[]));
};