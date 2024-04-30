import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './responsable-alumno.reducer';

export const ResponsableAlumno = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const responsableAlumnoList = useAppSelector(state => state.responsableAlumno.entities);
  const loading = useAppSelector(state => state.responsableAlumno.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="responsable-alumno-heading" data-cy="ResponsableAlumnoHeading">
        Responsable Alumnos
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refrescar lista
          </Button>
          <Link
            to="/responsable-alumno/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Crear nuevo Responsable Alumno
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {responsableAlumnoList && responsableAlumnoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  Nombre <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('apellido')}>
                  Apellido <FontAwesomeIcon icon={getSortIconByFieldName('apellido')} />
                </th>
                <th className="hand" onClick={sort('dni')}>
                  Dni <FontAwesomeIcon icon={getSortIconByFieldName('dni')} />
                </th>
                <th className="hand" onClick={sort('telefono')}>
                  Telefono <FontAwesomeIcon icon={getSortIconByFieldName('telefono')} />
                </th>
                <th>
                  User <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Alumno <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {responsableAlumnoList.map((responsableAlumno, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/responsable-alumno/${responsableAlumno.id}`} color="link" size="sm">
                      {responsableAlumno.id}
                    </Button>
                  </td>
                  <td>{responsableAlumno.nombre}</td>
                  <td>{responsableAlumno.apellido}</td>
                  <td>{responsableAlumno.dni}</td>
                  <td>{responsableAlumno.telefono}</td>
                  <td>{responsableAlumno.user ? responsableAlumno.user.login : ''}</td>
                  <td>
                    {responsableAlumno.alumnos
                      ? responsableAlumno.alumnos.map((val, j) => (
                          <span key={j}>
                            <Link to={`/alumno/${val.id}`}>{val.dni}</Link>
                            {j === responsableAlumno.alumnos.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/responsable-alumno/${responsableAlumno.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Vista</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/responsable-alumno/${responsableAlumno.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/responsable-alumno/${responsableAlumno.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Eliminar</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Ningún Responsable Alumnos encontrado</div>
        )}
      </div>
    </div>
  );
};

export default ResponsableAlumno;
