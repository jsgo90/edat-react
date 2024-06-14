import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './baneados.reducer';

export const BaneadosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const baneadosEntity = useAppSelector(state => state.baneados.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="baneadosDetailsHeading">Baneados</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{baneadosEntity.id}</dd>
          <dt>
            <span id="dni">Dni</span>
          </dt>
          <dd>{baneadosEntity.dni}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{baneadosEntity.nombre}</dd>
          <dt>
            <span id="apellido">Apellido</span>
          </dt>
          <dd>{baneadosEntity.apellido}</dd>
          <dt>
            <span id="motivo">Motivo</span>
          </dt>
          <dd>{baneadosEntity.motivo}</dd>
          <dt>
            <span id="fechaBaneo">Fecha Baneo</span>
          </dt>
          <dd>
            {baneadosEntity.fechaBaneo ? <TextFormat value={baneadosEntity.fechaBaneo} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Alumnos</dt>
          <dd>
            {baneadosEntity.alumnos
              ? baneadosEntity.alumnos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.dni}</a>
                    {baneadosEntity.alumnos && i === baneadosEntity.alumnos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/baneados" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/baneados/${baneadosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BaneadosDetail;
