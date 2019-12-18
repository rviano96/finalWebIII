import { List } from './list.model';
export class Task {
  id: number;
  name: string;
  description: string;
  estimation: number;
  priority: string;
  creation: Date;
  modification: Date;
  list: List;
  listId: number;
  constructor (id=0,
    name= "",
    description= "",
    creation= new Date(),
    modification= creation,
    priority= "",
    estimation= 0){
      this. id = id;
      this.name = name;
      this.description = description;
      this.modification = modification;
      this.priority = priority;
      this.estimation = estimation;
    }
}

