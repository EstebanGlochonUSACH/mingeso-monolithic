import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: 3000,
    strictPort: true,
    proxy: {
      '/api': process.env.BACKEND_URI ? process.env.BACKEND_URI : 'http://localhost:8080',
    }
  },
  plugins: [react()],
})
