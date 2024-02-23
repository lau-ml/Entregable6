import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AmigosService {

  constructor(private httpClient: HttpClient) {
  }

  getAmigos(page?: number, perPage?: number, usuario?: string) {
    const options = {
      params: {
        page: page ? page.toString() : "1",
        pageSize: perPage ? perPage.toString() : "",
        usuario: usuario || ""
      }
    };
    return this.httpClient.get<any>("http://localhost:8080/amigos", options).pipe(
      map((amigosData) => {
          return {
            amigos: amigosData.amigos,
            totalItems: amigosData.totalItems,
            totalPages: amigosData.totalPages,
            currentPage: amigosData.currentPage,
            itemsPerPage: amigosData.itemsPerPage
          };
        }
      ))
      ;
  }
}
