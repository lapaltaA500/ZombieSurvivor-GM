// MovimientoCircular.java
package puppy.code.movimiento;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import puppy.code.core.Enemigo;
import puppy.code.jugador.Nave4;

/**
 * Estrategia de movimiento: Se mueve en círculos alrededor del jugador
 */
public class MovimientoCircular implements EstrategiaMovimiento {
    private float angulo = 0;
    private float radio = 150f;
    
    @Override
    public void mover(Enemigo enemigo, float delta) {
        Nave4 jugador = enemigo.getJugador();
        if (jugador != null && jugador.estaVivo()) {
            float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
            float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
            
            // Movimiento circular alrededor del jugador
            angulo += delta * 2f; // Velocidad angular
            
            float targetX = jugadorX + MathUtils.cos(angulo) * radio;
            float targetY = jugadorY + MathUtils.sin(angulo) * radio;
            
            // Mover suavemente hacia la posición del círculo
            Vector2 direccion = new Vector2(targetX - enemigo.getPosicion().x, 
                                          targetY - enemigo.getPosicion().y);
            
            if (direccion.len() > 0.1f) {
                direccion.nor();
                float velocidadActual = enemigo.getVelocidadBase() * delta;
                enemigo.getPosicion().x += direccion.x * velocidadActual;
                enemigo.getPosicion().y += direccion.y * velocidadActual;
                
                enemigo.getArea().setPosition(enemigo.getPosicion().x, enemigo.getPosicion().y);
            }
        }
    }
}