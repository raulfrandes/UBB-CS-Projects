import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Trial } from './trial';
import { TrialService } from './trial.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { response } from 'express';
import { error } from 'console';
import { TrialEventService } from './trial-event.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  public trials: Trial[] = [];
  public nullTrial: Trial = {
    id: 0,
    name: '',
    description: '',
    referee: {
      id: 0,
      name: '',
      username: '',
      password: ''
    }
  };
  public editTrial: Trial = this.nullTrial;
  public deleteTrial: Trial = this.nullTrial;

  constructor(private trialService: TrialService, private trialEventService: TrialEventService){}

  ngOnInit() {
    this.getTrials();
    this.trialEventService.trialUpdated$.subscribe(() => {
      this.getTrials();
    })
  }

  public getTrials(): void {
    this.trialService.getTrials().subscribe({
      next: (response: Trial[]) => {
        this.trials = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public onAddTrial(addForm: NgForm): void {
    document.getElementById('add-trial-form')?.click();

    const formValues = addForm.value;
    const newTrial: Trial = {
      id: 0,
      name: formValues.name,
      description: formValues.description,
      referee: {
        id: formValues['referee-id'],
        name: '',
        username: '',
        password: ''
      }
    };

    console.log(addForm.value);
    this.trialService.addTrial(newTrial).subscribe({
      next: (response: Trial) => {
        console.log(response);
        this.getTrials();
        this.trialEventService.notifyTrialUpdated();
        addForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
        addForm.reset();
      }
    });
  }

  public onUpdateTrial(editForm: NgForm): void {
    document.getElementById('edit-trial-form')?.click();

    const formValues = editForm.value;
    const editedTrial: Trial = {
      id: formValues.id,
      name: formValues.name,
      description: formValues.description,
      referee: {
        id: formValues['referee-id'],
        name: '',
        username: '',
        password: ''
      }
    };

    this.trialService.updateTrial(editedTrial).subscribe({
      next: (response: Trial) => {
        console.log(response);
        this.getTrials();
        this.trialEventService.notifyTrialUpdated();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public onDeleteTrial(trialId: number): void {
    document.getElementById('delete-trial-form')?.click();

    this.trialService.deleteTrial(trialId).subscribe({
      next: (response: void | string) => {
        console.log(response);
        this.getTrials();
        this.trialEventService.notifyTrialUpdated();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public onOpenModal(mode: string, trial: Trial): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addTrialModal');
    }
    if (mode === 'edit') {
      this.editTrial = trial;
      button.setAttribute('data-target', '#editTrialModal');
    }
    if (mode === 'delete') {
      this.deleteTrial = trial;
      button.setAttribute('data-target', '#deleteTrialModal');
    }
    container?.appendChild(button);
    button.click();
  }



}
