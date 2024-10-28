import { Routes } from '@angular/router';
import {TelaTestaLatexComponent} from "./tela-testa-latex/tela-testa-latex.component";
import {TelaTesteComponent} from "./tela-teste/tela-teste.component";

export const routes: Routes = [
  {path:'latex', component: TelaTestaLatexComponent},
  {path:'', component: TelaTesteComponent}];
