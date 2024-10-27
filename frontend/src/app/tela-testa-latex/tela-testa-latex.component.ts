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

  latex = new FormControl('');

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
