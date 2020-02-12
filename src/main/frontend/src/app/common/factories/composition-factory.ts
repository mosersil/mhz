import {Composition} from "../entities/composition";

export class CompositionFactory {


  static empty(): Composition {
    return {
      id: 0,
      inventory: '',
      title: '',
      subtitle: '',
      description: '',
      genre: '',
      composers: [ {id: 0, name: ''} ],
      arrangers: [ {id: 0, name: ''} ],
      sheets: [ {id: 0, title: '', location: ''} ],
      samples: [ {id: 0, title: '', location: ''} ]
    };
  }
}
