package puppy.code.bosses;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.ataques.AtaqueSimple;
import puppy.code.jugador.Nave4;
import puppy.code.movimiento.MovimientoPatronCiudad;

/**
 * Boss del bioma ciudad - Movimientos complejos pero predecibles
 */
public class BossCiudad extends BossBase {
    private static final int SALUD_BASE = 80;  // REDUCIDO de 150 a 80
    private static final int DANIO_BASE = 3;   // Un poco más de daño
    private float cooldownHabilidad = 0f;
    private int faseAtaque = 0;
    
    public BossCiudad(float x, float y, Texture textura, Nave4 jugador) {
        super(x, y, textura, jugador, SALUD_BASE, DANIO_BASE, 35f); // Más lento pero estratégico
        setEstrategiaMovimiento(new MovimientoPatronCiudad());
        setEstrategiaAtaque(new AtaqueSimple());
    }
    
    @Override
    protected void mover(float delta) {
        super.mover(delta);
        activarHabilidadEspecial(delta);
    }
    
    @Override
    protected void atacar(float delta) {
        // Boss ciudad: ciclos de ataque predecibles
        cooldownHabilidad -= delta;
        if (cooldownHabilidad <= 0) {
            faseAtaque = (faseAtaque + 1) % 3;
            cooldownHabilidad = 3.0f; // Cambia cada 3 segundos
        }
    }
    
    @Override
    protected void verificarMuerte(float delta) {
        if (!estaVivo()) {
            efectosMuerte();
        }
    }
    
    @Override
    public void activarHabilidadEspecial(float delta) {
        // Boss ciudad: patrones de movimiento defensivos
        // La lógica está principalmente en MovimientoPatronCiudad
    }
    
    @Override
    protected void efectosMuerte() {
        // Efecto especial: explosión urbana
        // (podría añadir sonido de explosión o efectos visuales)
    }
    
    @Override
    public void escalarPorRonda(int ronda) {
        float factor = 1.0f + (ronda - 1) * 0.3f;
        
        salud = (int)(SALUD_BASE * factor);
        danio = (int)(DANIO_BASE * factor);
        velocidadBase = 35f * factor;
    }
}