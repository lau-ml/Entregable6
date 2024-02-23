import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GastoService {
  private url: string = "http://localhost:8080/gastos";
  constructor(private httpClient: HttpClient,
  ) {
  }

  crearGasto(gasto: any) {

    return this.httpClient.post(this.url+"/crear", gasto);
  }


  updateGasto(id: number, gasto: any) {
    return this.httpClient.put(this.url + "/" + id + "/actualizar", gasto);
  }


  getGasto(id: number) {
    return this.httpClient.get<any>(this.url + "/" + id).pipe(
      map((gastoData) => {
        return {
          valores: gastoData.valores,
          id: gastoData.id,
          nombreGrupo: gastoData.nombreGrupo,
          monto: gastoData.monto,
          fecha: gastoData.fecha,
          responsable: gastoData.responsable,
          imagen: gastoData.imagen,
          division: gastoData.division,
          tipo: gastoData.tipo,
          grupoBool: !!gastoData.id_grupo,
          id_grupo: gastoData.id_grupo
        };
      }));
  }
  getGastos(page?: number, tipoGasto?: string, fechaDesde?: string, fechaHasta?: string, nombreGrupo?: string,grupoId?:number) {

    const options={

      params:new HttpParams()
        .set('page',page? page.toString():'')
        .set('tipoGasto',tipoGasto? tipoGasto:'')
        .set('fechaDesde',fechaDesde ? new Date(fechaDesde).toISOString() : '')
        .set('fechaHasta',fechaHasta ? new Date(fechaHasta).toISOString() : '')
        .set('nombreGrupo',nombreGrupo? nombreGrupo:'')
        .set('grupoId',grupoId? grupoId.toString():'')
    }
    return this.httpClient.get<any>(this.url, options).pipe(
      map((grupoData) => {
          return {
            gastos: grupoData.gastos,
            totalItems: grupoData.totalItems,
            totalPages: grupoData.totalPages,
            currentPage: grupoData.currentPage,
            itemsPerPage: grupoData.itemsPerPage
          };
        }
      ));
  }


}
