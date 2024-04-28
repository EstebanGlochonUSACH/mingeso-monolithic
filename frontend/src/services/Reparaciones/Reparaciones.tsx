import axios from "axios";
import type { Orden } from "../Ordenes/Ordenes";

export interface Reparacion {
	id?: number,
	tipo: string,
	monto: number,
	orden?: Orden,
};

export type ReparacionTipoDetail = { code: string, label: string, description: string };

const reparacionesTipo: Record<string, ReparacionTipoDetail> = {
	FRENOS: {
		code: 'FRENOS',
		label: 'Reparaciones del Sistema de Frenos',
		description: 'Incluye el reemplazo de pastillas de freno, discos, tambores, líneas de freno y reparación o reemplazo del cilindro maestro de frenos.',
	},
	REFRIGERACION: {
		code: 'REFRIGERACION',
		label: 'Servicio del Sistema de Refrigeración',
		description: 'Reparación o reemplazo de radiadores, bombas de agua, termostatos y mangueras, así como la solución de problemas de sobrecalentamiento.',
	},
	MOTOR: {
		code: 'MOTOR',
		label: 'Reparaciones del Motor',
		description: 'Desde reparaciones menores como el reemplazo de bujías y cables, hasta reparaciones mayores como la reconstrucción del motor o la reparación de la junta de la culata.',
	},
	TRANSMISION: {
		code: 'TRANSMISION',
		label: 'Reparaciones de la Transmisión',
		description: 'Incluyen la reparación o reemplazo de componentes de la transmisión manual o automática, cambios de líquido y solución de problemas de cambios de marcha.',
	},
	SIS_ELECTRICO: {
		code: 'SIS_ELECTRICO',
		label: 'Reparación del Sistema Eléctrico',
		description: 'Solución de problemas y reparación de alternadores, arrancadores, baterías y sistemas de cableado, así como la reparación de componentes eléctricos como faros, intermitentes y sistemas de entretenimiento.',
	},
	SIS_ESCAPE: {
		code: 'SIS_ESCAPE',
		label: 'Reparaciones del Sistema de Escape',
		description: 'Incluye el reemplazo del silenciador, tubos de escape, catalizador y la solución de problemas relacionados con las emisiones.',
	},
	NEUMATICOS: {
		code: 'NEUMATICOS',
		label: 'Reparación de Neumáticos y Ruedas',
		description: 'Reparación de pinchazos, reemplazo de neumáticos, alineación y balanceo de ruedas.',
	},
	SUSPENSION_DIRECCION: {
		code: 'SUSPENSION_DIRECCION',
		label: 'Reparaciones de la Suspensión y la Dirección',
		description: 'Reemplazo de amortiguadores, brazos de control, rótulas y reparación del sistema de dirección asistida.',
	},
	AIRE_ACONDICIONADO: {
		code: 'AIRE_ACONDICIONADO',
		label: 'Reparación del Sistema de Aire Acondicionado y Calefacción',
		description: 'Incluye la recarga de refrigerante, reparación o reemplazo del compresor, y solución de problemas del sistema de calefacción.',
	},
	COMBUSTIBLE: {
		code: 'COMBUSTIBLE',
		label: 'Reparaciones del Sistema de Combustible',
		description: 'Limpieza o reemplazo de inyectores de combustible, reparación o reemplazo de la bomba de combustible y solución de problemas de suministro de combustible.',
	},
	PARABRISAS: {
		code: 'PARABRISAS',
		label: 'Reparación y Reemplazo del Parabrisas y Cristales',
		description: 'Reparación de pequeñas grietas en el parabrisas o reemplazo completo de parabrisas y ventanas dañadas.',
	},
};

export const getAllReparacionTipo = () => Object.keys(reparacionesTipo);

export const getReparacionDetail = (tipo: string): ReparacionTipoDetail => {
	if(tipo in reparacionesTipo){
		return reparacionesTipo[tipo];
	}
	return {
		code: tipo,
		label: tipo,
		description: tipo,
	}
};

export const getTipoReparacion = async (orden: Orden) => {
	return axios.get('/api/reparaciones/tipos', { params: { ordenId: orden.id } }).then(res => (res.data as string[]));
};

export const createReparacion = async (tipo: string, orden: Orden) => {
	const reparacion: Reparacion = { tipo, monto: 0, orden };
	return axios.post('/api/reparaciones/create', reparacion).then(res => (res.data as Orden));
};

export const deleteReparacion = async (reparacion: Reparacion) => {
	if(reparacion.id){
		return axios.delete('/api/reparaciones/'+reparacion.id).then(res => (res.data as Orden));
	}
	return Promise.reject(new Error("La reparacion no tiene <id>"));
};