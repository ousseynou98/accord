import * as dayjs from 'dayjs';
import { ValidationDC } from 'app/entities/enumerations/validation-dc.model';
import { ValidationR } from 'app/entities/enumerations/validation-r.model';

export interface IAccord {
  id?: number;
  partenaire?: string | null;
  domaine?: string | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  dure?: string | null;
  zone?: string | null;
  type?: string | null;
  nature?: string | null;
  validationDircoop?: ValidationDC | null;
  validationRecteur?: ValidationR | null;
}

export class Accord implements IAccord {
  constructor(
    public id?: number,
    public partenaire?: string | null,
    public domaine?: string | null,
    public description?: string | null,
    public date?: dayjs.Dayjs | null,
    public dure?: string | null,
    public zone?: string | null,
    public type?: string | null,
    public nature?: string | null,
    public validationDircoop?: ValidationDC | null,
    public validationRecteur?: ValidationR | null
  ) {}
}

export function getAccordIdentifier(accord: IAccord): number | undefined {
  return accord.id;
}
