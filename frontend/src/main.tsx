import React from 'react';
import moment from 'moment';
import 'moment/locale/es';
import ReactDOM from 'react-dom/client';
import App from './views/App.tsx';
import './scss/index.scss';
import { MarcasProvider } from './services/Marcas/Marcas.tsx';

moment.locale('es');

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<MarcasProvider>
			<App />
		</MarcasProvider>
	</React.StrictMode>,
)
