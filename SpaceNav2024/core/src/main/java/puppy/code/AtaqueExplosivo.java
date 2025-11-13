// AtaqueExplosivo.java
package puppy.code;

/**
 * Estrategia de ataque: Al morir, causa daño en área
 */
public class AtaqueExplosivo implements EstrategiaAtaque {
    private boolean explosionActiva = false;
    
    @Override
    public void atacar(Enemigo enemigo, float delta) {
        // Si el enemigo muere, activar explosión
        if (!enemigo.estaVivo() && !explosionActiva) {
            explosionActiva = true;
            // Aquí se podría añadir lógica para dañar en un área
            // Por ahora, es un placeholder para futura implementación
        }
    }
}