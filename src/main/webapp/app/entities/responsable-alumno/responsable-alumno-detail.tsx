import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './responsable-alumno.reducer';

export const ResponsableAlumnoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const responsableAlumnoEntity = useAppSelector(state => state.responsableAlumno.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="responsableAlumnoDetailsHeading">Responsable Alumno</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{responsableAlumnoEntity.id}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{responsableAlumnoEntity.nombre}</dd>
          <dt>
            <span id="apellido">Apellido</span>
          </dt>
          <dd>{responsableAlumnoEntity.apellido}</dd>
          <dt>
            <span id="dni">Dni</span>
          </dt>
          <dd>{responsableAlumnoEntity.dni}</dd>
          <dt>
            <span id="telefono">Telefono</span>
          </dt>
          <dd>{responsableAlumnoEntity.telefono}</dd>
          <dt>User</dt>
          <dd>{responsableAlumnoEntity.user ? responsableAlumnoEntity.user.login : ''}</dd>
          <dt>Alumno</dt>
          <dd>
            {responsableAlumnoEntity.alumnos
              ? responsableAlumnoEntity.alumnos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.dni}</a>
                    {responsableAlumnoEntity.alumnos && i === responsableAlumnoEntity.alumnos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Autorizado</dt>
          <dd>
            {responsableAlumnoEntity.autorizados
              ? responsableAlumnoEntity.autorizados.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.dni}</a>
                    {responsableAlumnoEntity.autorizados && i === responsableAlumnoEntity.autorizados.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/responsable-alumno" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/responsable-alumno/${responsableAlumnoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ResponsableAlumnoDetail;
