import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GastoCreacionComponent } from './gasto-creacion.component';

describe('GastoCreacionComponent', () => {
  let component: GastoCreacionComponent;
  let fixture: ComponentFixture<GastoCreacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GastoCreacionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GastoCreacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
