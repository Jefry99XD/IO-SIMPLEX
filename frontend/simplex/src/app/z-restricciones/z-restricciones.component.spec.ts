import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZRestriccionesComponent } from './z-restricciones.component';

describe('ZRestriccionesComponent', () => {
  let component: ZRestriccionesComponent;
  let fixture: ComponentFixture<ZRestriccionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ZRestriccionesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ZRestriccionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
