import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../service/model/Book';
import 'rxjs/Rx';
import { BookService } from '../service/api/book.service';



@Component({
  selector: 'bs-book-form',
  templateUrl: './book-form.component.html',
  styles: []
})
export class BookFormComponent implements OnInit {
  private book : Book = new Book();
  constructor(private router : Router, private bookService : BookService) { }

  ngOnInit() {
  }

  create() {
    this.bookService.createBook(this.book)
      .finally(() => this.router.navigate(['/book-list']))
      .subscribe();
  }
}
