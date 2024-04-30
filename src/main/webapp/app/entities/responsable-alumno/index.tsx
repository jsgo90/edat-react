import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ResponsableAlumno from './responsable-alumno';
import ResponsableAlumnoDetail from './responsable-alumno-detail';
import ResponsableAlumnoUpdate from './responsable-alumno-update';
import ResponsableAlumnoDeleteDialog from './responsable-alumno-delete-dialog';

const ResponsableAlumnoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ResponsableAlumno />} />
    <Route path="new" element={<ResponsableAlumnoUpdate />} />
    <Route path=":id">
      <Route index element={<ResponsableAlumnoDetail />} />
      <Route path="edit" element={<ResponsableAlumnoUpdate />} />
      <Route path="delete" element={<ResponsableAlumnoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ResponsableAlumnoRoutes;
