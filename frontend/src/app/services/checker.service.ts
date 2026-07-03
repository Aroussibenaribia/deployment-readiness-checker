import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Finding {
  ruleName: string;
  message: string;
  severity: 'INFO' | 'WARNING' | 'BLOCKER';
}

export interface Report {
  passed: boolean;
  findings: Finding[];
}

@Injectable({
  providedIn: 'root'
})
export class CheckerService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getReport(): Observable<Report> {
    return this.http.get<Report>(`${this.apiUrl}/report`);
  }
}
