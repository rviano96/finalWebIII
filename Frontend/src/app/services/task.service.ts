import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { Task } from '../models/task.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient) { }

  getTasks() {
    return this.http.get<any>(environment.urlApiRest + `tasks`)
      .pipe(map(resp => {
        return resp;
      }));
  }

  moveTask(taskId: number, listId: number): Observable<any> {
    return this.http.put<any>(environment.urlApiRest + `tasks/move/${listId}/${taskId}`, "")
      .pipe(map(resp => {
        console.log(resp);
        return resp;
      }))
  }
  /**
   *
   *
   * @param {Task} task
   * @returns
   * @memberof TaskService
   */
  editTask(task: Task) {
    /**
     * creation": "2019-12-16T02:13:18.126Z",
  "description": "string",
  "estimation": 0,
  "id": 0,
  "listName": {
    "id": 0,
    "name": "string",
    "sprint": {
      "id": 0,
      "name": "string",
      "startDate": "2019-12-16T02:13:18.126Z"
    },
    "task": [
      null
    ]
  },
  "modification": "2019-12-16T02:13:18.126Z",
  "name": "string",
  "priority": "string"
     */
    const data = {
      creation: task.creation,
      description: task.description,
      estimation: task.estimation,
      listName: {
        id: task.listId
      },
      modification: task.modification,
      name: task.name,
      priority: task.priority
    }
    console.log(data)
    return this.http.put<any>(environment.urlApiRest + `tasks/${task.id}` , data)
      .pipe(map(resp => {
        return resp;
      }))
  }

  deleteTask(id: number) {
    return this.http.delete<any>(environment.urlApiRest + `tasks/` + id)
      .pipe(map(resp => {
        console.log(resp)
        return resp;
      }))
  }

  public addTask(task: any): Observable<any> {
    const data = {
      creation: task.creation,
      description: task.description,
      estimation: task.estimation,
      listName: {
        id: task.listId
      },
      modification: task.modification,
      name: task.name,
      priority: task.priority
    }
    console.log(data)
    return this.http.post<any>(environment.urlApiRest + `tasks`, data)
      .pipe(map(resp => {
        console.log(resp);
        return resp;
      }))
  }
}
