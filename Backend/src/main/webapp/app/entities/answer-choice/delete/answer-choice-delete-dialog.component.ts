import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import { IAnswerChoice } from "../answer-choice.model";
import { AnswerChoiceService } from "../service/answer-choice.service";

@Component({
  templateUrl: "./answer-choice-delete-dialog.component.html",
})
export class AnswerChoiceDeleteDialogComponent {
  answerChoice?: IAnswerChoice;

  constructor(
    protected answerChoiceService: AnswerChoiceService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.answerChoiceService.delete(id).subscribe(() => {
      this.activeModal.close("deleted");
    });
  }
}
