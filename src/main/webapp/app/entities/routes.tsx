import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Alumno from './alumno';
import ResponsableAlumno from './responsable-alumno';
import Autorizado from './autorizado';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="alumno/*" element={<Alumno />} />
        <Route path="responsable-alumno/*" element={<ResponsableAlumno />} />
        <Route path="autorizado/*" element={<Autorizado />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
