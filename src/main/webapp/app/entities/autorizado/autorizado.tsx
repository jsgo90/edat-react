import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Row } from 'reactstrap';
import { Translate, JhiPagination, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ITEMS_PER_PAGE, ASC, DESC } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './autorizado.reducer';

export const Autorizado = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  // Inicializar el estado de paginación y ordenación correctamente
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(
      {
        ...getSortState(pageLocation, 'nombre'), // Configuración inicial de ordenación
        itemsPerPage: 10, // Añadir itemsPerPage para la paginación
        activePage: 1, // Añadir activePage para la paginación
      },
      pageLocation.search,
    ),
  );

  const autorizadoList = useAppSelector(state => state.autorizado.entities);
  const loading = useAppSelector(state => state.autorizado.loading);
  const totalItems = useAppSelector(state => state.autorizado.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1, // La API espera cero indexación para la página
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage => {
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
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
            <FontAwesomeIcon icon="sync" spin={loading} />
          </Button>
          <Link to="/autorizado/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Nuevo Autorizado
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {autorizadoList && autorizadoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('nombre')}>
                  Nombre <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('apellido')}>
                  Apellido <FontAwesomeIcon icon={getSortIconByFieldName('apellido')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {autorizadoList.map((autorizado, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{autorizado.nombre}</td>
                  <td>{autorizado.apellido}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/autorizado/${autorizado.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline"></span>
                      </Button>
                      <Button tag={Link} to={`/autorizado/${autorizado.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline"></span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/autorizado/${autorizado.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline"></span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Ningún Autorizado encontrado</div>
        )}
      </div>
      {totalItems ? (
        <div className={autorizadoList && autorizadoList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Autorizado;
