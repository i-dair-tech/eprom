import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { UserRouteAccessService } from "app/core/auth/user-route-access.service";
import { AnswerChoiceComponent } from "../list/answer-choice.component";
import { AnswerChoiceDetailComponent } from "../detail/answer-choice-detail.component";
import { AnswerChoiceUpdateComponent } from "../update/answer-choice-update.component";
import { AnswerChoiceRoutingResolveService } from "./answer-choice-routing-resolve.service";

const answerChoiceRoute: Routes = [
  {
    path: "",
    component: AnswerChoiceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ":id/view",
    component: AnswerChoiceDetailComponent,
    resolve: {
      answerChoice: AnswerChoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: "new",
    component: AnswerChoiceUpdateComponent,
    resolve: {
      answerChoice: AnswerChoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ":id/edit",
    component: AnswerChoiceUpdateComponent,
    resolve: {
      answerChoice: AnswerChoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(answerChoiceRoute)],
  exports: [RouterModule],
})
export class AnswerChoiceRoutingModule {}
