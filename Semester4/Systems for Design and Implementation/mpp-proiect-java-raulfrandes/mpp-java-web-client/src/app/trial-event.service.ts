import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TrialEventService {
  private trialUpdatedSource = new Subject<void>();

  trialUpdated$: Observable<void> = this.trialUpdatedSource.asObservable();

  notifyTrialUpdated(): void {
    this.trialUpdatedSource.next();
  }
}
