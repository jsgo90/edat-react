import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, ListGroup, ListGroupItem, Badge } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons'; // Importar el icono de FontAwesome
import Select from 'react-select'; // Importar React Select

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
import { getEntities as getAutorizados } from 'app/entities/autorizado/autorizado.reducer';
import { getEntity, updateEntity, createEntity, reset } from './responsable-alumno.reducer';

export const ResponsableAlumnoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const alumnos = useAppSelector(state => state.alumno.entities);
  const autorizados = useAppSelector(state => state.autorizado.entities);
  const responsableAlumnoEntity = useAppSelector(state => state.responsableAlumno.entity);
  const loading = useAppSelector(state => state.responsableAlumno.loading);
  const updating = useAppSelector(state => state.responsableAlumno.updating);
  const updateSuccess = useAppSelector(state => state.responsableAlumno.updateSuccess);

  const [selectedAlumnos, setSelectedAlumnos] = useState([]);
  const [selectedAutorizados, setSelectedAutorizados] = useState([]);

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
    dispatch(getAutorizados({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && responsableAlumnoEntity) {
      setSelectedAlumnos(responsableAlumnoEntity.alumnos?.map(alumno => alumno.id) || []);
      setSelectedAutorizados(responsableAlumnoEntity.autorizados?.map(autorizado => autorizado.id) || []);
    }
  }, [responsableAlumnoEntity, isNew]);

  const saveEntity = values => {
    const entity = {
      ...responsableAlumnoEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      alumnos: selectedAlumnos.map(id => ({ id })),
      autorizados: selectedAutorizados.map(id => ({ id })),
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
        };

  // Función para añadir un alumno seleccionado
  const handleAddAlumno = selectedOption => {
    if (selectedOption && !selectedAlumnos.includes(selectedOption.value)) {
      setSelectedAlumnos([...selectedAlumnos, selectedOption.value]);
    }
  };

  // Función para añadir un autorizado seleccionado
  const handleAddAutorizado = selectedOption => {
    if (selectedOption && !selectedAutorizados.includes(selectedOption.value)) {
      setSelectedAutorizados([...selectedAutorizados, selectedOption.value]);
    }
  };

  // Función para eliminar un alumno seleccionado de la lista
  const handleRemoveAlumno = id => {
    setSelectedAlumnos(selectedAlumnos.filter(alumnoId => alumnoId !== id));
  };

  // Función para eliminar un autorizado seleccionado de la lista
  const handleRemoveAutorizado = id => {
    setSelectedAutorizados(selectedAutorizados.filter(autorizadoId => autorizadoId !== id));
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edatApp.responsableAlumno.home.createOrEditLabel" data-cy="ResponsableAlumnoCreateUpdateHeading">
            Crear o editar Padre / Madre
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField label="Nombre" id="responsable-alumno-nombre" name="nombre" data-cy="nombre" type="text" />
              <ValidatedField label="Apellido" id="responsable-alumno-apellido" name="apellido" data-cy="apellido" type="text" />
              <ValidatedField label="Dni" id="responsable-alumno-dni" name="dni" data-cy="dni" type="text" />
              <ValidatedField label="Telefono" id="responsable-alumno-telefono" name="telefono" data-cy="telefono" type="text" />
              <ValidatedField id="responsable-alumno-user" name="user" data-cy="user" label="Usuario" type="select" required>
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {/* Select para agregar Alumnos */}
              <div className="mb-3">
                <label htmlFor="responsable-alumno-alumnos">Alumnos</label>
                <Select
                  id="responsable-alumno-alumnos"
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
              {/* Select para agregar Autorizados */}
              <div className="mb-3">
                <label htmlFor="responsable-alumno-autorizados">Autorizados</label>
                <Select
                  id="responsable-alumno-autorizados"
                  options={autorizados
                    .filter(autorizado => !selectedAutorizados.includes(autorizado.id)) // Filtrar autorizados que ya están seleccionados
                    .map(autorizado => ({
                      value: autorizado.id,
                      label: `${autorizado.nombre} ${autorizado.apellido} - ${autorizado.dni} `,
                    }))}
                  onChange={handleAddAutorizado}
                  isClearable
                />
                <ListGroup className="mt-2">
                  {selectedAutorizados.map(autorizadoId => {
                    const autorizado = autorizados.find(au => au.id === autorizadoId);
                    return (
                      <ListGroupItem key={autorizadoId} className="d-flex justify-content-between align-items-center">
                        {`${autorizado.nombre} ${autorizado.apellido} - ${autorizado.dni}`}
                        <FontAwesomeIcon
                          icon={faTimes}
                          className="text-danger"
                          style={{ cursor: 'pointer' }}
                          onClick={() => handleRemoveAutorizado(autorizadoId)}
                        />
                      </ListGroupItem>
                    );
                  })}
                </ListGroup>
              </div>
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
