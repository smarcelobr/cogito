import {Component, inject} from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {LatexRequest, LatexService} from "../latex.service";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {BehaviorSubject, defaultIfEmpty, map} from "rxjs";
import {AsyncPipe} from "@angular/common";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-tela-testa-latex',
  standalone: true,
  imports: [
    MatGridList,
    MatGridTile,
    MatFormField,
    MatInput,
    AsyncPipe,
    MatButton,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './tela-testa-latex.component.html',
  styleUrl: './tela-testa-latex.component.scss'
})
export class TelaTestaLatexComponent {

  private latexService: LatexService = inject(LatexService);

  latex = new FormControl('\\text{Qual é a área de um quadrado cujo perímetro mede $20\\,cm$ ?}\n' +
    '\\\\\n' +
    '\\begin{array}{rl}\n' +
    'a) & A = 5\\,cm\n' +
    '\\\\\n' +
    'b) & A = 20\\,cm^2\n' +
    '\\\\\n' +
    'c) & A = 25\\,cm^2\n' +
    '\\\\\n' +
    'd) & A = 80\\,cm^2\n' +
    '\\end{array}\\\\\n' +
    '\\begin{array}{l}\n' +
    '\\text{Opção Incorreta. $5\\,cm$ é o lado do quadrado. A questão solicita a }\\textbf{área}\\text{ do quadrado.}\n' +
    '\\\\\n' +
    '\\text{Opção Incorreta. Primeiro deve-se calcular o lado do quadrado a partir do perímetro ($p$)\\\\com a fórmula $l=\\frac{p}{4}$ e, em seguida, calcular a sua área usando a fórmula $A=l^2$.}\n' +
    '\\\\\n' +
    '\\text{Opção Correta. Um quadrado tem 4 lados congruentes.\\\\Se o perímetro tem $20\\,cm$, então o lado é $l=\\frac{20\\,cm}{4}=5\\,cm$ .\\\\A fórmula da área é $A=l^2$, então, $A=(5\\,cm)^2=25\\,cm^2}\n' +
    '\\\\\n' +
    '\\text{Opção incorreta. Primeiro deve-se calcular o lado do quadrado a partir do perímetro ($p$)\\\\com a fórmula $l=\\frac{p}{4}$ e, em seguida, calcular a sua área usando a fórmula $A=l^2$.}\n' +
    '\\end{array}' +
    '\\\\\n' +
    '\\begin{tabular}{ |c|c|c|c|c|c|c| } \n' +
    ' \\hline\n' +
    ' km & hm & dam & m & dm & cm & mm \\\\ \n' +
    ' \\hline\n' +
    '0 & 0 &  0 & 3 & 8 & 8 & 0 \\\\ \n' +
    ' \\hline\n' +
    '\\end{tabular}\n');

  private latexImageSubject= new BehaviorSubject<string>("data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==");
  latexImage = this.latexImageSubject.asObservable();

  enviar() {
    if (this.latex.value) {
      const latexRequest: LatexRequest = {
        latex: this.latex.value??''
      }
      this.latexService.getLatex(latexRequest).pipe(
        map(response=>response.base64PNG),
        defaultIfEmpty("iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="),
        map(base64PNG=> "data:image/png;base64, "+base64PNG)
      ).subscribe((base64PNG) => {
          this.latexImageSubject.next(base64PNG);
        })

    }
  }
}
