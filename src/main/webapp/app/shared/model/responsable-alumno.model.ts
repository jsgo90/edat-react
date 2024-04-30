import { IUser } from 'app/shared/model/user.model';
import { IAlumno } from 'app/shared/model/alumno.model';

export interface IResponsableAlumno {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  dni?: number | null;
  telefono?: string | null;
  user?: IUser;
  alumnos?: IAlumno[] | null;
}

export const defaultValue: Readonly<IResponsableAlumno> = {};
