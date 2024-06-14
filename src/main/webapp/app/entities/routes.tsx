import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Alumno from './alumno';
import ResponsableAlumno from './responsable-alumno';
import Autorizado from './autorizado';
import Historial from './historial';
import Baneados from './baneados';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="alumno/*" element={<Alumno />} />
        <Route path="responsable-alumno/*" element={<ResponsableAlumno />} />
        <Route path="autorizado/*" element={<Autorizado />} />
        <Route path="historial/*" element={<Historial />} />
        <Route path="baneados/*" element={<Baneados />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
