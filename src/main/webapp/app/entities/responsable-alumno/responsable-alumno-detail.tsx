import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './responsable-alumno.reducer';

export const ResponsableAlumnoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, [dispatch, id]);

  const responsableAlumnoEntity = useAppSelector(state => state.responsableAlumno.entity);

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="responsableAlumnoDetailsHeading">Padre / Madre</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>
            {responsableAlumnoEntity.nombre} {responsableAlumnoEntity.apellido}
          </dd>
          <dt>
            <span id="dni">DNI</span>
          </dt>
          <dd>{responsableAlumnoEntity.dni}</dd>
          <dt>
            <span id="telefono">Teléfono</span>
          </dt>
          <dd>{responsableAlumnoEntity.telefono}</dd>
          <dt>Usuario</dt>
          <dd>{responsableAlumnoEntity.user ? responsableAlumnoEntity.user.login : ''}</dd>
        </dl>
        {/* Alumnos Table */}
        <h4>Alumnos</h4>
        {responsableAlumnoEntity.alumnos && responsableAlumnoEntity.alumnos.length > 0 ? (
          <Table className="uniform-width-table" responsive>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>DNI</th>
              </tr>
            </thead>
            <tbody>
              {responsableAlumnoEntity.alumnos.map(alumno => (
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
        {/* Autorizados Table */}
        <h4>Autorizados</h4>
        {responsableAlumnoEntity.autorizados && responsableAlumnoEntity.autorizados.length > 0 ? (
          <Table className="uniform-width-table" responsive>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>DNI</th>
              </tr>
            </thead>
            <tbody>
              {responsableAlumnoEntity.autorizados.map(autorizado => (
                <tr key={autorizado.id}>
                  <td>{autorizado.nombre}</td>
                  <td>{autorizado.apellido}</td>
                  <td>{autorizado.dni}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div>No hay autorizados asociados.</div>
        )}
        {/* Action Buttons */}
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
