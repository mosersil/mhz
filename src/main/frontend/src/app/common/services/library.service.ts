import {Composition} from "../entities/composition";
import {environment} from "../../../environments/environment";
import {Observable, Subject} from "rxjs";
import {Composer} from "../entities/composer";
import {saveAs} from "file-saver";
import {Repertoire} from "../entities/repertoire";
import {HttpClient, HttpEventType, HttpRequest, HttpResponse} from '@angular/common/http';
import {Injectable} from "@angular/core";


const SAMPLE_API = environment.backendUrl + '/api/public/sample';
const SHEET_API = environment.backendUrl + '/api/public/sheet';
const COMPOSITION_API = environment.backendUrl + '/api/public/composition';
const COMPOSER_API = environment.backendUrl + '/api/public/composer';
const REPERTOIRE_API = environment.backendUrl + '/api/public/repertoire';

@Injectable({
  providedIn: 'root'
})
export class LibraryService {


  constructor(private http: HttpClient) {
  }


  getCompositions(): Observable<Composition[]> {
    return this.http.get<Composition[]>(COMPOSITION_API);
  }


  getComposition(id: string) : Observable<Composition> {
    return this.http.get<any>(COMPOSITION_API+"/"+id);
  }

  searchCompositions(searchTerm: string) : Observable<Composition[]> {
    return this.http.get<any>(COMPOSITION_API+"?searchTerm="+searchTerm);
  }

  getComposers(): Observable<Composer[]> {
    return this.http.get<any>(COMPOSER_API);
  }

  getRepertoires(): Observable<Repertoire[]> {
    return this.http.get<any>(REPERTOIRE_API)
  }

  save(composition: Composition): Observable<Composition> {
    return this.http.post<any>(COMPOSITION_API, composition);
  }

  current(): Observable<Composition[]> {
    return this.http.get<any>(COMPOSITION_API);
  }

  downloadFile(sheetId: string) {
    this.http.get(SHEET_API + "/"+sheetId, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, "application/pdf", "download.pdf"));
  }

  downloadSample(sampleId: string) {
    this.http.get(SAMPLE_API + "/"+sampleId, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, "audio/mpeg", "download.mp3"));
  }

  downLoadFile(data: any, type: string, filename: string) {
    var blob = new Blob([data], {type: type});
    saveAs(blob, filename);
  }



  public upload(compositionId: number, name: string, files: Set<File>):
    { [key: string]: { progress: Observable<number> } } {

    // this will be the our resulting map
    const status: { [key: string]: { progress: Observable<number> } } = {};

    files.forEach(file => {
      // create a new multipart-form for every file
      const formData: FormData = new FormData();
      formData.append('id', ""+compositionId);
      formData.append('title', name);
      formData.append('file', file, file.name);

      // create a http-post request and pass the form
      // tell it to report the upload progress
      const req = new HttpRequest('POST', SHEET_API, formData, {
        reportProgress: true
      });

      // create a new progress-subject for every file
      const progress = new Subject<number>();

      // send the http-request and subscribe for progress-updates
      this.http.request(req).subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {

          // calculate the progress percentage
          const percentDone = Math.round(100 * event.loaded / event.total);

          // pass the percentage into the progress-stream
          progress.next(percentDone);
        } else if (event instanceof HttpResponse) {

          // Close the progress-stream if we get an answer form the API
          // The upload is complete
          progress.complete();
        }
      });

      // Save every progress-observable in a map of all observables
      status[file.name] = {
        progress: progress.asObservable()
      };
    });

    // return the map of progress.observables
    return status;
  }




}
