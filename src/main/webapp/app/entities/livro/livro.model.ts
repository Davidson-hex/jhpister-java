import dayjs from 'dayjs/esm';

export interface ILivro {
  id?: number;
  autor?: string | null;
  titulo?: string | null;
  dataCriacao?: dayjs.Dayjs | null;
  ativo?: number | null;
  idUsuarioCadastro?: number | null;
  proprietario?: string | null;
}

export class Livro implements ILivro {
  constructor(
    public id?: number,
    public autor?: string | null,
    public titulo?: string | null,
    public dataCriacao?: dayjs.Dayjs | null,
    public ativo?: number | null,
    public idUsuarioCadastro?: number | null,
    public proprietario?: string | null
  ) {}
}

export function getLivroIdentifier(livro: ILivro): number | undefined {
  return livro.id;
}
