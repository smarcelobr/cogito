export interface OpcaoView {
  id: number
  letra: string
  base64PNG: string
}

export interface QuestaoView {
  id: number
  perguntaId: number
  disciplina: string;
  base64PNG: string
  opcoes: OpcaoView[]
  opcaoId?: number
}

export interface TesteView {
  id: number
  perguntas: QuestaoView[]
  nota?: number
  status: string
  dataCriacao: string
  dataConclusao?: string
}
