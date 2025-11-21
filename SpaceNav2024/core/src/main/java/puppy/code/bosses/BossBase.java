package puppy.code.bosses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.core.Enemigo;
import puppy.code.jugador.Nave4;

/**
 * Clase base abstracta para todos los bosses
 * Implementa el Template Method de Enemigo con funcionalidad común
 */
public abstract class BossBase extends Enemigo {
    protected Sprite spr;
    
    public BossBase(float x, float y, Texture textura, Nave4 jugador, 
                   int saludBase, int danioBase, float velocidadBase) {
        super(x, y, 120, 120, saludBase, danioBase, jugador);
        this.spr = new Sprite(textura);
        this.spr.setSize(120, 120);
        this.velocidadBase = velocidadBase;
    }
    
    // =========================================
    // IMPLEMENTACIÓN TEMPLATE METHOD
    // =========================================
    
    @Override
    protected void actualizarSprite(float delta) {
        spr.setPosition(posicion.x, posicion.y);
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        spr.draw(batch);
    }
    
    // =========================================
    // MÉTODOS ABSTRACTOS PARA SUBCLASES
    // =========================================
    
    /**
     * Habilidad especial única de cada boss
     */
    public abstract void activarHabilidadEspecial(float delta);
    
    /**
     * Efectos especiales al morir
     */
    protected abstract void efectosMuerte();
}