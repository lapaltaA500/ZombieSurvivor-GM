package puppy.code;

/**
 * Interfaz Strategy para comportamientos de movimiento
 */
public interface EstrategiaMovimiento {
    void mover(Enemigo enemigo, float delta);
}