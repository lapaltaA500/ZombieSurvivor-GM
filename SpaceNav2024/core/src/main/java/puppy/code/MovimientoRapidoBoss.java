package puppy.code;

import com.badlogic.gdx.math.Vector2;

/**
 * Movimiento rápido con cambios de dirección impredecibles para BossBosque
 */
public class MovimientoRapidoBoss implements EstrategiaMovimiento {
    private float tiempoCambio = 0;
    private float tiempoUltimoCambio = 0;
    private Vector2 direccionActual = new Vector2();
    
    @Override
    public void mover(Enemigo enemigo, float delta) {
        tiempoCambio += delta;
        Nave4 jugador = enemigo.getJugador();
        
        if (jugador != null && jugador.estaVivo()) {
            float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
            float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
            
            float enemigoX = enemigo.getPosicion().x + enemigo.getArea().width / 2;
            float enemigoY = enemigo.getPosicion().y + enemigo.getArea().height / 2;
            
            Vector2 direccion = new Vector2(jugadorX - enemigoX, jugadorY - enemigoY);
            float distancia = direccion.len();
            
            // DISTANCIA MÍNIMA: Evitar que se pegue al jugador
            float distanciaMinima = 80f;
            if (distancia < distanciaMinima) {
                // Alejarse del jugador si está demasiado cerca
                direccion.set(enemigoX - jugadorX, enemigoY - jugadorY);
                distancia = direccion.len();
            }
            
            if (distancia > 0.1f) {
                direccion.nor();
                
                // SUAVIZADO: Interpolar hacia la nueva dirección
                float factorSuavizado = 0.3f; // 0 = sin suavizado, 1 = máximo suavizado
                direccionActual.lerp(direccion, factorSuavizado);
                
                // Cambios de dirección menos frecuentes y más suaves
                if (tiempoCambio - tiempoUltimoCambio > 1.2f + Math.random() * 1.0f) {
                    float anguloCambio = (float)(Math.random() * 60 - 30); // REDUCIDO: -30 a +30 grados
                    direccionActual.rotate(anguloCambio);
                    tiempoUltimoCambio = tiempoCambio;
                }
                
                // Velocidad con límite máximo
                float variacion = 0.9f + (float)Math.random() * 0.2f; // REDUCIDA variación
                float velocidadMaxima = 45f; // LÍMITE DE VELOCIDAD
                float velocidadActual = Math.min(enemigo.getVelocidadBase() * variacion * delta, velocidadMaxima * delta);
                
                enemigo.getPosicion().x += direccionActual.x * velocidadActual;
                enemigo.getPosicion().y += direccionActual.y * velocidadActual;
                
                enemigo.getArea().setPosition(enemigo.getPosicion().x, enemigo.getPosicion().y);
            }
        }
    }
}