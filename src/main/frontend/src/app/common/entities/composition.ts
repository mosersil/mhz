import {Composer} from "./composer";
import {StaticFile} from "./staticfile";
import {Sample} from "./sample";

export class Composition {
  id: number;
  inventory: string;
  title: string;
  subtitle: string;
  genre: string;
  description: string;
  composers: Composer[];
  arrangers: Composer[];
  sheets: StaticFile[];
  samples: Sample[];
}
