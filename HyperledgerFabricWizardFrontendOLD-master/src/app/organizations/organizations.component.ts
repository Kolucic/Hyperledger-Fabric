import {AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {Org} from '../_models/org';
import {MatStepper} from '@angular/material/stepper';
import {OrganizationComponent} from '../organization/organization.component';
import {CdkStep, CdkStepper} from '@angular/cdk/stepper';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-organizations',
  templateUrl: './organizations.component.html',
  styleUrls: ['./organizations.component.scss']
})
export class OrganizationsComponent implements OnInit {

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({});
  }

  @Input() rootStepper: CdkStepper;

  form: FormGroup;

  @Input() orgs: Org[];

  ngOnInit(): void {
  }
}
