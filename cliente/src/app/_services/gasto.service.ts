import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
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


  getGastos(page: number) {

    const url = `http://localhost:8080/gastos?page=${page}`;
    const options={

    }
    return this.httpClient.get<any>(url).pipe(
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
