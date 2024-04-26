import type { FC } from "react";
import { BrowserRouter, Navigate, Routes, Route } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import ListAutos from "./Autos/List";
import ShowAuto from "./Autos/Show";
import CreateAuto from "./Autos/Create";
import ListMarcas from "./Marcas/List";

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
							<Route path="/marcas" element={<ListMarcas />} />
						</Routes>
					</main>
				</div>
			</div>
		</BrowserRouter>
	)
};

export default App
