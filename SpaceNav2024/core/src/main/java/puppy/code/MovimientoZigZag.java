// MovimientoZigZag.java
package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Estrategia de movimiento: Se mueve en zigzag mientras persigue
 */
public class MovimientoZigZag implements EstrategiaMovimiento {
    private float tiempo = 0;
    
    @Override
    public void mover(Enemigo enemigo, float delta) {
        tiempo += delta;
        Nave4 jugador = enemigo.getJugador();
        if (jugador != null && jugador.estaVivo()) {
            float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
            float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
            
            float enemigoX = enemigo.getPosicion().x + enemigo.getArea().width / 2;
            float enemigoY = enemigo.getPosicion().y + enemigo.getArea().height / 2;
            
            Vector2 direccion = new Vector2(jugadorX - enemigoX, jugadorY - enemigoY);
            
            if (direccion.len() > 0.1f) {
                direccion.nor();
                
                // Añadir movimiento lateral en zigzag
                float amplitud = 50f;
                float frecuencia = 5f;
                float lateral = MathUtils.sin(tiempo * frecuencia) * amplitud * delta;
                
                // Calcular vector perpendicular a la dirección
                Vector2 perpendicular = new Vector2(-direccion.y, direccion.x);
                
                float variacionVelocidad = 0.8f + (float) Math.random() * 0.4f;
                float velocidadActual = enemigo.getVelocidadBase() * delta * variacionVelocidad;
                
                enemigo.getPosicion().x += direccion.x * velocidadActual + perpendicular.x * lateral;
                enemigo.getPosicion().y += direccion.y * velocidadActual + perpendicular.y * lateral;
                
                enemigo.getArea().setPosition(enemigo.getPosicion().x, enemigo.getPosicion().y);
            }
        }
    }
}