package puppy.code.bosses;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.ataques.AtaqueSimple;
import puppy.code.core.Enemigo;
import puppy.code.enemigos.Ball2;
import puppy.code.gestores.GestorAssets;
import puppy.code.jugador.Nave4;
import puppy.code.movimiento.MovimientoPatronCiudad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BOSS FINAL - El temido Profe
 * Combina habilidades de todos los jefes + invocación de zombies
 */
public class BossProfe extends BossBase {
    private static final int SALUD_BASE = 200;
    private static final int DANIO_BASE = 5;
    
    private float cooldownHabilidad = 0f;
    private float cooldownInvocacion = 0f;
    private int faseAtaque = 0;
    private Random random;
    
    // Sistema de invocación de zombies
    private List<Enemigo> zombiesInvocados;
    private java.util.function.Consumer<Enemigo> onInvocarCallback;
    
    public BossProfe(float x, float y, Texture textura, Nave4 jugador) {
        super(x, y, textura, jugador, SALUD_BASE, DANIO_BASE, 40f);
        setEstrategiaMovimiento(new MovimientoPatronCiudad());
        setEstrategiaAtaque(new AtaqueSimple());
        
        this.random = new Random();
        this.zombiesInvocados = new ArrayList<>();
    }
    
    @Override
    protected void mover(float delta) {
        super.mover(delta);
        activarHabilidadEspecial(delta);
    }
    
    @Override
    protected void atacar(float delta) {
        // Ciclos de ataque más complejos
        cooldownHabilidad -= delta;
        cooldownInvocacion -= delta;
        
        if (cooldownHabilidad <= 0) {
            faseAtaque = (faseAtaque + 1) % 4;
            cooldownHabilidad = 2.5f; // Cambia cada 2.5 segundos
        }
        
        // Invocar zombies cada 8 segundos
        if (cooldownInvocacion <= 0 && zombiesInvocados.size() < 3) {
            invocarZombies();
            cooldownInvocacion = 8.0f;
        }
    }
    
    private void invocarZombies() {
        if (onInvocarCallback != null) {
            for (int i = 0; i < 2; i++) { // Invoca 2 zombies
                float spawnX = posicion.x + random.nextFloat() * 200 - 100;
                float spawnY = posicion.y + random.nextFloat() * 200 - 100;
                
                // Crear zombie básico con textura especial
                Ball2 zombie = new Ball2(
                    (int)spawnX, (int)spawnY, 
                    60, 2, 2, 
                    GestorAssets.get().getTextura("zombie-bosque"), 
                    jugador
                );
                zombie.escalarPorRonda(5); // Zombies un poco más fuertes
                
                zombiesInvocados.add(zombie);
                onInvocarCallback.accept(zombie);
            }
        }
    }
    
    @Override
    protected void verificarMuerte(float delta) {
        if (!estaVivo()) {
            efectosMuerte();
            // Limpiar zombies invocados
            zombiesInvocados.clear();
        }
    }
    
    @Override
    public void activarHabilidadEspecial(float delta) {
        // Habilidad especial: Cambia entre patrones de los jefes anteriores
        switch (faseAtaque) {
            case 0:
                // Patrón Boss Bosque: aumento de velocidad ocasional
                if (random.nextFloat() < 0.02f) {
                    velocidadBase *= 1.3f;
                }
                break;
            case 1:
                // Patrón Boss Ciudad: movimientos defensivos
                // (ya implementado en MovimientoPatronCiudad)
                break;
            case 2:
                // Patrón personalizado: teletransporte breve
                if (random.nextFloat() < 0.01f) {
                    posicion.x = random.nextFloat() * 1000;
                    posicion.y = 600;
                    area.setPosition(posicion.x, posicion.y);
                }
                break;
        }
    }
    
    @Override
    protected void efectosMuerte() {
        // Efecto especial: el profe se transforma en una A+ :)
        // Podría añadir explosión de partículas o sonido especial
    }
    
    public void setOnInvocarCallback(java.util.function.Consumer<Enemigo> callback) {
        this.onInvocarCallback = callback;
    }
    
    public List<Enemigo> getZombiesInvocados() {
        return zombiesInvocados;
    }
    
    public void removerZombieInvocado(Enemigo zombie) {
        zombiesInvocados.remove(zombie);
    }
    
    @Override
    public void escalarPorRonda(int ronda) {
        // El boss final no escala por rondas, ya es el más fuerte
        // Pero podemos ajustar un poco si queremos
        float factor = 1.0f + (ronda - 4) * 0.1f;
        
        salud = (int)(SALUD_BASE * factor);
        danio = (int)(DANIO_BASE * factor);
        velocidadBase = 40f * factor;
    }
}