import {Component, inject} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {MatInput} from "@angular/material/input";
import {PerguntaComponent} from "../pergunta/pergunta.component";
import {PerguntaImgResponse, PerguntaService} from "../pergunta.service";
import {ActivatedRoute} from "@angular/router";
import {BehaviorSubject, filter, Subject, switchMap, tap} from "rxjs";
import {Constantes} from "../constantes";
import {MatCheckbox} from "@angular/material/checkbox";

@Component({
  selector: 'app-tela-pergunta',
  standalone: true,
  imports: [
    AsyncPipe,
    FormsModule,
    MatButton,
    MatFormField,
    MatGridList,
    MatGridTile,
    MatInput,
    MatLabel,
    PerguntaComponent,
    ReactiveFormsModule,
    MatCheckbox
  ],
  templateUrl: './tela-pergunta.component.html',
  styleUrl: './tela-pergunta.component.scss'
})
export class TelaPerguntaComponent {
  private readonly perguntaSubject = new BehaviorSubject<number>(0);
  private readonly pergunta$ = this.perguntaSubject.asObservable();
  private readonly formBuilder = inject(FormBuilder);
  readonly perguntaForm = this.formBuilder.group({
    enunciado: [''],
    opcaoA: [''],
    opcaoACorreto: [false],
    opcaoAExplicacao: [''],
    opcaoB: [''],
    opcaoBCorreto: [false],
    opcaoBExplicacao: [''],
    opcaoC: [''],
    opcaoCCorreto: [false],
    opcaoCExplicacao: [''],
    opcaoD: [''],
    opcaoDCorreto: [false],
    opcaoDExplicacao: [''],
  });

  perguntaImg: PerguntaImgResponse = {
    id: 0,
    disciplina: "",
    opcoes: [],
    base64PNG: Constantes.redCircleBase64PNG
  };

  constructor(route: ActivatedRoute,
              private perguntaService: PerguntaService) {

    route.queryParamMap.subscribe(params => {
      if (params.has('perguntaId')) {
        console.log('params.perguntaId=', params.get('perguntaId'));
        this.perguntaSubject.next(Number(params.get('perguntaId') || '0'));
      }
    });

  }

  ngOnInit(): void {
    this.pergunta$.pipe(
      tap((perguntaId) => console.log('perguntaSubject emit img', perguntaId)),
      filter((perguntaId) => perguntaId > 0),
      switchMap((perguntaId) => this.perguntaService.getImg(perguntaId, 100)),
    ).subscribe(perguntaImg => this.perguntaImg = perguntaImg);

    this.pergunta$.pipe(
      tap((perguntaId) => console.log('perguntaSubject emit dto', perguntaId)),
      filter((perguntaId) => perguntaId > 0),
      switchMap((perguntaId) => this.perguntaService.get(perguntaId)),
    ).subscribe(pergunta =>
      this.perguntaForm.patchValue({
        enunciado: pergunta.enunciadoLatex,
        opcaoA: pergunta.opcoes[0]?.alternativaLatex,
        opcaoACorreto: pergunta.opcoes[0]?.correta,
        opcaoAExplicacao: pergunta.opcoes[0]?.explicacaoLatex,
        opcaoB: pergunta.opcoes[1]?.alternativaLatex,
        opcaoBCorreto: pergunta.opcoes[1]?.correta,
        opcaoBExplicacao: pergunta.opcoes[1]?.explicacaoLatex,
        opcaoC: pergunta.opcoes[2]?.alternativaLatex,
        opcaoCCorreto: pergunta.opcoes[2]?.correta,
        opcaoCExplicacao: pergunta.opcoes[2]?.explicacaoLatex,
        opcaoD: pergunta.opcoes[3]?.alternativaLatex,
        opcaoDCorreto: pergunta.opcoes[3]?.correta,
        opcaoDExplicacao: pergunta.opcoes[3]?.explicacaoLatex,
      })
    );
  }

  preview() {
    return;
  }
}
