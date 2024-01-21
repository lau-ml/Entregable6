import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitudesGruposComponent } from './solicitudes.grupos.component';

describe('SolicitudesGruposComponent', () => {
  let component: SolicitudesGruposComponent;
  let fixture: ComponentFixture<SolicitudesGruposComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitudesGruposComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SolicitudesGruposComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
