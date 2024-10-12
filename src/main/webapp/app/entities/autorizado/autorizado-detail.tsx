import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './autorizado.reducer';

export const AutorizadoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, [dispatch, id]);

  const autorizadoEntity = useAppSelector(state => state.autorizado.entity);

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="autorizadoDetailsHeading">Autorizado</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>
            {autorizadoEntity.nombre} {autorizadoEntity.apellido}
          </dd>
          <dt>
            <span id="dni">DNI</span>
          </dt>
          <dd>{autorizadoEntity.dni}</dd>
          <dt>
            <span id="telefono">Teléfono</span>
          </dt>
          <dd>{autorizadoEntity.telefono}</dd>
        </dl>
        {/* Alumnos Table */}
        <h4>Alumnos Asociados</h4>
        {autorizadoEntity.alumnos && autorizadoEntity.alumnos.length > 0 ? (
          <Table className="uniform-width-table" responsive>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>DNI</th>
              </tr>
            </thead>
            <tbody>
              {autorizadoEntity.alumnos.map(alumno => (
                <tr key={alumno.id}>
                  <td>{alumno.nombre}</td>
                  <td>{alumno.apellido}</td>
                  <td>{alumno.dni}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div>No hay alumnos asociados.</div>
        )}
        {/* Action Buttons */}
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
