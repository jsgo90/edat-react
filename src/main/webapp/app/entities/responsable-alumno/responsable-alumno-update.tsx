import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAlumno } from 'app/shared/model/alumno.model';
import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
import { IResponsableAlumno } from 'app/shared/model/responsable-alumno.model';
import { getEntity, updateEntity, createEntity, reset } from './responsable-alumno.reducer';

export const ResponsableAlumnoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const alumnos = useAppSelector(state => state.alumno.entities);
  const responsableAlumnoEntity = useAppSelector(state => state.responsableAlumno.entity);
  const loading = useAppSelector(state => state.responsableAlumno.loading);
  const updating = useAppSelector(state => state.responsableAlumno.updating);
  const updateSuccess = useAppSelector(state => state.responsableAlumno.updateSuccess);

  const handleClose = () => {
    navigate('/responsable-alumno');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
      ...responsableAlumnoEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          ...responsableAlumnoEntity,
          user: responsableAlumnoEntity?.user?.id,
          alumnos: responsableAlumnoEntity?.alumnos?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edatApp.responsableAlumno.home.createOrEditLabel" data-cy="ResponsableAlumnoCreateUpdateHeading">
            Crear o editar Responsable Alumno
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="responsable-alumno-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Nombre" id="responsable-alumno-nombre" name="nombre" data-cy="nombre" type="text" />
              <ValidatedField label="Apellido" id="responsable-alumno-apellido" name="apellido" data-cy="apellido" type="text" />
              <ValidatedField label="Dni" id="responsable-alumno-dni" name="dni" data-cy="dni" type="text" />
              <ValidatedField label="Telefono" id="responsable-alumno-telefono" name="telefono" data-cy="telefono" type="text" />
              <ValidatedField id="responsable-alumno-user" name="user" data-cy="user" label="User" type="select" required>
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>Este campo es obligatorio.</FormText>
              <ValidatedField label="Alumno" id="responsable-alumno-alumno" data-cy="alumno" type="select" multiple name="alumnos">
                <option value="" key="0" />
                {alumnos
                  ? alumnos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.dni}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/responsable-alumno" replace color="info">
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

export default ResponsableAlumnoUpdate;
