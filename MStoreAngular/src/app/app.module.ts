import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { TagInputModule } from 'ngx-chips';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShopComponent } from './components/shop/shop.component';
import { PostComponent } from './components/post/post.component';
import { CartComponent } from './components/cart/cart.component';
import { ProfileComponent } from './components/public-profile/profile.component';
import { MyProfileComponent } from './components/public-profile/my-profile.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { CreatePostComponent } from './components/create-post/create-post.component';
import { IndexComponent } from './components/index/index.component';
import { ErrorInterceptor } from './components/error/error-interceptor.component';
import { ErrorComponentNF } from './components/error/errorNF.component';
import { LoginComponent } from './components/login/login.component';
import { IconsModule } from './modules/icons/icons.module';
import {ShowPostsComponent} from './components/show-posts/show-posts.component';
import { PaymentComponent } from './components/payment/payment.component';
import { StepperComponent } from './components/stepper/stepper.component';
import { FinalReviewComponent } from './components/final-review/final-review.component';
import { CompleteComponent } from './components/complete/complete.component';
import { AdminComponent } from './components/admin/admin.component';
import { SearchPipe } from './search.pipe';

@NgModule({
  declarations: [
    AppComponent,
    ShopComponent,
    PostComponent,
    CartComponent,
    ProfileComponent,
    MyProfileComponent,
    EditProfileComponent,
    CreatePostComponent,
    IndexComponent,
    ErrorComponentNF,
    LoginComponent,
    ShowPostsComponent,
    PaymentComponent,
    StepperComponent,
    FinalReviewComponent,
    CompleteComponent,
    AdminComponent,
    SearchPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    IconsModule,
    TagInputModule
  ],
  providers: [      
    {provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptor,
    multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }