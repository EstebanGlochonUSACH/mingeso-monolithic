import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './views/App.tsx';
import './scss/index.scss';
import { MarcasProvider } from './services/Marcas/Marcas.tsx';

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<MarcasProvider>
			<App />
		</MarcasProvider>
	</React.StrictMode>,
)
