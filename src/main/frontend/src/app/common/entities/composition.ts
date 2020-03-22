import {Composer} from "./composer";
import {StaticFile} from "./staticfile";

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
  samples: StaticFile[];
}
