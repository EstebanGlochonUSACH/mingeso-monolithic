import type { FC } from "react";
import { useLocation } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWrench } from '@fortawesome/free-solid-svg-icons/faWrench';
import { faCarSide } from '@fortawesome/free-solid-svg-icons/faCarSide';
import { faQuestion } from '@fortawesome/free-solid-svg-icons/faQuestion';
import { faCopyright } from '@fortawesome/free-solid-svg-icons/faCopyright';
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
						<div className="nav-icon"><FontAwesomeIcon icon={faQuestion} /></div> Ordenes
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