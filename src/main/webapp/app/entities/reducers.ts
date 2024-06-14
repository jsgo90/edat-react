import alumno from 'app/entities/alumno/alumno.reducer';
import responsableAlumno from 'app/entities/responsable-alumno/responsable-alumno.reducer';
import autorizado from 'app/entities/autorizado/autorizado.reducer';
import historial from 'app/entities/historial/historial.reducer';
import baneados from 'app/entities/baneados/baneados.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  alumno,
  responsableAlumno,
  autorizado,
  historial,
  baneados,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
