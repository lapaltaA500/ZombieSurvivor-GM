package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Clase base Enemigo con Template Method y Strategy Pattern - CORREGIDA
 */
public abstract class Enemigo extends EntidadJuego implements Daniable {
    protected int salud;
    protected int saludMaxima;
    protected int danio;
    protected boolean invulnerable = false;
    protected Nave4 jugador;
    
    // Velocidad base para mayor dinamismo
    protected float velocidadBase = 60f;
    
    // Estrategias de movimiento y ataque
    protected EstrategiaMovimiento estrategiaMovimiento;
    protected EstrategiaAtaque estrategiaAtaque;
    
    public Enemigo(float x, float y, float ancho, float alto, int salud, int danio, Nave4 jugador) {
        super(x, y, ancho, alto);
        this.salud = salud;
        this.saludMaxima = salud;
        this.danio = danio;
        this.jugador = jugador;
        
        // Estrategias por defecto
        this.estrategiaMovimiento = new MovimientoPerseguir();
        this.estrategiaAtaque = new AtaqueSimple();
    }
    
    // =========================================
    // TEMPLATE METHOD: Ciclo de actualización
    // =========================================
    
    /**
     * Método final que define el esqueleto del comportamiento del enemigo
     * No puede ser sobrescrito por las subclases (Template Method)
     */
    @Override
    public final void actualizar(float delta) {
        mover(delta);
        atacar(delta);
        actualizarSprite(delta);
        verificarMuerte(delta);
    }
    
    // =========================================
    // OPERACIONES PRIMITIVAS (para subclases)
    // =========================================
    
    /**
     * Operación primitiva: Movimiento del enemigo
     * CORRECCIÓN: Quitamos @Override porque no existe en EntidadJuego
     */
    protected void mover(float delta) {
        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.mover(this, delta);
        }
    }
    
    /**
     * Operación primitiva: Lógica de ataque  
     * CORRECCIÓN: Quitamos @Override porque no existe en EntidadJuego
     */
    protected void atacar(float delta) {
        if (estrategiaAtaque != null) {
            estrategiaAtaque.atacar(this, delta);
        }
    }
    
    /**
     * Operación primitiva: Actualización visual
     * Debe ser implementada por las subclases
     */
    protected abstract void actualizarSprite(float delta);
    
    /**
     * Operación primitiva: Verificación de estado de muerte
     * Debe ser implementada por las subclases
     */
    protected abstract void verificarMuerte(float delta);
    
    // =========================================
    // MÉTODOS PARA CAMBIAR ESTRATEGIAS
    // =========================================
    
    public void setEstrategiaMovimiento(EstrategiaMovimiento estrategiaMovimiento) {
        this.estrategiaMovimiento = estrategiaMovimiento;
    }
    
    public void setEstrategiaAtaque(EstrategiaAtaque estrategiaAtaque) {
        this.estrategiaAtaque = estrategiaAtaque;
    }
    
    // =========================================
    // Hook method para persecución (ahora opcional)
    // =========================================
    
    /**
     * Hook method - las subclases pueden usarlo si su estrategia lo requiere
     */
    protected void perseguirJugador(float delta) {
        // Comportamiento base de persecución
        if (jugador != null && jugador.estaVivo()) {
            float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
            float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
            
            float enemigoX = posicion.x + area.width / 2;
            float enemigoY = posicion.y + area.height / 2;
            
            Vector2 direccion = new Vector2(jugadorX - enemigoX, jugadorY - enemigoY);
            
            if (direccion.len() > 0.1f) {
                direccion.nor();
                
                float variacionVelocidad = 0.8f + (float) Math.random() * 0.4f;
                float velocidadActual = velocidadBase * delta * variacionVelocidad;
                posicion.x += direccion.x * velocidadActual;
                posicion.y += direccion.y * velocidadActual;
                
                area.setPosition(posicion.x, posicion.y);
            }
        }
    }
    
    // =========================================
    // Implementación de Daniable
    // =========================================
    
    @Override
    public void recibirDanio(int danio) {
        if (!invulnerable) {
            salud -= danio;
        }
    }
    
    @Override
    public boolean estaVivo() {
        return salud > 0;
    }
    
    @Override
    public int getSalud() {
        return salud;
    }
    
    @Override
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }
    
    @Override
    public boolean estaInvulnerable() {
        return invulnerable;
    }
    
    public int getDanio() { 
        return danio; 
    }
    
    // =========================================
    // Escalado por ronda
    // =========================================
    
    @Override
    public void escalarPorRonda(int ronda) {
        float factor = 1.0f + (ronda - 1) * 0.3f;
        
        salud = (int)(saludMaxima * factor);
        danio = (int)(danio * factor);
        velocidadBase *= factor;
    }
    
    // =========================================
    // Getters para las estrategias
    // =========================================
    
    public float getVelocidadBase() {
        return velocidadBase;
    }
    
    public Nave4 getJugador() {
        return jugador;
    }
    
    public EstrategiaMovimiento getEstrategiaMovimiento() {
        return estrategiaMovimiento;
    }
    
    public EstrategiaAtaque getEstrategiaAtaque() {
        return estrategiaAtaque;
    }
    
    public int getSaludMaxima() {
        return saludMaxima;
    }
}