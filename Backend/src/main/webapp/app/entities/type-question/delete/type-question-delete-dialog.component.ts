import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import { ITypeQuestion } from "../type-question.model";
import { TypeQuestionService } from "../service/type-question.service";

@Component({
  templateUrl: "./type-question-delete-dialog.component.html",
})
export class TypeQuestionDeleteDialogComponent {
  typeQuestion?: ITypeQuestion;

  constructor(
    protected typeQuestionService: TypeQuestionService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeQuestionService.delete(id).subscribe(() => {
      this.activeModal.close("deleted");
    });
  }
}
