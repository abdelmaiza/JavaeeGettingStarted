import {Component, OnInit} from '@angular/core';


@Component({
  selector: 'bs-book-form',
  templateUrl: './book-form.component.html',
  styles: []
})
export class BookFormComponent implements OnInit {
  private book = {
    title: "dummy title",
    description: "dummy description",
    unitCost: "123",
    nbOfPages:"234",
    imageURL: "http://ecx.images-amazon.com/images/I/51baRLwlufL._SL160_.jpg",
    language: "0"
  };
  constructor() { }

  ngOnInit() {
  }

}
