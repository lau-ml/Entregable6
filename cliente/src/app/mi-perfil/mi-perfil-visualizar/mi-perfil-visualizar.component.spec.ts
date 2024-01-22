import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiPerfilVisualizarComponent } from './mi-perfil-visualizar.component';

describe('MiPerfilVisualizarComponent', () => {
  let component: MiPerfilVisualizarComponent;
  let fixture: ComponentFixture<MiPerfilVisualizarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiPerfilVisualizarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MiPerfilVisualizarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
