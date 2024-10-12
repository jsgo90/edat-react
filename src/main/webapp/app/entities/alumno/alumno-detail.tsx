import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alumno.reducer';

export const AlumnoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alumnoEntity = useAppSelector(state => state.alumno.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alumnoDetailsHeading">Alumno</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>
            {alumnoEntity.nombre} {alumnoEntity.apellido}
          </dd>
          <dt>
            <span id="dni">DNI</span>
          </dt>
          <dd>{alumnoEntity.dni}</dd>
        </dl>
        <Button tag={Link} to="/alumno" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/alumno/${alumnoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlumnoDetail;
