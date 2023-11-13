import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { ITypeQuestion } from "../type-question.model";

@Component({
  selector: "jhi-type-question-detail",
  templateUrl: "./type-question-detail.component.html",
})
export class TypeQuestionDetailComponent implements OnInit {
  typeQuestion: ITypeQuestion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeQuestion }) => {
      this.typeQuestion = typeQuestion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
