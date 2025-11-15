package puppy.code;

import com.badlogic.gdx.graphics.Texture;

/**
 * Boss del bioma bosque - Veloz pero frágil
 * Vida reducida significativamente para mayor equilibrio
 */
public class BossBosque extends BossBase {
    private static final int SALUD_BASE = 60;  // REDUCIDO de 150 a 60
    private static final int DANIO_BASE = 2;
    private float cooldownHabilidad = 0f;
    
    public BossBosque(float x, float y, Texture textura, Nave4 jugador) {
        super(x, y, textura, jugador, SALUD_BASE, DANIO_BASE, 65f); // Más rápido
        setEstrategiaMovimiento(new MovimientoRapidoBoss());
        setEstrategiaAtaque(new AtaqueSimple());
    }
    
    @Override
    protected void mover(float delta) {
        // Usa la estrategia de movimiento asignada
        super.mover(delta);
        activarHabilidadEspecial(delta);
    }
    
    @Override
    protected void atacar(float delta) {
        // Boss bosque ataca más frecuentemente pero con menos daño
        if (cooldownHabilidad <= 0) {
            // Ataque rápido cada 2 segundos
            cooldownHabilidad = 2.0f;
        }
        cooldownHabilidad -= delta;
    }
    
    @Override
    protected void verificarMuerte(float delta) {
        if (!estaVivo()) {
            efectosMuerte();
        }
    }
    
    @Override
    public void activarHabilidadEspecial(float delta) {
        // Boss bosque: aumenta velocidad temporalmente ocasionalmente
        if (Math.random() < 0.01f) { // 0.1% de probabilidad por frame
            velocidadBase *= 1.5f; // Boost de velocidad
        }
    }
    
    @Override
    protected void efectosMuerte() {
        // Efecto especial: el boss bosque se desintegra rápidamente
        // (podría añadir partículas o animación aquí)
    }
    
    @Override
    public void escalarPorRonda(int ronda) {
        float factor = 1.0f + (ronda - 1) * 0.25f; // Escala más suave
        
        salud = (int)(SALUD_BASE * factor);
        danio = (int)(DANIO_BASE * factor);
        velocidadBase = 65f * factor; // Velocidad base escalada
    }
}