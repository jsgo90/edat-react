import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Historial from './historial';
import HistorialDetail from './historial-detail';
import HistorialUpdate from './historial-update';
import HistorialDeleteDialog from './historial-delete-dialog';

const HistorialRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Historial />} />
    <Route path="new" element={<HistorialUpdate />} />
    <Route path=":id">
      <Route index element={<HistorialDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HistorialRoutes;
