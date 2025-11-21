package puppy.code.ataques;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import puppy.code.jugador.Nave4;

public class ProyectilMiniJefe {
    private float x, y;
    private float vx, vy;
    private boolean destruido = false;
    private Sprite spr;
    private int danio;
    private float tiempoVida = 0f;
    private float tiempoVidaMax = 4.0f;
    
    // Hitbox ajustable según el tipo de proyectil
    private Rectangle hitbox;
    private float factorReduccionHitbox;
    
    // Velocidad ajustable según el tipo de proyectil
    private float velocidadBase;
    
    private static final float WORLD_WIDTH = 1200f;
    private static final float WORLD_HEIGHT = 800f;
    
    // Constructor modificado para aceptar tipo de proyectil (grande o normal)
    public ProyectilMiniJefe(float x, float y, float anguloRadianes, Texture textura, int danio, float escala, boolean esGrande) {
        this.x = x;
        this.y = y;
        this.danio = danio;
        
        // Velocidad diferente según el tipo de proyectil
        if (esGrande) {
            this.velocidadBase = 220f; // Más lento para proyectiles grandes
            this.factorReduccionHitbox = 0.4f; // Hitbox más grande para proyectiles grandes (40%)
        } else {
            this.velocidadBase = 280f; // Velocidad normal
            this.factorReduccionHitbox = 0.3f; // Hitbox normal (30%)
        }
        
        this.vx = MathUtils.cos(anguloRadianes) * velocidadBase;
        this.vy = MathUtils.sin(anguloRadianes) * velocidadBase;
        
        this.spr = new Sprite(textura);
        
        // Ajustar tamaño según la escala
        float ancho = textura.getWidth() * escala;
        float alto = textura.getHeight() * escala;
        this.spr.setSize(ancho, alto);
        this.spr.setPosition(x, y);
        
        // Crear hitbox según el tipo de proyectil
        float hitboxAncho = ancho * factorReduccionHitbox;
        float hitboxAlto = alto * factorReduccionHitbox;
        this.hitbox = new Rectangle(
            x + (ancho - hitboxAncho) / 2,  // Centrar la hitbox
            y + (alto - hitboxAlto) / 2,
            hitboxAncho,
            hitboxAlto
        );
        
        // Rotar sprite en dirección del movimiento
        float anguloGrados = anguloRadianes * MathUtils.radiansToDegrees;
        spr.setRotation(anguloGrados);
    }
    
    public void actualizar(float delta) {
        if (destruido) return;
        
        x += vx * delta;
        y += vy * delta;
        spr.setPosition(x, y);
        
        // Actualizar también la posición de la hitbox de manera más precisa
        float ancho = spr.getWidth();
        float alto = spr.getHeight();
        float hitboxAncho = ancho * factorReduccionHitbox;
        float hitboxAlto = alto * factorReduccionHitbox;
        
        hitbox.setPosition(
            x + (ancho - hitboxAncho) / 2,
            y + (alto - hitboxAlto) / 2
        );
        
        tiempoVida += delta;
        if (tiempoVida >= tiempoVidaMax) {
            destruido = true;
        }
        
        // Verificar límites del mundo
        if (x < -100 || x > WORLD_WIDTH + 100 || y < -100 || y > WORLD_HEIGHT + 100) {
            destruido = true;
        }
    }
    
    public void dibujar(SpriteBatch batch) {
        if (!destruido) {
            spr.draw(batch);
        }
    }
    
    public boolean verificarColision(Nave4 jugador) {
        if (!destruido && jugador.estaVivo() && !jugador.estaInvulnerable()) {
            // Usar la hitbox reducida en lugar del bounding rectangle completo
            // Añadir un pequeño margen adicional de seguridad
            Rectangle areaJugador = jugador.getArea();
            
            // Reducir ligeramente el área del jugador para colisiones
            Rectangle areaJugadorAjustada = new Rectangle(
                areaJugador.x + areaJugador.width * 0.1f,
                areaJugador.y + areaJugador.height * 0.1f,
                areaJugador.width * 0.8f,
                areaJugador.height * 0.8f
            );
            
            if (hitbox.overlaps(areaJugadorAjustada)) {
                destruido = true;
                return true;
            }
        }
        return false;
    }
    
    public boolean estaDestruido() {
        return destruido;
    }
    
    public int getDanio() {
        return danio;
    }
    
    // Getter para la hitbox (útil para debug)
    public Rectangle getHitbox() {
        return hitbox;
    }
}