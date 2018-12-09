import {Component, OnInit} from '@angular/core';
import {GalleryItem, ImageItem} from "@ngx-gallery/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";


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
        var item = new ImageItem({src: entry.src, thumb: entry.thumb});
        newImages.push(item);
        this.images = newImages;
      }
    }, error => {
      console.log("error occured: " + error.message);
    });
/*
      this.images = [
        new ImageItem({ src: '/assets/logo/logo.jpg', thumb: '/assets/logo/logo.jpg' }),
        new ImageItem({ src: '/assets/images/1.jpg', thumb: '/assets/images/1.jpg' })
  ];
*/
  }

}
