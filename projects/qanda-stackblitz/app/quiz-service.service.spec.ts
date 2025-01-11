import { TestBed, inject } from '@angular/core/testing';

import { QuizServiceService } from './quiz-service.service';

describe('QuizServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [QuizServiceService]
    });
  });

  it('should be created', inject([QuizServiceService], (service: QuizServiceService) => {
    expect(service).toBeTruthy();
  }));
});
