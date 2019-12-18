import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SprintService {

  constructor(private http: HttpClient) { }

  getAllSprints(): Observable<any> {
    return this.http.get<any>(environment.urlApiRest + `sprints`)
      .pipe(map(resp => {
        return resp;
      }));
  }

  addSprint(sprint): Observable<any> {
    const data = {
      name: sprint.name,
      startDate: sprint.startDate
    }
    return this.http.post<any>(environment.urlApiRest + `sprints`, data)
      .pipe(map(resp => {
        return resp;
      }));
  }

  editSprint(sprint): Observable<any> {
    const data = {
      name: sprint.name,
      startDate: sprint.startDate
    }
    return this.http.put<any>(environment.urlApiRest + `sprints/${sprint.id}`, data)
      .pipe(map(resp => {
        return resp;
      }))
  }

  deleteSprint(id: number): Observable<any> {
    return this.http.delete<any>(environment.urlApiRest + `sprints/${id}`)
      .pipe(map(resp => {
        // console.log(resp)
        return resp;
      }))
  }

  getSprint(id: number): Observable<any> {
    return this.http.get<any>(environment.urlApiRest + `sprints/${id}`)
      .pipe(map(resp => {
        // console.log(resp)
        return resp;
      }))
  }
}
