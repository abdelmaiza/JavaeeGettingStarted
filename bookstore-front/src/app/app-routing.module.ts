import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {BookListComponent} from "./book-list/book-list.component";
import {BookDetailComponent} from "./book-detail/book-detail.component";
import {BookFormComponent} from "./book-form/book-form.component";

const routes: Routes = [
  {path:'', component: BookListComponent},
  {path:'book-list' , component:BookListComponent},
  {path:'book-detail/:bookId', component: BookDetailComponent},
  {path:'book-form' , component:BookFormComponent}
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
