package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 extends Enemigo {
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, Nave4 jugador) {
        super(x, y, 60, 60, 1, 1, jugador);
        this.spr = new Sprite(tx);
        this.spr.setSize(60, 60);

        float velocidadMagnitud = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
        float variacion = 0.7f + (float) Math.random() * 0.6f;
        this.velocidadBase = velocidadMagnitud * 25f * variacion;

        this.spr.setPosition(posicion.x, posicion.y);
    }

    @Override
    protected void actualizarSprite(float delta) {
        spr.setPosition(posicion.x, posicion.y);
    }

    @Override
    protected void verificarMuerte(float delta) {
        // La muerte se verifica en GestorEntidades
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        spr.draw(batch);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    // Getters para compatibilidad (legacy)
    public int getXSpeed() { 
        return (int) (velocidadBase / 35f);
    }
    
    public void setXSpeed(int xSpeed) { 
        float nuevaVelocidad = (float) Math.sqrt(xSpeed * xSpeed + getySpeed() * getySpeed());
        this.velocidadBase = nuevaVelocidad * 35f;
    }
    
    public int getySpeed() { 
        return (int) (velocidadBase / 35f);
    }
    
    public void setySpeed(int ySpeed) { 
        float nuevaVelocidad = (float) Math.sqrt(getXSpeed() * getXSpeed() + ySpeed * ySpeed);
        this.velocidadBase = nuevaVelocidad * 35f;
    }
}