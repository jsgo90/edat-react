import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './baneados.reducer';

export const BaneadosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, [dispatch, id]);

  const baneadosEntity = useAppSelector(state => state.baneados.entity);

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="baneadosDetailsHeading">Persona no autorizada</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>
            {baneadosEntity.nombre} {baneadosEntity.apellido}
          </dd>
          <dt>
            <span id="dni">DNI</span>
          </dt>
          <dd>{baneadosEntity.dni}</dd>
          <dt>
            <span id="motivo">Motivo</span>
          </dt>
          <dd>{baneadosEntity.motivo}</dd>
          <dt>
            <span id="fechaBaneo">Fecha de Baneo</span>
          </dt>
          <dd>
            {baneadosEntity.fechaBaneo ? <TextFormat value={baneadosEntity.fechaBaneo} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        {/* Tabla de Alumnos */}
        <h4>Alumnos Asociados</h4>
        {baneadosEntity.alumnos && baneadosEntity.alumnos.length > 0 ? (
          <Table className="uniform-width-table" responsive>
            <thead>
              <tr>
                <th>DNI</th>
                <th>Nombre</th>
                <th>Apellido</th>
              </tr>
            </thead>
            <tbody>
              {baneadosEntity.alumnos.map(alumno => (
                <tr key={alumno.id}>
                  <td>{alumno.dni}</td>
                  <td>{alumno.nombre}</td>
                  <td>{alumno.apellido}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div>No hay alumnos asociados.</div>
        )}
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
