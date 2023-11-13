import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: "survey",
        data: { pageTitle: "Surveys" },
        loadChildren: () =>
          import("./survey/survey.module").then((m) => m.SurveyModule),
      },
      {
        path: "question",
        data: { pageTitle: "Questions" },
        loadChildren: () =>
          import("./question/question.module").then((m) => m.QuestionModule),
      },
      {
        path: "type-question",
        data: { pageTitle: "TypeQuestions" },
        loadChildren: () =>
          import("./type-question/type-question.module").then(
            (m) => m.TypeQuestionModule
          ),
      },
      {
        path: "answer",
        data: { pageTitle: "Answers" },
        loadChildren: () =>
          import("./answer/answer.module").then((m) => m.AnswerModule),
      },
      {
        path: "answer-choice",
        data: { pageTitle: "AnswerChoices" },
        loadChildren: () =>
          import("./answer-choice/answer-choice.module").then(
            (m) => m.AnswerChoiceModule
          ),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
