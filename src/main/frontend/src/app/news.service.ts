import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Article} from "./article";
import {Observable} from "rxjs";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(private http: HttpClient) {
  }

  // Uses http.get() to load data from a single API endpoint
  getArticle() {
    console.log("Backend: " + environment.backendUrl);
    return this.http.get<Article>(environment.backendUrl + '/public/api/article');
  }
}
