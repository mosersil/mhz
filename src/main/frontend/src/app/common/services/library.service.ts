import {Composition} from "../entities/composition";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {Composer} from "../entities/composer";
import {saveAs} from "file-saver";
import {Repertoire} from "../entities/repertoire";
import {HttpClient} from '@angular/common/http';
import {Injectable} from "@angular/core";
import {StaticFile} from "../entities/staticfile";


const SAMPLE_API = environment.backendUrl + '/api/sample';
const SHEET_API = environment.backendUrl + '/api/sheet';
const COMPOSITION_API = environment.backendUrl + '/api/composition';
const COMPOSER_API = environment.backendUrl + '/api/composer';
const REPERTOIRE_API = environment.backendUrl + '/api/repertoire';
const SHEETDOWNLOAD = environment.backendUrl + '/api/securedownload';
const SHEETUPLOAD = environment.backendUrl + '/api/uploadstaticfile';
const SAMPLEDOWNLOAD = environment.backendUrl + '/api/sampledownload';

@Injectable({
  providedIn: 'root'
})
export class LibraryService {


  constructor(private http: HttpClient) {
  }

  getCompositions(): Observable<Composition[]> {
    return this.http.get<Composition[]>(COMPOSITION_API);
  }


  getComposition(id: string): Observable<Composition> {
    return this.http.get<any>(COMPOSITION_API + "/" + id);
  }

  searchCompositions(searchTerm: string): Observable<Composition[]> {
    return this.http.get<any>(COMPOSITION_API + "?searchTerm=" + searchTerm);
  }

  getComposers(): Observable<Composer[]> {
    return this.http.get<any>(COMPOSER_API+"?sortBy=name");
  }

  getRepertoires(): Observable<Repertoire[]> {
    return this.http.get<any>(REPERTOIRE_API)
  }

  update(composition: Composition): Observable<Composition> {
    return this.http.put<any>(COMPOSITION_API, composition);
  }

  create(composition: Composition): Observable<Composition> {
    return this.http.post<any>(COMPOSITION_API, composition);
  }

  current(): Observable<Composition[]> {
    return this.http.get<any>(COMPOSITION_API);
  }

  downloadFile(sheetId: string) {
    this.http.get(SHEETDOWNLOAD + "/" + sheetId, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, "application/pdf", "download.pdf"));
  }

  downloadSample(sampleId: string) {
    this.http.get(SAMPLEDOWNLOAD + "/" + sampleId, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, "audio/mpeg", "download.mp3"));
  }

  downLoadFile(data: any, type: string, filename: string) {
    var blob = new Blob([data], {type: type});
    saveAs(blob, filename);
  }


  uploadSheet(compositionId: number, name: string, file: File):Observable<StaticFile> {

    // create a new multipart-form for every file
    const formData: FormData = new FormData();
    formData.append('id', "" + compositionId);
    formData.append('title', name);
    formData.append('file', file, file.name);

    // send the http-request and subscribe for progress-updates
    return this.http.post<any>(SHEETUPLOAD, formData);

  }


}
