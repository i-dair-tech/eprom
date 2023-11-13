import { NgModule } from "@angular/core";
import { SharedModule } from "app/shared/shared.module";
import { AnswerChoiceComponent } from "./list/answer-choice.component";
import { AnswerChoiceDetailComponent } from "./detail/answer-choice-detail.component";
import { AnswerChoiceUpdateComponent } from "./update/answer-choice-update.component";
import { AnswerChoiceDeleteDialogComponent } from "./delete/answer-choice-delete-dialog.component";
import { AnswerChoiceRoutingModule } from "./route/answer-choice-routing.module";

@NgModule({
  imports: [SharedModule, AnswerChoiceRoutingModule],
  declarations: [
    AnswerChoiceComponent,
    AnswerChoiceDetailComponent,
    AnswerChoiceUpdateComponent,
    AnswerChoiceDeleteDialogComponent,
  ],
  entryComponents: [AnswerChoiceDeleteDialogComponent],
})
export class AnswerChoiceModule {}
