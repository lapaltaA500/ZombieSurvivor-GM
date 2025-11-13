package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gestor centralizado de entidades del juego
 * Se encarga de actualizar y dibujar enemigos y balas
 * Maneja las colisiones entre entidades
 */
public class GestorEntidades {
    private List<Ball2> enemigos;
    private List<Bullet> balas;
    private Nave4 jugador;
    private Sound sonidoExplosion;
    
    public GestorEntidades(Nave4 jugador) {
        this.jugador = jugador;
        this.enemigos = new ArrayList<>();
        this.balas = new ArrayList<>();
        this.sonidoExplosion = GestorAssets.get().getSonido("explosion");
    }
    
    /**
     * Actualiza todas las entidades y verifica colisiones
     */
    public void actualizar(float delta) {
        actualizarBalas(delta);
        actualizarEnemigos(delta);
        verificarColisiones();
    }
    
    private void actualizarBalas(float delta) {
        Iterator<Bullet> iteradorBalas = balas.iterator();
        while (iteradorBalas.hasNext()) {
            Bullet bala = iteradorBalas.next();
            bala.update(delta);
            
            if (bala.isDestroyed()) {
                iteradorBalas.remove();
            }
        }
    }
    
    private void actualizarEnemigos(float delta) {
        for (Ball2 enemigo : enemigos) {
            enemigo.actualizar(delta);
        }
    }
    
    private void verificarColisiones() {
        // Colisiones bala-enemigo
        Iterator<Bullet> iteradorBalas = balas.iterator();
        while (iteradorBalas.hasNext()) {
            Bullet bala = iteradorBalas.next();
            
            Iterator<Ball2> iteradorEnemigos = enemigos.iterator();
            while (iteradorEnemigos.hasNext()) {
                Ball2 enemigo = iteradorEnemigos.next();
                if (bala.checkCollision(enemigo)) {
                    sonidoExplosion.play();
                    iteradorEnemigos.remove();
                    iteradorBalas.remove();
                    Puntaje.get().sumar(10);
                    break;
                }
            }
        }
        
        // Colisiones enemigo-jugador
        for (Ball2 enemigo : enemigos) {
            if (!jugador.estaInvulnerable() && enemigo.getArea().overlaps(jugador.getArea())) {
                jugador.recibirDanio(enemigo.getDanio());
            }
        }
    }
    
    /**
     * Dibuja todas las entidades activas
     */
    public void dibujar(SpriteBatch batch) {
        // Dibujar enemigos
        for (Ball2 enemigo : enemigos) {
            enemigo.dibujar(batch);
        }
        
        // Dibujar balas
        for (Bullet bala : balas) {
            if (!bala.isDestroyed()) {
                bala.draw(batch);
            }
        }
    }
    
    // Métodos de gestión de entidades
    public void agregarBala(Bullet bala) {
        if (bala != null) {
            balas.add(bala);
        }
    }
    
    public void agregarEnemigo(Ball2 enemigo) {
        if (enemigo != null) {
            enemigos.add(enemigo);
        }
    }
    
    public void limpiarEnemigos() {
        enemigos.clear();
    }
    
    public void limpiarBalas() {
        balas.clear();
    }
    
    // Getters
    public int getCantidadEnemigos() {
        return enemigos.size();
    }
    
    public List<Ball2> getEnemigos() {
        return enemigos;
    }
    
    public List<Bullet> getBalas() {
        return balas;
    }
}