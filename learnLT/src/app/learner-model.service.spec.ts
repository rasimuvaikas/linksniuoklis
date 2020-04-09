import { TestBed } from '@angular/core/testing';

import { LearnerModelService } from './learner-model.service';

describe('LearnerModelService', () => {
  let service: LearnerModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LearnerModelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
