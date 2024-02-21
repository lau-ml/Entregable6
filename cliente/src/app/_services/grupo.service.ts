import {Injectable} from '@angular/core';
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {HttpClient, HttpParams} from "@angular/common/http";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GrupoService {
  private url: string = "http://localhost:8080/grupos";

  constructor(private http: HttpClient) {
  }

  createGroup(grupo: GrupoCreateRequest) {
    alert(grupo)
    return this.http.post<any>(this.url + "/crear", grupo).pipe(
      map((grupoData) => grupoData)
    );

  }



  getGroupsPaginated(page?: number,perPage?:number, categoria?: string, nombreGrupo?: string, participantes?: any[] ) {
    const options = {
      params: new HttpParams()
        .set('page', page ? page.toString() : "1")
        .set('nombreGrupo', nombreGrupo || '')
        .set('categoria', categoria || '')
        .set('pageSize', perPage ? perPage.toString() : "")
        .set('amigos', participantes ? participantes.join(",") : "")
    };


    return this.http.get<any>(this.url, options).pipe(
      map((grupoData) => {
          return {
            grupos: grupoData.grupos, // Ajusta según la estructura real de tu respuesta
            totalItems: grupoData.totalItems,
            totalPages: grupoData.totalPages,
            currentPage: grupoData.currentPage,
            itemsPerPage: grupoData.itemsPerPage
          };
        }

      )

    );
  }

  getGroup(id: number) {
    return this.http.get<any>(this.url + "/" + id).pipe(
      map((grupoData) => grupoData)
    );
  }

  updateGroup(id: number, grupo: GrupoCreateRequest) {
    return this.http.put<any>(this.url + "/" + id + "/actualizar", grupo).pipe(
      map((grupoData) => grupoData)
    );
  }
}
