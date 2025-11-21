package puppy.code.ataques;

import puppy.code.core.Enemigo;

/**
 * Interfaz Strategy para comportamientos de ataque
 */
public interface EstrategiaAtaque {
    void atacar(Enemigo enemigo, float delta);
}