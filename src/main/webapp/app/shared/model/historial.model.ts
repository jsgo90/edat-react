import dayjs from 'dayjs';
import { IAlumno } from 'app/shared/model/alumno.model';
import { IAutorizado } from 'app/shared/model/autorizado.model';

export interface IHistorial {
  id?: number;
  fecha?: dayjs.Dayjs;
  alumno?: IAlumno | null;
  autorizado?: IAutorizado;
}

export const defaultValue: Readonly<IHistorial> = {};
