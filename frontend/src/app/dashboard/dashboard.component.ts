import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckerService, Report, Finding } from '../services/checker.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  report: Report | null = null;
  loading = false;
  error: string | null = null;
  lastChecked: Date | null = null;

  get blockers(): Finding[] {
    return this.report?.findings.filter(f => f.severity === 'BLOCKER') ?? [];
  }

  get warnings(): Finding[] {
    return this.report?.findings.filter(f => f.severity === 'WARNING') ?? [];
  }

  get infos(): Finding[] {
    return this.report?.findings.filter(f => f.severity === 'INFO') ?? [];
  }

  constructor(private checkerService: CheckerService) {}

  ngOnInit(): void {
    this.runCheck();
  }

  runCheck(): void {
    this.loading = true;
    this.error = null;
    this.report = null;

    this.checkerService.getReport().subscribe({
      next: (data) => {
        this.report = data;
        this.loading = false;
        this.lastChecked = new Date();
      },
      error: (err) => {
        this.error = 'Could not connect to the Checker service. Make sure the Agent and Checker are both running.';
        this.loading = false;
      }
    });
  }
}
