import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { IAnswerChoice } from "../answer-choice.model";

@Component({
  selector: "jhi-answer-choice-detail",
  templateUrl: "./answer-choice-detail.component.html",
})
export class AnswerChoiceDetailComponent implements OnInit {
  answerChoice: IAnswerChoice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answerChoice }) => {
      this.answerChoice = answerChoice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
