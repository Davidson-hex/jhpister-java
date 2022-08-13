import dayjs from 'dayjs/esm';

export interface IMovimentos {
  id?: number;
  idUsuario?: number | null;
  idLivro?: number | null;
  locacao?: number | null;
  dataLocacao?: dayjs.Dayjs | null;
  previsaoDevolucao?: dayjs.Dayjs | null;
  dataDevolucao?: dayjs.Dayjs | null;
  devolucao?: number | null;
  status?: string | null;
  proprietario?: string | null;
}

export class Movimentos implements IMovimentos {
  constructor(
    public id?: number,
    public idUsuario?: number | null,
    public idLivro?: number | null,
    public locacao?: number | null,
    public dataLocacao?: dayjs.Dayjs | null,
    public previsaoDevolucao?: dayjs.Dayjs | null,
    public dataDevolucao?: dayjs.Dayjs | null,
    public devolucao?: number | null,
    public status?: string | null,
    public proprietario?: string | null
  ) {}
}

export function getMovimentosIdentifier(movimentos: IMovimentos): number | undefined {
  return movimentos.id;
}
