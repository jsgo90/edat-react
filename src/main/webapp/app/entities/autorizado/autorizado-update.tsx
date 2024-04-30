import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAlumno } from 'app/shared/model/alumno.model';
import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
import { IAutorizado } from 'app/shared/model/autorizado.model';
import { getEntity, updateEntity, createEntity, reset } from './autorizado.reducer';

export const AutorizadoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alumnos = useAppSelector(state => state.alumno.entities);
  const autorizadoEntity = useAppSelector(state => state.autorizado.entity);
  const loading = useAppSelector(state => state.autorizado.loading);
  const updating = useAppSelector(state => state.autorizado.updating);
  const updateSuccess = useAppSelector(state => state.autorizado.updateSuccess);

  const handleClose = () => {
    navigate('/autorizado');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAlumnos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.dni !== undefined && typeof values.dni !== 'number') {
      values.dni = Number(values.dni);
    }

    const entity = {
      ...autorizadoEntity,
      ...values,
      alumnos: mapIdList(values.alumnos),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...autorizadoEntity,
          alumnos: autorizadoEntity?.alumnos?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edatApp.autorizado.home.createOrEditLabel" data-cy="AutorizadoCreateUpdateHeading">
            Crear o editar Autorizado
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="autorizado-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Nombre" id="autorizado-nombre" name="nombre" data-cy="nombre" type="text" />
              <ValidatedField label="Apellido" id="autorizado-apellido" name="apellido" data-cy="apellido" type="text" />
              <ValidatedField label="Dni" id="autorizado-dni" name="dni" data-cy="dni" type="text" />
              <ValidatedField label="Telefono" id="autorizado-telefono" name="telefono" data-cy="telefono" type="text" />
              <ValidatedField label="Alumno" id="autorizado-alumno" data-cy="alumno" type="select" multiple name="alumnos">
                <option value="" key="0" />
                {alumnos
                  ? alumnos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.dni}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/autorizado" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Volver</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Guardar
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AutorizadoUpdate;
