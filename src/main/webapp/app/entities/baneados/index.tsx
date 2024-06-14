import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Baneados from './baneados';
import BaneadosDetail from './baneados-detail';
import BaneadosUpdate from './baneados-update';
import BaneadosDeleteDialog from './baneados-delete-dialog';

const BaneadosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Baneados />} />
    <Route path="new" element={<BaneadosUpdate />} />
    <Route path=":id">
      <Route index element={<BaneadosDetail />} />
      <Route path="edit" element={<BaneadosUpdate />} />
      <Route path="delete" element={<BaneadosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BaneadosRoutes;
