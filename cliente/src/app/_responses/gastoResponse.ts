export interface GastoResponse {
  responsable: string;
    id: number;
    nombreGrupo: string;
    monto: number;
    fecha: string;
    imagen: string;
    division: string;
    tipo: string;
    grupoBool: boolean;
    id_grupo: number;
    valores: Map<string, number>;
}
