import './home.scss';

import React from 'react';
import { Button, Container, Row, Col, Card, CardBody, CardTitle, CardText } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserShield, faUsers, faClipboardList, faUserLock, faGraduationCap } from '@fortawesome/free-solid-svg-icons';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className={`home-container ${!account?.login ? 'unauthenticated' : ''}`}>
      {!account?.login ? (
        <div className="home-unauthenticated">
          <div className="background-overlay">
            <div className="branding-container">
              <img src="/content/images/me-gusta.png" alt="Logo" className="app-logo" />
              <h1 className="app-title">EDAT</h1>
              <h2 className="app-subtitle">Protegiendo lo más valioso</h2>
              <Button color="primary" href="/login" size="lg" className="login-button">
                Iniciar Sesión
              </Button>
            </div>
          </div>
        </div>
      ) : (
        <div className="authenticated-content">
          <Container>
            <h1 className="display-3">¡Bienvenido, {account.login}!</h1>

            <Row className="mt-5 mb-5">
              {/* Gestión de Alumnos */}
              <Col md="4" className="d-flex align-items-stretch">
                <Card className="info-card flex-fill">
                  <CardBody>
                    <FontAwesomeIcon icon={faGraduationCap} size="3x" className="mb-3 text-primary" />
                    <CardTitle tag="h5">Gestionar Alumnos</CardTitle>
                    <CardText>Administra la información de los alumnos.</CardText>
                    <Button color="primary" href="/alumno">
                      Alumnos
                    </Button>
                  </CardBody>
                </Card>
              </Col>

              {/* Gestión de Padres */}
              <Col md="4" className="d-flex align-items-stretch">
                <Card className="info-card flex-fill">
                  <CardBody>
                    <FontAwesomeIcon icon={faUsers} size="3x" className="mb-3 text-success" />
                    <CardTitle tag="h5">Gestionar Padres</CardTitle>
                    <CardText>Administra la información de los padres y tutores.</CardText>
                    <Button color="success" href="/responsable-alumno">
                      Gestionar Padres
                    </Button>
                  </CardBody>
                </Card>
              </Col>

              {/* Gestión de Autorizados */}
              <Col md="4" className="d-flex align-items-stretch">
                <Card className="info-card flex-fill">
                  <CardBody>
                    <FontAwesomeIcon icon={faUserShield} size="3x" className="mb-3 text-info" />
                    <CardTitle tag="h5">Autorizados</CardTitle>
                    <CardText>Administra a las personas autorizadas para retirar a los niños del colegio.</CardText>
                    <Button color="info" href="/autorizado">
                      Gestionar Autorizados
                    </Button>
                  </CardBody>
                </Card>
              </Col>

              {/* Gestión de No Autorizados */}
              <Col md="4" className="d-flex align-items-stretch mt-4">
                <Card className="info-card flex-fill">
                  <CardBody>
                    <FontAwesomeIcon icon={faUserLock} size="3x" className="mb-3 text-danger" />
                    <CardTitle tag="h5">No Autorizados</CardTitle>
                    <CardText>Administra las personas que tienen restricciones de acceso.</CardText>
                    <Button color="danger" href="/baneados">
                      Gestionar No Autorizados
                    </Button>
                  </CardBody>
                </Card>
              </Col>

              {/* Ver Registros de Retiro */}
              <Col md="4" className="d-flex align-items-stretch mt-4">
                <Card className="info-card flex-fill">
                  <CardBody>
                    <FontAwesomeIcon icon={faClipboardList} size="3x" className="mb-3 text-warning" />
                    <CardTitle tag="h5">Registros de Retiro</CardTitle>
                    <CardText>Consulta los registros de los retiros realizados.</CardText>
                    <Button color="warning" href="/historial">
                      Ver Registros
                    </Button>
                  </CardBody>
                </Card>
              </Col>
            </Row>
          </Container>
        </div>
      )}
    </div>
  );
};

export default Home;
