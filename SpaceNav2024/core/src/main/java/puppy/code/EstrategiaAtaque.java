package puppy.code;

/**
 * Interfaz Strategy para comportamientos de ataque
 */
public interface EstrategiaAtaque {
    void atacar(Enemigo enemigo, float delta);
}