jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccordService } from '../service/accord.service';
import { IAccord, Accord } from '../accord.model';

import { AccordUpdateComponent } from './accord-update.component';

describe('Accord Management Update Component', () => {
  let comp: AccordUpdateComponent;
  let fixture: ComponentFixture<AccordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accordService: AccordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AccordUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AccordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accordService = TestBed.inject(AccordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const accord: IAccord = { id: 456 };

      activatedRoute.data = of({ accord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accord));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Accord>>();
      const accord = { id: 123 };
      jest.spyOn(accordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accordService.update).toHaveBeenCalledWith(accord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Accord>>();
      const accord = new Accord();
      jest.spyOn(accordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accord }));
      saveSubject.complete();

      // THEN
      expect(accordService.create).toHaveBeenCalledWith(accord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Accord>>();
      const accord = { id: 123 };
      jest.spyOn(accordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accordService.update).toHaveBeenCalledWith(accord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
