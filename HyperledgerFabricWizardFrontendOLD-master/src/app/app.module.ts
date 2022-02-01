import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatStepper, MatStepperModule} from '@angular/material/stepper';
import {MatFormFieldModule, MatFormFieldDefaultOptions, MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {FlexLayoutModule} from '@angular/flex-layout';
import {NetworkComponent} from './network/network.component';
import {OrganizationComponent} from './organization/organization.component';
import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import {MatSelectModule} from '@angular/material/select';
import {OrganizationsComponent} from './organizations/organizations.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {ThemingService} from './_services/theming.service';
import {NavComponent} from './nav/nav.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {ConsortiumsComponent} from './consortiums/consortiums.component';
import {ChannelsComponent} from './channels/channels.component';
import {Server} from './_services/server';
import {HttpClientModule} from '@angular/common/http';
import {SummaryComponent} from './summary/summary.component';
import {MatTableModule} from '@angular/material/table';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatMenuModule} from '@angular/material/menu';
import {ImportDialogComponent} from './import-dialog/import-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatoptionselectedDirective} from './matoptionselected.directive';
import {MystepperComponent} from './mystepper/mystepper.component';
import {CdkStepper, CdkStepperModule} from '@angular/cdk/stepper';
import {MatRippleModule} from '@angular/material/core';
import {MatTooltipModule} from '@angular/material/tooltip';

const appearance: MatFormFieldDefaultOptions = {
  appearance: 'outline'
};

@NgModule({
  declarations: [
    AppComponent,
    NetworkComponent,
    OrganizationComponent,
    OrganizationsComponent,
    NavComponent,
    ConsortiumsComponent,
    ChannelsComponent,
    SummaryComponent,
    ImportDialogComponent,
    MatoptionselectedDirective,
    MystepperComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatStepperModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    FlexLayoutModule,
    MatDividerModule,
    FormsModule,
    MatIconModule,
    MatSelectModule,
    MatCheckboxModule,
    LayoutModule,
    MatToolbarModule,
    HttpClientModule,
    MatSidenavModule,
    MatListModule,
    MatTableModule,
    MatMenuModule,
    MatDialogModule,
    CdkStepperModule,
    MatRippleModule,
    MatTooltipModule
  ],
  providers: [
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: appearance
    },
    ThemingService,
    Server,
    MatSnackBar,
    MatStepper
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
