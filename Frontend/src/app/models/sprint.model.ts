import {List} from "./list.model"
export class Sprint {
  id: number;
  name: string;
  lists: List[];
  startDate: Date;
}
