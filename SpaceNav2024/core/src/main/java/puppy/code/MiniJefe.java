package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MiniJefe extends Enemigo {
    private Sprite spr;
    private static final int SALUD_BASE = 150;
    private static final int DANIO_BASE = 3;
    
    public MiniJefe(float x, float y, Texture textura, Nave4 jugador) {
        super(x, y, 120, 120, SALUD_BASE, DANIO_BASE, jugador);
        this.spr = new Sprite(textura);
        this.spr.setSize(120, 120);
        this.velocidadBase = 45f;
    }
    
    // =========================================
    // IMPLEMENTACIÓN DE LAS OPERACIONES PRIMITIVAS
    // =========================================

    @Override
    protected void mover(float delta) {
        // MiniJefe también usa persecución base pero con diferente velocidad
        perseguirJugador(delta);
    }

    @Override
    protected void atacar(float delta) {
        // MiniJefe: el ataque se maneja por colisión en PantallaJuego
        // Podría añadir lógica especial de ataque aquí en el futuro
    }

    @Override
    protected void actualizarSprite(float delta) {
        spr.setPosition(posicion.x, posicion.y);
    }

    @Override
    protected void verificarMuerte(float delta) {
        // La muerte se verifica en PantallaJuego
        // Podría añadir efectos especiales al morir aquí
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        spr.draw(batch);
    }
    
    @Override
    public void escalarPorRonda(int ronda) {
        float factor = 1.0f + (ronda - 1) * 0.5f; 
        
        salud = (int)(SALUD_BASE * factor);
        danio = (int)(DANIO_BASE * factor);
        velocidadBase *= factor;
    }
}