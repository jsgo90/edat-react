import React, { useEffect, useState, CSSProperties } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Modal, ModalBody } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './historial.reducer';

export const HistorialDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const historialEntity = useAppSelector(state => state.historial.entity);

  // Estado para manejar la apertura del modal
  const [modalOpen, setModalOpen] = useState(false);
  const [modalImage, setModalImage] = useState<string | null>(null);

  // Función para abrir el modal con la imagen seleccionada
  const openImageModal = (image: string) => {
    setModalImage(image);
    setModalOpen(true);
  };

  // Función para cerrar el modal
  const closeImageModal = () => {
    setModalOpen(false);
    setModalImage(null);
  };

  // Estilos para las imágenes en paralelo y ajuste del diseño
  const containerStyle: CSSProperties = { display: 'flex', flexDirection: 'column', height: '100%' };
  const contentStyle: CSSProperties = { flexGrow: 1 };
  const buttonContainerStyle: CSSProperties = { textAlign: 'center', marginTop: '20px' };
  const imageContainerStyle: CSSProperties = { display: 'flex', justifyContent: 'flex-start', gap: '10px' }; // Ajuste para menos separación
  const imageStyle: CSSProperties = { cursor: 'pointer', maxHeight: '200px', width: 'auto' };

  return (
    <Row>
      {/* Columna para la información del historial */}
      <Col md="6" style={containerStyle}>
        <div style={contentStyle}>
          <h2 data-cy="historialDetailsHeading">Detalles del Historial</h2>
          <dl className="jh-entity-details">
            <dt>Fecha</dt>
            <dd>
              {historialEntity.fecha ? <TextFormat value={historialEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : 'No disponible'}
            </dd>
            <dt>Alumno</dt>
            <dd>{historialEntity.alumno ? `${historialEntity.alumno.nombre} ${historialEntity.alumno.apellido}` : 'No disponible'}</dd>
            <dt>Autorizado</dt>
            <dd>
              {historialEntity.autorizado ? `${historialEntity.autorizado.nombre} ${historialEntity.autorizado.apellido}` : 'No disponible'}
            </dd>
          </dl>
        </div>
        <div style={buttonContainerStyle}>
          <Button tag={Link} to="/historial" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/historial/${historialEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
          </Button>
        </div>
      </Col>

      {/* Columna para las imágenes, en paralelo */}
      <Col md="6" style={containerStyle}>
        <div style={contentStyle}>
          <h2 data-cy="historialDetailsHeading">Imágenes del Autorizado</h2>
          <div style={imageContainerStyle}>
            {historialEntity.autorizado_dni ? (
              <img
                src={`data:${historialEntity.autorizado_dniContentType};base64,${historialEntity.autorizado_dni}`}
                style={imageStyle}
                alt="DNI del autorizado"
                onClick={() => openImageModal(`data:${historialEntity.autorizado_dniContentType};base64,${historialEntity.autorizado_dni}`)}
              />
            ) : (
              <span>No disponible</span>
            )}

            {historialEntity.autorizado_rostro ? (
              <img
                src={`data:${historialEntity.autorizado_rostroContentType};base64,${historialEntity.autorizado_rostro}`}
                style={imageStyle}
                alt="Rostro del autorizado"
                onClick={() =>
                  openImageModal(`data:${historialEntity.autorizado_rostroContentType};base64,${historialEntity.autorizado_rostro}`)
                }
              />
            ) : (
              <span>No disponible</span>
            )}
          </div>
        </div>
      </Col>

      {/* Modal para mostrar la imagen ampliada */}
      <Modal isOpen={modalOpen} toggle={closeImageModal}>
        <ModalBody>{modalImage && <img src={modalImage} style={{ width: '100%' }} alt="Imagen ampliada" />}</ModalBody>
        <Button color="secondary" onClick={closeImageModal}>
          Cerrar
        </Button>
      </Modal>
    </Row>
  );
};

export default HistorialDetail;
