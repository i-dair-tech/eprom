import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { ITypeQuestion } from "../type-question.model";
import { TypeQuestionService } from "../service/type-question.service";
import { TypeQuestionDeleteDialogComponent } from "../delete/type-question-delete-dialog.component";

@Component({
  selector: "jhi-type-question",
  templateUrl: "./type-question.component.html",
})
export class TypeQuestionComponent implements OnInit {
  typeQuestions?: ITypeQuestion[];
  isLoading = false;

  constructor(
    protected typeQuestionService: TypeQuestionService,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.typeQuestionService.query().subscribe({
      next: (res: HttpResponse<ITypeQuestion[]>) => {
        this.isLoading = false;
        this.typeQuestions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITypeQuestion): number {
    return item.id!;
  }

  delete(typeQuestion: ITypeQuestion): void {
    const modalRef = this.modalService.open(TypeQuestionDeleteDialogComponent, {
      size: "lg",
      backdrop: "static",
    });
    modalRef.componentInstance.typeQuestion = typeQuestion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe((reason) => {
      if (reason === "deleted") {
        this.loadAll();
      }
    });
  }
}
