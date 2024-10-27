import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TelaTestaLatexComponent } from './tela-testa-latex.component';

describe('TelaTestaLatexComponent', () => {
  let component: TelaTestaLatexComponent;
  let fixture: ComponentFixture<TelaTestaLatexComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TelaTestaLatexComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TelaTestaLatexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
