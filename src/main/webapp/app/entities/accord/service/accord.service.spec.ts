import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ValidationDC } from 'app/entities/enumerations/validation-dc.model';
import { ValidationR } from 'app/entities/enumerations/validation-r.model';
import { IAccord, Accord } from '../accord.model';

import { AccordService } from './accord.service';

describe('Accord Service', () => {
  let service: AccordService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccord;
  let expectedResult: IAccord | IAccord[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccordService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      partenaire: 'AAAAAAA',
      domaine: 'AAAAAAA',
      description: 'AAAAAAA',
      date: currentDate,
      dure: 'AAAAAAA',
      zone: 'AAAAAAA',
      type: 'AAAAAAA',
      nature: 'AAAAAAA',
      validationDircoop: ValidationDC.VALIDE,
      validationRecteur: ValidationR.VALIDE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Accord', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Accord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Accord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          partenaire: 'BBBBBB',
          domaine: 'BBBBBB',
          description: 'BBBBBB',
          date: currentDate.format(DATE_TIME_FORMAT),
          dure: 'BBBBBB',
          zone: 'BBBBBB',
          type: 'BBBBBB',
          nature: 'BBBBBB',
          validationDircoop: 'BBBBBB',
          validationRecteur: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Accord', () => {
      const patchObject = Object.assign(
        {
          domaine: 'BBBBBB',
          type: 'BBBBBB',
          nature: 'BBBBBB',
          validationRecteur: 'BBBBBB',
        },
        new Accord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Accord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          partenaire: 'BBBBBB',
          domaine: 'BBBBBB',
          description: 'BBBBBB',
          date: currentDate.format(DATE_TIME_FORMAT),
          dure: 'BBBBBB',
          zone: 'BBBBBB',
          type: 'BBBBBB',
          nature: 'BBBBBB',
          validationDircoop: 'BBBBBB',
          validationRecteur: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Accord', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccordToCollectionIfMissing', () => {
      it('should add a Accord to an empty array', () => {
        const accord: IAccord = { id: 123 };
        expectedResult = service.addAccordToCollectionIfMissing([], accord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accord);
      });

      it('should not add a Accord to an array that contains it', () => {
        const accord: IAccord = { id: 123 };
        const accordCollection: IAccord[] = [
          {
            ...accord,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccordToCollectionIfMissing(accordCollection, accord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Accord to an array that doesn't contain it", () => {
        const accord: IAccord = { id: 123 };
        const accordCollection: IAccord[] = [{ id: 456 }];
        expectedResult = service.addAccordToCollectionIfMissing(accordCollection, accord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accord);
      });

      it('should add only unique Accord to an array', () => {
        const accordArray: IAccord[] = [{ id: 123 }, { id: 456 }, { id: 30540 }];
        const accordCollection: IAccord[] = [{ id: 123 }];
        expectedResult = service.addAccordToCollectionIfMissing(accordCollection, ...accordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accord: IAccord = { id: 123 };
        const accord2: IAccord = { id: 456 };
        expectedResult = service.addAccordToCollectionIfMissing([], accord, accord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accord);
        expect(expectedResult).toContain(accord2);
      });

      it('should accept null and undefined values', () => {
        const accord: IAccord = { id: 123 };
        expectedResult = service.addAccordToCollectionIfMissing([], null, accord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accord);
      });

      it('should return initial array if no Accord is added', () => {
        const accordCollection: IAccord[] = [{ id: 123 }];
        expectedResult = service.addAccordToCollectionIfMissing(accordCollection, undefined, null);
        expect(expectedResult).toEqual(accordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
