import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Article} from "./common/entities/article";


const URL_API_ARTICLE = environment.backendUrl + '/public/api/article?date=now';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(private http: HttpClient) {
  }

  getArticle() {
    return this.http.get<Article>(URL_API_ARTICLE);
  }
}
