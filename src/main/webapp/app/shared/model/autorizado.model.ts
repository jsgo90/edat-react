import { IAlumno } from 'app/shared/model/alumno.model';

export interface IAutorizado {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  dni?: number | null;
  telefono?: string | null;
  alumnos?: IAlumno[] | null;
}

export const defaultValue: Readonly<IAutorizado> = {};
