import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAlumno } from 'app/shared/model/alumno.model';
import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
import { IAutorizado } from 'app/shared/model/autorizado.model';
import { getEntities as getAutorizados } from 'app/entities/autorizado/autorizado.reducer';
import { IHistorial } from 'app/shared/model/historial.model';
import { getEntity, updateEntity, createEntity, reset } from './historial.reducer';

export const HistorialUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alumnos = useAppSelector(state => state.alumno.entities);
  const autorizados = useAppSelector(state => state.autorizado.entities);
  const historialEntity = useAppSelector(state => state.historial.entity);
  const loading = useAppSelector(state => state.historial.loading);
  const updating = useAppSelector(state => state.historial.updating);
  const updateSuccess = useAppSelector(state => state.historial.updateSuccess);

  const handleClose = () => {
    navigate('/historial');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAlumnos({}));
    dispatch(getAutorizados({}));
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
    values.fecha = convertDateTimeToServer(values.fecha);

    const entity = {
      ...historialEntity,
      ...values,
      alumno: alumnos.find(it => it.id.toString() === values.alumno?.toString()),
      autorizado: autorizados.find(it => it.id.toString() === values.autorizado?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fecha: displayDefaultDateTime(),
        }
      : {
          ...historialEntity,
          fecha: convertDateTimeFromServer(historialEntity.fecha),
          alumno: historialEntity?.alumno?.id,
          autorizado: historialEntity?.autorizado?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edatApp.historial.home.createOrEditLabel" data-cy="HistorialCreateUpdateHeading">
            Crear o editar Historial
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="historial-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Fecha"
                id="historial-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedBlobField
                label="Autorizado Dni"
                id="historial-autorizado_dni"
                name="autorizado_dni"
                data-cy="autorizado_dni"
                isImage
                accept="image/*"
              />
              <ValidatedBlobField
                label="Autorizado Rostro"
                id="historial-autorizado_rostro"
                name="autorizado_rostro"
                data-cy="autorizado_rostro"
                isImage
                accept="image/*"
              />
              <ValidatedField id="historial-alumno" name="alumno" data-cy="alumno" label="Alumno" type="select">
                <option value="" key="0" />
                {alumnos
                  ? alumnos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.dni}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="historial-autorizado" name="autorizado" data-cy="autorizado" label="Autorizado" type="select" required>
                <option value="" key="0" />
                {autorizados
                  ? autorizados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.dni}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>Este campo es obligatorio.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/historial" replace color="info">
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

export default HistorialUpdate;
