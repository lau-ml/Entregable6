import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GastoService {
  private perPage: number = 10;

  constructor(private httpClient: HttpClient,
  ) {
  }

  crearGasto(gasto: any) {


    return this.httpClient.post("http://localhost:8080/gastos/crear", gasto);
  }

  getPerPages() {
    return this.perPage;
  }

  getGastos(page: number) {

    const url = `http://localhost:8080/gastos?page=${page}&pageSize=${this.perPage}`;

    return this.httpClient.get<any>(url).pipe(
      map((grupoData) => {
          return {
            gastos: grupoData.gastos,
            totalItems: grupoData.totalItems,
            totalPages: grupoData.totalPages,
            currentPage: grupoData.currentPage
          };
        }
      ));
  }


}
