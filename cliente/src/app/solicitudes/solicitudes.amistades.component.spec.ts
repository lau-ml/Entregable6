import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitudesAmistadesComponent } from './solicitudes.amistades.component';

describe('SolicitudesAmistadesComponent', () => {
  let component: SolicitudesAmistadesComponent;
  let fixture: ComponentFixture<SolicitudesAmistadesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitudesAmistadesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SolicitudesAmistadesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
