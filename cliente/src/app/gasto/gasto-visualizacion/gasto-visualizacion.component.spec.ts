import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GastoVisualizacionComponent } from './gasto-visualizacion.component';

describe('GastoVisualizacionComponent', () => {
  let component: GastoVisualizacionComponent;
  let fixture: ComponentFixture<GastoVisualizacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GastoVisualizacionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GastoVisualizacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
