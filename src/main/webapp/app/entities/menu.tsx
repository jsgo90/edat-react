import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/alumno">
        Alumno
      </MenuItem>
      <MenuItem icon="asterisk" to="/responsable-alumno">
        Responsable Alumno
      </MenuItem>
      <MenuItem icon="asterisk" to="/autorizado">
        Autorizado
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
