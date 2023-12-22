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

  getGroup(id: number) {
    return this.http.get<any>(`http://localhost:8080/grupo/${id}`).pipe(
      map((grupoData) => grupoData)
    );
  }

  updateGroup(id: number, grupo: GrupoCreateRequest) {
    return this.http.put<any>(`http://localhost:8080/grupo/${id}/actualizar`, grupo).pipe(
      map((grupoData) => grupoData)
    );
  }
}
