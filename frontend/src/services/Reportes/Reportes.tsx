import axios from "axios";
import type { AutoMotor, AutoTipo } from "../Autos/Autos";

export interface ReporteTipo {
	tipoReparacion: string,
	tipoAuto: AutoTipo,
	countVehiculos: number,
	montoTotal: number,
};

export const getReporteReparacionTipo = async () => {
	return axios.get('/api/reportes/tipo-auto').then(res => (res.data as ReporteTipo[]));
};

export interface ReporteTipoMotor {
	tipoReparacion: string,
	tipoMotor: AutoMotor,
	countVehiculos: number,
	montoTotal: number,
};

export const getReporteReparacionMotor = async () => {
	return axios.get('/api/reportes/tipo-motor').then(res => (res.data as ReporteTipoMotor[]));
};

export interface ReporteTiempoMarca {
	marca: string,
	avgRepairTime: number,
	avgRepairTimeText: string,
};

export const getReporteTiempoReparacion = async () => {
	return axios.get('/api/reportes/tiempo-reparacion').then(res => (res.data as ReporteTiempoMarca[]));
};

