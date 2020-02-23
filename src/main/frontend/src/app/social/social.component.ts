import {Component, OnInit} from '@angular/core';
import {ImageItem} from "@ngx-gallery/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

const IMAGE_URL_PREFIX = environment.backendUrl + "/api/public/image?name=";

@Component({
  selector: 'app-social',
  templateUrl: './social.component.html',
  styleUrls: ['./social.component.sass']
})
export class SocialComponent implements OnInit {



  images: ImageItem[];

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.http.get<any[]>(environment.backendUrl + "/api/public/images").subscribe(data => {
      var newImages = new Array();
      for (let entry of data) {
        var item = new ImageItem({src: IMAGE_URL_PREFIX +  entry.name, thumb: IMAGE_URL_PREFIX + entry.name});
        newImages.push(item);
        this.images = newImages;
      }
    }, error => {
      console.log("error occured: " + error.message);
    });
  }

}
