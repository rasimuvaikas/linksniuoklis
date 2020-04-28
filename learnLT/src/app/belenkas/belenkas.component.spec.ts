import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BelenkasComponent } from './belenkas.component';

describe('BelenkasComponent', () => {
  let component: BelenkasComponent;
  let fixture: ComponentFixture<BelenkasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BelenkasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BelenkasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
