import { Injectable } from '@angular/core';
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GrupoService {

  constructor(private http: HttpClient) { }

  createGroup(grupo:GrupoCreateRequest) {
    return this.http.post<any>("http://localhost:8080/grupo/crear", grupo).pipe(
      map((grupoData) => grupoData)
    );

  }
  getGroups(page: number) {
    const perPage = 3;
    const url = `http://localhost:8080/grupo/todos?page=${page}&pageSize=${perPage}`;

    return this.http.get<any>(url).pipe(
      map((grupoData) => {
        return {
          grupos: grupoData.grupos, // Ajusta según la estructura real de tu respuesta
          totalItems: grupoData.totalItems,
          totalPages: grupoData.totalPages,
          currentPage: grupoData.currentPage
        };
      }
    ));
  }
}
