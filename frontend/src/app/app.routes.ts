import { Routes } from '@angular/router';
import {TelaTestaLatexComponent} from "./tela-testa-latex/tela-testa-latex.component";
import {TelaTesteComponent} from "./tela-teste/tela-teste.component";
import {TelaPerguntaComponent} from "./tela-pergunta/tela-pergunta.component";

export const routes: Routes = [
  {path:'latex', component: TelaTestaLatexComponent},
  {path:'pergunta', component: TelaPerguntaComponent},
  {path:'', component: TelaTesteComponent}];
