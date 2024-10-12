import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, ListGroup, ListGroupItem } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons'; // Importar icono de FontAwesome
import Select from 'react-select'; // Importar React Select

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
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

  const [selectedAlumnos, setSelectedAlumnos] = useState([]);

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
  }, [dispatch, id, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && baneadosEntity) {
      setSelectedAlumnos(baneadosEntity.alumnos?.map(alumno => alumno.id) || []);
    }
  }, [baneadosEntity, isNew]);

  const saveEntity = values => {
    values.fechaBaneo = convertDateTimeToServer(values.fechaBaneo);

    const entity = {
      ...baneadosEntity,
      ...values,
      alumnos: selectedAlumnos.map(id => ({ id })),
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
        };

  // Función para añadir un alumno seleccionado
  const handleAddAlumno = selectedOption => {
    if (selectedOption && !selectedAlumnos.includes(selectedOption.value)) {
      setSelectedAlumnos([...selectedAlumnos, selectedOption.value]);
    }
  };

  // Función para eliminar un alumno seleccionado de la lista
  const handleRemoveAlumno = id => {
    setSelectedAlumnos(selectedAlumnos.filter(alumnoId => alumnoId !== id));
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
              {/* Select para agregar Alumnos */}
              <div className="mb-3">
                <label htmlFor="baneados-alumnos">Agregar Alumnos</label>
                <Select
                  id="baneados-alumnos"
                  options={alumnos
                    .filter(alumno => !selectedAlumnos.includes(alumno.id)) // Filtrar alumnos que ya están seleccionados
                    .map(alumno => ({
                      value: alumno.id,
                      label: `${alumno.nombre} ${alumno.apellido} - ${alumno.dni}`,
                    }))}
                  onChange={handleAddAlumno}
                  isClearable
                />
                <ListGroup className="mt-2">
                  {selectedAlumnos.map(alumnoId => {
                    const alumno = alumnos.find(al => al.id === alumnoId);
                    return (
                      <ListGroupItem key={alumnoId} className="d-flex justify-content-between align-items-center">
                        {`${alumno.nombre} ${alumno.apellido} - ${alumno.dni}`}
                        <FontAwesomeIcon
                          icon={faTimes}
                          className="text-danger"
                          style={{ cursor: 'pointer' }}
                          onClick={() => handleRemoveAlumno(alumnoId)}
                        />
                      </ListGroupItem>
                    );
                  })}
                </ListGroup>
              </div>
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
