import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, throwError } from 'rxjs';
import { Trial } from './trial';
import { TrialEventService } from './trial-event.service';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
export class TrialService {
  private apiServerUrl = 'http://localhost:8080/triathlon/trials';

  constructor(private http: HttpClient, private trialEventService: TrialEventService) { }

  public getTrials(): Observable<Trial[]> {
    return this.http.get<Trial[]>(`${this.apiServerUrl}`);
  }

  public addTrial(trial: Trial): Observable<Trial> {
    return this.http.post<Trial>(`${this.apiServerUrl}`, trial).pipe(
      map((response: Trial) => {
        this.trialEventService.notifyTrialUpdated();
        return response;
      })
    );
  }

  public updateTrial(trial: Trial): Observable<Trial> {
    return this.http.put<Trial>(`${this.apiServerUrl}`, trial).pipe(
      map((response: Trial) => {
        this.trialEventService.notifyTrialUpdated();
        return response;
      })
    );
  }

  public deleteTrial(trialId: number): Observable<void | string> {
    return this.http.delete(`${this.apiServerUrl}/${trialId}`, {observe: 'response', responseType: 'text'}).pipe(
      map(response => {
        if (response.status === 200) {
          this.trialEventService.notifyTrialUpdated();
          return;
        }
        return 'Unexpected response';
      }),
      catchError((error: HttpErrorResponse) => {
        let errorMessage: string;
        if (error.error instanceof ErrorEvent) {
          errorMessage = `Client-side error: ${error.error.message}`;
        } else {
          errorMessage = error.error;
        }
        return throwError(errorMessage);
      })
    );
  }
}
