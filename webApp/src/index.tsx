import React from 'react';
import ReactDOM from 'react-dom/client';
import { LaughterSegments } from './components/LaughterSegments/LaughterSegments';

const rootElement = document.getElementById('root');
if (!rootElement) throw new Error('Failed to find the root element');

ReactDOM.createRoot(rootElement).render(
  <React.StrictMode>
    <LaughterSegments />
  </React.StrictMode>
);