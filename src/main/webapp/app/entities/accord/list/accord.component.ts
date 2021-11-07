import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccord } from '../accord.model';
import { AccordService } from '../service/accord.service';
import { AccordDeleteDialogComponent } from '../delete/accord-delete-dialog.component';

@Component({
  selector: 'jhi-accord',
  templateUrl: './accord.component.html',
})
export class AccordComponent implements OnInit {
  accords?: IAccord[];
  isLoading = false;

  constructor(protected accordService: AccordService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.accordService.query().subscribe(
      (res: HttpResponse<IAccord[]>) => {
        this.isLoading = false;
        this.accords = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAccord): number {
    return item.id!;
  }

  delete(accord: IAccord): void {
    const modalRef = this.modalService.open(AccordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.accord = accord;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
