package puppy.code.movimiento;

import puppy.code.core.Enemigo;

/**
 * Interfaz Strategy para comportamientos de movimiento
 */
public interface EstrategiaMovimiento {
    void mover(Enemigo enemigo, float delta);
}