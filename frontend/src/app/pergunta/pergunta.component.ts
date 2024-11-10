import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {OpcaoImg, PerguntaImgResponse, PerguntaService} from "../pergunta.service";
import {MatIcon} from "@angular/material/icon";
import {NgForOf, NgIf} from "@angular/common";
import {Constantes} from "../constantes";

@Component({
  selector: 'app-pergunta',
  standalone: true,
  imports: [
    MatIcon,
    NgIf,
    NgForOf
  ],
  templateUrl: './pergunta.component.html',
  styleUrl: './pergunta.component.scss'
})
export class PerguntaComponent implements OnInit, OnChanges {

  _pergunta: PerguntaImgResponse = {
    id: 0,
    disciplina: "",
    opcoes: [],
    base64PNG: Constantes.redCircleBase64PNG
  };

  private _opcaoId: number = 0;

  @Input()
  get pergunta(): PerguntaImgResponse {
    return this._pergunta;
  }
  set pergunta(value: PerguntaImgResponse) {
    this._pergunta = value;
    console.log('setPergunta', value.id);
  }

  @Input()
  get opcaoId(): number {
    return this._opcaoId;
  }

  set opcaoId(value: number) {
    this._opcaoId = value;
  }

  @Input()
  numPergunta: number = 0;

  @Input()
  rndSeed: number = 10;

  @Output() opcaoIdChange = new EventEmitter<number>();

  correta: boolean = false;
  explicacao?: string = undefined;

  constructor(private perguntaService: PerguntaService) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

  marcar_opcao(opcao_clicada: OpcaoImg) {
     if (!this.pergunta) {
       return;
     }

     this.explicacao = opcao_clicada.explicacaoBase64PNG;
     this.correta = opcao_clicada.correta;
     this._opcaoId = opcao_clicada.id;
     this.opcaoIdChange.emit(opcao_clicada.id);
  }

  toImgSrc(base64PNG?: string) {
    return "data:image/png;base64, "+ (base64PNG?base64PNG:Constantes.redCircleBase64PNG);
  }
}
