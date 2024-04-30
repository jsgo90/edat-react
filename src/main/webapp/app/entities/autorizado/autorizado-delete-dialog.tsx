import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './autorizado.reducer';

export const AutorizadoDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const autorizadoEntity = useAppSelector(state => state.autorizado.entity);
  const updateSuccess = useAppSelector(state => state.autorizado.updateSuccess);

  const handleClose = () => {
    navigate('/autorizado');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(autorizadoEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="autorizadoDeleteDialogHeading">
        Confirmar operación de borrado
      </ModalHeader>
      <ModalBody id="edatApp.autorizado.delete.question">¿Seguro que quiere eliminar Autorizado {autorizadoEntity.id}?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancelar
        </Button>
        <Button id="jhi-confirm-delete-autorizado" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Eliminar
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AutorizadoDeleteDialog;
