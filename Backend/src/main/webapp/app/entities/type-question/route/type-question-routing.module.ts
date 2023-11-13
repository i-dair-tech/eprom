import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { UserRouteAccessService } from "app/core/auth/user-route-access.service";
import { TypeQuestionComponent } from "../list/type-question.component";
import { TypeQuestionDetailComponent } from "../detail/type-question-detail.component";
import { TypeQuestionUpdateComponent } from "../update/type-question-update.component";
import { TypeQuestionRoutingResolveService } from "./type-question-routing-resolve.service";

const typeQuestionRoute: Routes = [
  {
    path: "",
    component: TypeQuestionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ":id/view",
    component: TypeQuestionDetailComponent,
    resolve: {
      typeQuestion: TypeQuestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: "new",
    component: TypeQuestionUpdateComponent,
    resolve: {
      typeQuestion: TypeQuestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ":id/edit",
    component: TypeQuestionUpdateComponent,
    resolve: {
      typeQuestion: TypeQuestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeQuestionRoute)],
  exports: [RouterModule],
})
export class TypeQuestionRoutingModule {}
