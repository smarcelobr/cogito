import {Component, inject, OnInit} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {MatInput} from "@angular/material/input";
import {PerguntaComponent} from "../pergunta/pergunta.component";
import {PerguntaDto, PerguntaImgResponse, PerguntaService, PerguntaUpdateRequest} from "../pergunta.service";
import {ActivatedRoute, Router} from "@angular/router";
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
export class TelaPerguntaComponent implements OnInit {
  private readonly perguntaSubject = new BehaviorSubject<number>(0);
  private readonly pergunta$ = this.perguntaSubject.asObservable();
  private readonly formBuilder = inject(FormBuilder);
  readonly perguntaForm = this.formBuilder.group({
    disciplinaInput: [''],
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

  perguntaCtx = {
    perguntaId: 0,
    opcaoAId: 0,
    opcaoBId: 0,
    opcaoCId: 0,
    opcaoDId: 0
  }

  perguntaImg: PerguntaImgResponse = {
    id: 0,
    disciplina: "",
    opcoes: [],
    base64PNG: Constantes.redCircleBase64PNG
  };

  constructor(route: ActivatedRoute,
              private router: Router,
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
    ).subscribe(pergunta => {
        this.perguntaCtx = {
          perguntaId: pergunta.id,
          opcaoAId: pergunta.opcoes[0].id,
          opcaoBId: pergunta.opcoes[1].id,
          opcaoCId: pergunta.opcoes[2].id,
          opcaoDId: pergunta.opcoes[3].id
        };
        this.perguntaForm.patchValue({
          disciplinaInput: pergunta.disciplina,
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
          }
        );
      }
    );
  }

  atualizar() {
    const formValues = this.perguntaForm.value;

    const perguntaAlterada: PerguntaUpdateRequest = {
      disciplina: formValues.disciplinaInput || '',
      enunciadoLatex: formValues.enunciado || '',
      opcoes: [{
        id: this.perguntaCtx.opcaoAId,
        correta: formValues.opcaoACorreto || false,
        alternativaLatex: formValues.opcaoA || '',
        explicacaoLatex: formValues.opcaoAExplicacao || ''
      }, {
        id: this.perguntaCtx.opcaoBId,
        correta: formValues.opcaoBCorreto || false,
        alternativaLatex: formValues.opcaoB || '',
        explicacaoLatex: formValues.opcaoBExplicacao || ''
      }, {
        id: this.perguntaCtx.opcaoCId,
        correta: formValues.opcaoCCorreto || false,
        alternativaLatex: formValues.opcaoC || '',
        explicacaoLatex: formValues.opcaoCExplicacao || ''
      }, {
        id: this.perguntaCtx.opcaoDId,
        correta: formValues.opcaoDCorreto || false,
        alternativaLatex: formValues.opcaoD || '',
        explicacaoLatex: formValues.opcaoDExplicacao || ''
      }]
    }

    this.perguntaService.put(this.perguntaCtx.perguntaId, perguntaAlterada).subscribe(perguntaDto => this.perguntaSubject.next(perguntaDto.id));

  }

  clonar() {
    this.perguntaService.clone(this.perguntaCtx.perguntaId)
      .subscribe(perguntaDto =>
          this.router.navigate(['pergunta'],{queryParams: {perguntaId: perguntaDto.id}}));
  }
}
