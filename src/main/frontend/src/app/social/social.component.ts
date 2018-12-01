import {Component, OnInit} from '@angular/core';
import {GalleryItem, ImageItem} from "@ngx-gallery/core";

@Component({
  selector: 'app-social',
  templateUrl: './social.component.html',
  styleUrls: ['./social.component.sass']
})
export class SocialComponent implements OnInit {

  images: GalleryItem[];

  constructor() {
  }

  ngOnInit() {

      // Set gallery items array
      this.images = [
        new ImageItem({ src: '/assets/logo/logo.jpg', thumb: '/assets/logo/logo.jpg' }),
        new ImageItem({ src: '/assets/images/1.jpg', thumb: '/assets/images/1.jpg' })
  ];

  }

}
