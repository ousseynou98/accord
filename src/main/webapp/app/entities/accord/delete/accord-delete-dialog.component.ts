import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccord } from '../accord.model';
import { AccordService } from '../service/accord.service';

@Component({
  templateUrl: './accord-delete-dialog.component.html',
})
export class AccordDeleteDialogComponent {
  accord?: IAccord;

  constructor(protected accordService: AccordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
