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


  getGastos(page?: number, tipoGasto?: string, fechaDesde?: string, fechaHasta?: string, nombreGrupo?: string) {

    const options={

      params:new HttpParams()
        .set('page',page? page.toString():'')
        .set('tipoGasto',tipoGasto? tipoGasto:'')
        .set('fechaDesde',fechaDesde ? new Date(fechaDesde).toISOString() : '')
        .set('fechaHasta',fechaHasta ? new Date(fechaHasta).toISOString() : '')
        .set('nombreGrupo',nombreGrupo? nombreGrupo:'')
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
