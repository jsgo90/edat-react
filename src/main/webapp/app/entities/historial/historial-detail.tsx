import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './historial.reducer';

export const HistorialDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const historialEntity = useAppSelector(state => state.historial.entity);
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
          <dd>
            {historialEntity.alumno.nombre} {historialEntity.alumno.apellido} - {historialEntity.alumno.dni}{' '}
          </dd>
          <dt>Autorizado</dt>
          <dd>
            {historialEntity.autorizado.nombre} {historialEntity.autorizado.apellido} - {historialEntity.autorizado.dni}
          </dd>
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
