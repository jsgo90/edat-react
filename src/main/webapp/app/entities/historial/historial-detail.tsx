import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './historial.reducer';

export const HistorialDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dispatch(getEntity(id)).then(() => {
      setLoading(false);
    });
  }, [dispatch, id]);

  const historialEntity = useAppSelector(state => state.historial.entity);

  if (loading) {
    return <div>Cargando datos...</div>;
  }

  if (!historialEntity || !historialEntity.alumno || !historialEntity.autorizado) {
    return <div>Error al cargar los datos</div>;
  }

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historialDetailsHeading">Historial</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fecha">Fecha</span>
          </dt>
          <dd>{historialEntity.fecha ? <TextFormat value={historialEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Alumno</dt>
          {historialEntity.alumno.nombre} {historialEntity.alumno.apellido} - {historialEntity.alumno.dni}
          <dt>Autorizado</dt>
          {historialEntity.autorizado.nombre} {historialEntity.autorizado.apellido} - {historialEntity.autorizado.dni}
        </dl>
        <Button tag={Link} to="/historial" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
      </Col>
    </Row>
  );
};

export default HistorialDetail;
