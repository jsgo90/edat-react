import './home.scss';

import React from 'react';
import { Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div>
      {account?.login ? (
        <Alert color="success">¡Bienvenido, {account.login}!</Alert>
      ) : (
        <Alert color="warning">No has iniciado sesión.</Alert>
      )}
    </div>
  );
};

export default Home;
