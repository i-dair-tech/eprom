import { NgModule } from "@angular/core";
import { SharedModule } from "app/shared/shared.module";
import { TypeQuestionComponent } from "./list/type-question.component";
import { TypeQuestionDetailComponent } from "./detail/type-question-detail.component";
import { TypeQuestionUpdateComponent } from "./update/type-question-update.component";
import { TypeQuestionDeleteDialogComponent } from "./delete/type-question-delete-dialog.component";
import { TypeQuestionRoutingModule } from "./route/type-question-routing.module";

@NgModule({
  imports: [SharedModule, TypeQuestionRoutingModule],
  declarations: [
    TypeQuestionComponent,
    TypeQuestionDetailComponent,
    TypeQuestionUpdateComponent,
    TypeQuestionDeleteDialogComponent,
  ],
  entryComponents: [TypeQuestionDeleteDialogComponent],
})
export class TypeQuestionModule {}
