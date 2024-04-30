import alumno from 'app/entities/alumno/alumno.reducer';
import responsableAlumno from 'app/entities/responsable-alumno/responsable-alumno.reducer';
import autorizado from 'app/entities/autorizado/autorizado.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  alumno,
  responsableAlumno,
  autorizado,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
