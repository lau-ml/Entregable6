import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiPerfilEditarComponent } from './mi-perfil-editar.component';

describe('MiPerfilEditarComponent', () => {
  let component: MiPerfilEditarComponent;
  let fixture: ComponentFixture<MiPerfilEditarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiPerfilEditarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MiPerfilEditarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
