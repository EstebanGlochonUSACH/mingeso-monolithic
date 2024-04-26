import { type FC, type PropsWithChildren, createContext, useContext, useState, useEffect } from "react";
import axios, { type AxiosError } from "axios";

export interface Marca {
	id: number,
	nombre: string,
	totalAutos?: number,
};

interface MarcasContextState {
	loading: boolean,
	error: string|null,
	marcas: Marca[],
	add?: (nombre: string) => void,
	remove?: (marca: Marca) => void,
}

const DEFAULT_STATE: MarcasContextState = {
	loading: false,
	error: null,
	marcas: [],
};

const MarcasContext = createContext<MarcasContextState>(DEFAULT_STATE);

export const useMarcasService = () => useContext(MarcasContext);

export const MarcasProvider: FC<PropsWithChildren> = ({ children }) => {
	const [state, setState] = useState(DEFAULT_STATE);

	useEffect(() => {
		setState(state => ({ ...state, loading: true }));
		axios.get('/api/marcas').then(res => {
			const marcas = res.data as Marca[];
			marcas.sort((marca1,marca2) => marca1.nombre.localeCompare(marca2.nombre));
			setState(state => ({ ...state, loading: false, marcas }));
		})
		.catch(() => {
			setState(state => ({ ...state, loading: false, marcas: [] }));
		});
	}, []);

	const add = (nombre: string) => {
		setState(state => ({ ...state, loading: true }));
		axios.post('/api/marcas/create', { nombre }).then(res => {
			const marca = res.data as Marca;
			const marcas = [ ...state.marcas, marca ];
			marcas.sort((marca1,marca2) => marca1.nombre.localeCompare(marca2.nombre));
			setState(state => ({
				...state,
				loading: false,
				error: null,
				marcas,
			}));
		})
		.catch((err: AxiosError) => {
			setState(state => ({ ...state, loading: false }));
			if(err.response?.data){
				const message = (err.response?.data as any).message;
				setState(state => ({
					...state,
					loading: false,
					error: message,
				}));
			}
			else{
				setState(state => ({
					...state,
					loading: false,
					error: `No se pudo crear la marca "${nombre}"`,
				}));
			}
		});
	};

	const remove = (marca: Marca) => {
		setState(state => ({ ...state, loading: true }));
		axios.delete('/api/marcas/'+marca.id).then(() => {
			setState(state => {
				const marcas = state.marcas.filter(m => m.id !== marca.id);
				marcas.sort((marca1,marca2) => marca1.nombre.localeCompare(marca2.nombre));
				return {
					...state,
					loading: false,
					marcas: marcas,
				};
			});
		})
		.catch((err: AxiosError) => {
			setState(state => ({ ...state, loading: false }));
			if(err.response?.data){
				const message = (err.response?.data as any).message;
				setState(state => ({
					...state,
					loading: false,
					error: message,
				}));
			}
			else{
				setState(state => ({
					...state,
					loading: false,
					error: `No se pudo eliminar la marca "${marca.nombre}"`,
				}));
			}
		});
	};

	const contextState = { ...state, add, remove };

	return (
		<MarcasContext.Provider value={contextState}>
			{children}
		</MarcasContext.Provider>
	)
};

