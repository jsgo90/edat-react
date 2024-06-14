import dayjs from 'dayjs';
import { IAlumno } from 'app/shared/model/alumno.model';

export interface IBaneados {
  id?: number;
  dni?: number | null;
  nombre?: string | null;
  apellido?: string | null;
  motivo?: string | null;
  fechaBaneo?: dayjs.Dayjs | null;
  alumnos?: IAlumno[] | null;
}

export const defaultValue: Readonly<IBaneados> = {};
