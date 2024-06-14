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
import { IBaneados } from 'app/shared/model/baneados.model';
import { getEntity, updateEntity, createEntity, reset } from './baneados.reducer';

export const BaneadosUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alumnos = useAppSelector(state => state.alumno.entities);
  const baneadosEntity = useAppSelector(state => state.baneados.entity);
  const loading = useAppSelector(state => state.baneados.loading);
  const updating = useAppSelector(state => state.baneados.updating);
  const updateSuccess = useAppSelector(state => state.baneados.updateSuccess);

  const handleClose = () => {
    navigate('/baneados');
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
    values.fechaBaneo = convertDateTimeToServer(values.fechaBaneo);

    const entity = {
      ...baneadosEntity,
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
      ? {
          fechaBaneo: displayDefaultDateTime(),
        }
      : {
          ...baneadosEntity,
          fechaBaneo: convertDateTimeFromServer(baneadosEntity.fechaBaneo),
          alumnos: baneadosEntity?.alumnos?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edatApp.baneados.home.createOrEditLabel" data-cy="BaneadosCreateUpdateHeading">
            Crear o editar Baneados
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="baneados-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Dni" id="baneados-dni" name="dni" data-cy="dni" type="text" />
              <ValidatedField label="Nombre" id="baneados-nombre" name="nombre" data-cy="nombre" type="text" />
              <ValidatedField label="Apellido" id="baneados-apellido" name="apellido" data-cy="apellido" type="text" />
              <ValidatedField label="Motivo" id="baneados-motivo" name="motivo" data-cy="motivo" type="text" />
              <ValidatedField
                label="Fecha Baneo"
                id="baneados-fechaBaneo"
                name="fechaBaneo"
                data-cy="fechaBaneo"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Alumnos" id="baneados-alumnos" data-cy="alumnos" type="select" multiple name="alumnos">
                <option value="" key="0" />
                {alumnos
                  ? alumnos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.dni}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/baneados" replace color="info">
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

export default BaneadosUpdate;
