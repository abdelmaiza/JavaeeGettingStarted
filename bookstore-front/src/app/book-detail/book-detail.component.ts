import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Book, BookService} from "../service";
import {__param} from "tslib";

@Component({
  selector: 'bs-book-detail',
  templateUrl: './book-detail.component.html',
  styles: []
})
export class BookDetailComponent implements OnInit {
  private book : Book  = new Book();
  constructor(private router: Router, private bookService : BookService, private route : ActivatedRoute) { }

  ngOnInit() {
    this.route.params.map(params => params['bookId']).switchMap(id => this.bookService.getBook(id)).subscribe(book=> this.book = book);
  }

  delete(){
    // invoke our REST API
    this.bookService.deleteBook(this.book.id).finally(()=> this.router.navigate(['/book-list'])).subscribe();

  }

}
