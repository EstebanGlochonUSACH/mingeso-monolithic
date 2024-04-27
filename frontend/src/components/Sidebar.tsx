import type { FC } from "react";
import { useLocation } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWrench } from '@fortawesome/free-solid-svg-icons/faWrench';
import { faCarSide } from '@fortawesome/free-solid-svg-icons/faCarSide';
import { faFileLines } from '@fortawesome/free-solid-svg-icons/faFileLines';
import { faCopyright } from '@fortawesome/free-solid-svg-icons/faCopyright';
import { faChartLine } from '@fortawesome/free-solid-svg-icons/faChartLine';
import { faTag } from '@fortawesome/free-solid-svg-icons/faTag';
import Nav from 'react-bootstrap/Nav';

const Sidebar: FC = () => {
	const location = useLocation();
	return (
		<div className="sidebar">
			<div className="logo p-2">
				<FontAwesomeIcon icon={faWrench} /> AutoFix
			</div>
			<div className="sidebar-navbar">
				<Nav activeKey={location.pathname} className="flex-column w-100">
					<Nav.Link eventKey="/autos" href="/autos">
						<div className="nav-icon"><FontAwesomeIcon icon={faCarSide} /></div> Autos
					</Nav.Link>
					<Nav.Link eventKey="/ordenes" href="/ordenes">
						<div className="nav-icon"><FontAwesomeIcon icon={faFileLines} /></div> Ordenes
					</Nav.Link>
					<Nav.Link eventKey="/reportes" href="/reportes">
						<div className="nav-icon"><FontAwesomeIcon icon={faChartLine} /></div> Reportes
					</Nav.Link>
					<Nav.Link eventKey="/bonos" href="/bonos">
						<div className="nav-icon"><FontAwesomeIcon icon={faTag} /></div> Bonos
					</Nav.Link>
					<Nav.Link eventKey="/marcas" href="/marcas">
						<div className="nav-icon"><FontAwesomeIcon icon={faCopyright} /></div> Marcas
					</Nav.Link>
				</Nav>
			</div>
		</div>
	)
};

export default Sidebar;