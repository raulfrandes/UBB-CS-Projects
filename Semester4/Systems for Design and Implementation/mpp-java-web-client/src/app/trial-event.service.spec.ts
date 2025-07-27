import { TestBed } from '@angular/core/testing';

import { TrialEventService } from './trial-event.service';

describe('TrialEventService', () => {
  let service: TrialEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrialEventService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
