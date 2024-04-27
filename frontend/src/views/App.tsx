import type { FC } from "react";
import { BrowserRouter, Navigate, Routes, Route } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import ListAutos from "./Autos/List";
import ShowAuto from "./Autos/Show";
import CreateAuto from "./Autos/Create";
import ListMarcas from "./Marcas/List";
import ListOrdenes from "./Ordenes/List";
import ShowOrden from "./Ordenes/Show";
import ListBonos from "./Bonos/List";
import RouterReportes from "./Reportes/Router";

const App: FC = () => {
	return (
		<BrowserRouter>
			<div className="layout-container">
				<div className="layout-sidebar">
					<Sidebar />
				</div>
				<div className="layout-routes">
					<main>
						<Routes>
							<Route path="/" element={<Navigate to="/autos" />} />
							<Route path="/autos" element={<ListAutos />} />
							<Route path="/auto/create" element={<CreateAuto />} />
							<Route path="/auto/:id" element={<ShowAuto />} />
							<Route path="/ordenes" element={<ListOrdenes />} />
							<Route path="/orden/:id" element={<ShowOrden />} />
							<Route path="/marcas" element={<ListMarcas />} />
							<Route path="/bonos" element={<ListBonos />} />
							<Route path="/reportes/*" element={<RouterReportes />} />
						</Routes>
					</main>
				</div>
			</div>
		</BrowserRouter>
	)
};

export default App
