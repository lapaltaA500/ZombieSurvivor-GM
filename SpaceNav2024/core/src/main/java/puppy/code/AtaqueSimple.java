package puppy.code;

/**
 * Estrategia de ataque: Ataque básico por colisión (morder)
 */
public class AtaqueSimple implements EstrategiaAtaque {
    @Override
    public void atacar(Enemigo enemigo, float delta) {
        // El daño se aplica por colisión en GestorEntidades
        // No necesita lógica adicional aquí
    }
}