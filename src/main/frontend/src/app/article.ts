import {Event} from "./event";

export class Article {

  title: string;
  teaser: string;
  text: string;
  startDate: Date;
  endDate: Date;
  events: Event[];
}
