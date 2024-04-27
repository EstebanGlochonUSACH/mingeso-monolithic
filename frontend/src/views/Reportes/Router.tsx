import { useState, type FC } from "react";
import { type Location, Navigate, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import ViewReporte1 from "./Reporte1";
import ViewReporte2 from "./Reporte2";
import ViewReporte3 from "./Reporte3";

const DEFAULT_ROUTE = 'reparaciones-tipo-auto';

function createDefaultState(location: Location){
	const path = location.pathname.split('/').pop();
	return path || DEFAULT_ROUTE;
}

const RouterReportes: FC = () => {
	const location = useLocation();
	const navigate = useNavigate();
	const [tab, setTab] = useState<string|null>(createDefaultState(location));
	const handleSelectTab = (eventKey: string|null) => {
		if(eventKey){
			setTab(eventKey);
			navigate(eventKey);
		}
	};
	return (
		<Container className="py-2">
			<Nav className="mb-2" variant="pills" activeKey={tab || DEFAULT_ROUTE} onSelect={handleSelectTab}>
				<Nav.Item>
					<Nav.Link eventKey={DEFAULT_ROUTE}>Reparaciones / Tipo Auto</Nav.Link>
				</Nav.Item>
				<Nav.Item>
					<Nav.Link eventKey="tiempo-reparacion">Tiempos de Reparaci&oacute;n</Nav.Link>
				</Nav.Item>
				<Nav.Item>
					<Nav.Link eventKey="reparaciones-tipo-motor">Reparaciones / Tipo Motor</Nav.Link>
				</Nav.Item>
			</Nav>
			<Routes>
				<Route path="" element={<Navigate to={DEFAULT_ROUTE} />} />
				<Route path="/reparaciones-tipo-auto" element={<ViewReporte1 />} />
				<Route path="/tiempo-reparacion" element={<ViewReporte2 />} />
				<Route path="/reparaciones-tipo-motor" element={<ViewReporte3 />} />
			</Routes>
		</Container>
	);
};

export default RouterReportes;