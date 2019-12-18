import { Injectable } from '@angular/core';
import { HttpParams, HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map, catchError } from 'rxjs/operators';
import { List } from '../models/list.model';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  constructor(private http: HttpClient) { }

  getlists() {
    return this.http.get<any>(environment.urlApiRest + `lists`, { observe: 'response' })
      .pipe(map(resp => {
        return resp;
      }));
  }

  getList(sprintId) {
    return this.http.get<any>(environment.urlApiRest + `lists/sprint/${sprintId}`)
      .pipe(map(resp => {
        return resp;
      }));
  }
  editList(list: List) {
    return this.http.put<any>(environment.urlApiRest + `lists`, list)
      .pipe(map(resp => {
        return resp;
      }))
  }

  deleteList(id: number) {
    return this.http.delete<any>(environment.urlApiRest + `lists/` + id)
      .pipe(map(resp => {
        console.log(resp)
        return resp;
      }))
  }

  addList(listName:string, sprintId:number) {
    const data = {
      name: listName,
      sprint: {
        id : sprintId
      }
    }
    return this.http.post<any>(environment.urlApiRest + `lists`, data, { observe: 'response' })
      .pipe(map(resp => {
        //console.log(resp.headers.get('location'))
        return resp;
      }))
  }

  filter(tab, property){
    return tab.filter(
      (list)=> {
          return list.name === property;
      }
    )
  }
}
