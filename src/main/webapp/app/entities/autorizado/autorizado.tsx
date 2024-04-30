import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './autorizado.reducer';

export const Autorizado = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const autorizadoList = useAppSelector(state => state.autorizado.entities);
  const loading = useAppSelector(state => state.autorizado.loading);

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
      <h2 id="autorizado-heading" data-cy="AutorizadoHeading">
        Autorizados
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refrescar lista
          </Button>
          <Link to="/autorizado/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Crear nuevo Autorizado
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {autorizadoList && autorizadoList.length > 0 ? (
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
                  Alumno <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {autorizadoList.map((autorizado, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/autorizado/${autorizado.id}`} color="link" size="sm">
                      {autorizado.id}
                    </Button>
                  </td>
                  <td>{autorizado.nombre}</td>
                  <td>{autorizado.apellido}</td>
                  <td>{autorizado.dni}</td>
                  <td>{autorizado.telefono}</td>
                  <td>
                    {autorizado.alumnos
                      ? autorizado.alumnos.map((val, j) => (
                          <span key={j}>
                            <Link to={`/alumno/${val.id}`}>{val.dni}</Link>
                            {j === autorizado.alumnos.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/autorizado/${autorizado.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Vista</span>
                      </Button>
                      <Button tag={Link} to={`/autorizado/${autorizado.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/autorizado/${autorizado.id}/delete`)}
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
          !loading && <div className="alert alert-warning">Ningún Autorizados encontrado</div>
        )}
      </div>
    </div>
  );
};

export default Autorizado;
