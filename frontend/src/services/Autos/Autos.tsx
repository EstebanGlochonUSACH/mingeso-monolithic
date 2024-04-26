import axios from 'axios';
import type { Pagination } from '../../types/Pagination';

export const autoTipos = ['SEDAN', 'HATCHBACK', 'SUV', 'PICKUP', 'FURGONETA'];

export type AutoTipo = 'SEDAN' | 'HATCHBACK' | 'SUV' | 'PICKUP' | 'FURGONETA';

export const autoMotores = ['GASOLINA', 'DIESEL', 'HIBRIDO', 'ELECTRICO'];

export type AutoMotor = 'GASOLINA' | 'DIESEL' | 'HIBRIDO' | 'ELECTRICO';

export interface Auto {
	id: number,
	patente: string,
	anio: number,
	asientos: number,
	kilometraje: number,
	marca: {
		id: number,
		nombre: string,
	},
	modelo: string,
	motor: AutoMotor,
	tipo: AutoTipo,
};

export const getAutos = async (page: number, patente: string) => {
	if(patente){
		return axios.get(`/api/autos?page=${page}&patente=${patente}`).then(res => (res.data as Pagination<Auto>));
	}
	return axios.get('/api/autos?page='+page).then(res => (res.data as Pagination<Auto>));
};

export const getAuto = (id_auto: number) =>
	axios.get('/api/autos/'+id_auto).then(res => (res.data as Auto));

export const updateAuto = (auto: Auto) =>
	axios.put('/api/autos/'+auto.id, auto).then(res => (res.data as Auto));
