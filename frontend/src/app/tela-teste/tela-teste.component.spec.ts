import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TelaTesteComponent } from './tela-teste.component';

describe('TelaTesteComponent', () => {
  let component: TelaTesteComponent;
  let fixture: ComponentFixture<TelaTesteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TelaTesteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TelaTesteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
