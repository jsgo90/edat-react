export interface IAlumno {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  dni?: number | null;
}

export const defaultValue: Readonly<IAlumno> = {};
