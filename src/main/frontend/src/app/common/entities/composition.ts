import {Composer} from "./composer";
import {Sheet} from "./sheet";
import {Sample} from "./sample";

export class Composition {
  id: number;
  inventory: string;
  title: string;
  subtitle: string;
  genre: string;
  tag: string;
  description: string;
  composers: Composer[];
  arrangers: Composer[];
  sheets: Sheet[];
  samples: Sample[];
}
