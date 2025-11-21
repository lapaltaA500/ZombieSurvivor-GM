package puppy.code.movimiento;

import com.badlogic.gdx.math.Vector2;

import puppy.code.core.Enemigo;
import puppy.code.jugador.Nave4;

/**
 * Movimiento en patrones predecibles para BossCiudad
 */
public class MovimientoPatronCiudad implements EstrategiaMovimiento {
    private int fase = 0;
    private float tiempoFase = 0;
    private Vector2 direccionSuavizada = new Vector2();
    
    @Override
    public void mover(Enemigo enemigo, float delta) {
        tiempoFase += delta;
        
        // Cambiar fase cada 4 segundos
        if (tiempoFase > 4.0f) {
            fase = (fase + 1) % 4;
            tiempoFase = 0;
        }
        
        Nave4 jugador = enemigo.getJugador();
        if (jugador != null && jugador.estaVivo()) {
            float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
            float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
            
            float enemigoX = enemigo.getPosicion().x + enemigo.getArea().width / 2;
            float enemigoY = enemigo.getPosicion().y + enemigo.getArea().height / 2;
            
            Vector2 direccion = new Vector2();
            float distancia = new Vector2(jugadorX - enemigoX, jugadorY - enemigoY).len();
            
            // DISTANCIA MÍNIMA
            float distanciaMinima = 100f;
            boolean demasiadoCerca = distancia < distanciaMinima;
            
            switch (fase) {
                case 0: // Persecución directa
                    if (demasiadoCerca) {
                        direccion.set(enemigoX - jugadorX, enemigoY - jugadorY);
                    } else {
                        direccion.set(jugadorX - enemigoX, jugadorY - enemigoY);
                    }
                    break;
                case 1: // Movimiento circular izquierda
                    direccion.set(-(jugadorY - enemigoY), jugadorX - enemigoX);
                    break;
                case 2: // Movimiento circular derecha  
                    direccion.set(jugadorY - enemigoY, -(jugadorX - enemigoX));
                    break;
                case 3: // Retroceso estratégico
                    direccion.set(enemigoX - jugadorX, enemigoY - jugadorY);
                    direccion.nor().scl(0.4f); // REDUCIDA velocidad de retroceso
                    break;
            }
            
            if (direccion.len() > 0.1f) {
                direccion.nor();
                
                // SUAVIZADO de movimiento
                direccionSuavizada.lerp(direccion, 0.2f);
                
                float velocidadActual = enemigo.getVelocidadBase() * delta;
                enemigo.getPosicion().x += direccionSuavizada.x * velocidadActual;
                enemigo.getPosicion().y += direccionSuavizada.y * velocidadActual;
                
                enemigo.getArea().setPosition(enemigo.getPosicion().x, enemigo.getPosicion().y);
            }
        }
    }
}