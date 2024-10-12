import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAlumno } from 'app/shared/model/alumno.model';
import { getEntity, updateEntity, createEntity, reset } from './alumno.reducer';

export const AlumnoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alumnoEntity = useAppSelector(state => state.alumno.entity);
  const loading = useAppSelector(state => state.alumno.loading);
  const updating = useAppSelector(state => state.alumno.updating);
  const updateSuccess = useAppSelector(state => state.alumno.updateSuccess);

  const handleClose = () => {
    navigate('/alumno');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...alumnoEntity,
      ...values,
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
          ...alumnoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edatApp.alumno.home.createOrEditLabel" data-cy="AlumnoCreateUpdateHeading">
            Crear o editar Alumno
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField label="Nombre" id="alumno-nombre" name="nombre" data-cy="nombre" type="text" />
              <ValidatedField label="Apellido" id="alumno-apellido" name="apellido" data-cy="apellido" type="text" />
              <ValidatedField label="Dni" id="alumno-dni" name="dni" data-cy="dni" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/alumno" replace color="info">
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

export default AlumnoUpdate;
