import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { IAnswerChoice } from "../answer-choice.model";
import { AnswerChoiceService } from "../service/answer-choice.service";
import { AnswerChoiceDeleteDialogComponent } from "../delete/answer-choice-delete-dialog.component";

@Component({
  selector: "jhi-answer-choice",
  templateUrl: "./answer-choice.component.html",
})
export class AnswerChoiceComponent implements OnInit {
  answerChoices?: IAnswerChoice[];
  isLoading = false;

  constructor(
    protected answerChoiceService: AnswerChoiceService,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.answerChoiceService.query().subscribe({
      next: (res: HttpResponse<IAnswerChoice[]>) => {
        this.isLoading = false;
        this.answerChoices = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAnswerChoice): number {
    return item.id!;
  }

  delete(answerChoice: IAnswerChoice): void {
    const modalRef = this.modalService.open(AnswerChoiceDeleteDialogComponent, {
      size: "lg",
      backdrop: "static",
    });
    modalRef.componentInstance.answerChoice = answerChoice;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe((reason) => {
      if (reason === "deleted") {
        this.loadAll();
      }
    });
  }
}
