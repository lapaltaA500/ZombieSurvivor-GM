package puppy.code;

import com.badlogic.gdx.math.Vector2;

/**
 * Estrategia de movimiento: Persigue al jugador directamente
 */
public class MovimientoPerseguir implements EstrategiaMovimiento {
    @Override
    public void mover(Enemigo enemigo, float delta) {
        Nave4 jugador = enemigo.getJugador();
        if (jugador != null && jugador.estaVivo()) {
            float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
            float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
            
            float enemigoX = enemigo.getPosicion().x + enemigo.getArea().width / 2;
            float enemigoY = enemigo.getPosicion().y + enemigo.getArea().height / 2;
            
            Vector2 direccion = new Vector2(jugadorX - enemigoX, jugadorY - enemigoY);
            
            if (direccion.len() > 0.1f) {
                direccion.nor();
                
                float variacionVelocidad = 0.8f + (float) Math.random() * 0.4f;
                float velocidadActual = enemigo.getVelocidadBase() * delta * variacionVelocidad;
                enemigo.getPosicion().x += direccion.x * velocidadActual;
                enemigo.getPosicion().y += direccion.y * velocidadActual;
                
                enemigo.getArea().setPosition(enemigo.getPosicion().x, enemigo.getPosicion().y);
            }
        }
    }
}