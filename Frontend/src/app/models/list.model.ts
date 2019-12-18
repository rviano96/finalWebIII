import { Task } from "./task.model";
import {Sprint} from "./sprint.model";
export class List {
  id: number;
  name: string;
  sprint: Sprint;
  task: Task[];

  constructor(id=0, name ="", tasks = []){
    this.id = id;
    this.name = name;
    this.task = tasks;
  }
}
