import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, ListGroup, ListGroupItem } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons'; // Importar icono de FontAwesome
import Select from 'react-select'; // Importar React Select

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
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

  const [selectedAlumnos, setSelectedAlumnos] = useState([]);

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
  }, [dispatch, id, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && autorizadoEntity) {
      setSelectedAlumnos(autorizadoEntity.alumnos?.map(alumno => alumno.id) || []);
    }
  }, [autorizadoEntity, isNew]);

  const saveEntity = values => {
    const entity = {
      ...autorizadoEntity,
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
      ? {}
      : {
          ...autorizadoEntity,
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
              {/* Select para agregar Alumnos */}
              <div className="mb-3">
                <label htmlFor="autorizado-alumnos">Agregar Alumno</label>
                <Select
                  id="autorizado-alumnos"
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
