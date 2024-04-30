import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Autorizado from './autorizado';
import AutorizadoDetail from './autorizado-detail';
import AutorizadoUpdate from './autorizado-update';
import AutorizadoDeleteDialog from './autorizado-delete-dialog';

const AutorizadoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Autorizado />} />
    <Route path="new" element={<AutorizadoUpdate />} />
    <Route path=":id">
      <Route index element={<AutorizadoDetail />} />
      <Route path="edit" element={<AutorizadoUpdate />} />
      <Route path="delete" element={<AutorizadoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AutorizadoRoutes;
