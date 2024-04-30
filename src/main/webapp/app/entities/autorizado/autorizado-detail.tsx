import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './autorizado.reducer';

export const AutorizadoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const autorizadoEntity = useAppSelector(state => state.autorizado.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="autorizadoDetailsHeading">Autorizado</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{autorizadoEntity.id}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{autorizadoEntity.nombre}</dd>
          <dt>
            <span id="apellido">Apellido</span>
          </dt>
          <dd>{autorizadoEntity.apellido}</dd>
          <dt>
            <span id="dni">Dni</span>
          </dt>
          <dd>{autorizadoEntity.dni}</dd>
          <dt>
            <span id="telefono">Telefono</span>
          </dt>
          <dd>{autorizadoEntity.telefono}</dd>
          <dt>Alumno</dt>
          <dd>
            {autorizadoEntity.alumnos
              ? autorizadoEntity.alumnos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.dni}</a>
                    {autorizadoEntity.alumnos && i === autorizadoEntity.alumnos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/autorizado" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/autorizado/${autorizadoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AutorizadoDetail;
