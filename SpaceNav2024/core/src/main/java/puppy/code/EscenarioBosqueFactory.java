package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;

/**
 * Factory para escenario de bosque con zombies naturales
 */
public class EscenarioBosqueFactory implements EscenarioFactory {
    
    @Override
    public Texture crearFondo() {
        return GestorAssets.get().getTextura("fondo-juego"); // Temporal - luego añadiremos fondo-bosque
    }
    
    @Override
    public Music crearMusica() {
        return GestorAssets.get().getMusica("survival-theme");
    }
    
    @Override
    public Enemigo crearEnemigoBasico(float x, float y, Nave4 jugador) {
        Ball2 zombie = new Ball2(
            (int)x, (int)y, 
            60, 2, 2, 
            GestorAssets.get().getTextura("zombie"), 
            jugador
        );
        zombie.setEstrategiaMovimiento(new MovimientoPerseguir());
        zombie.setEstrategiaAtaque(new AtaqueSimple());
        return zombie;
    }
    
    @Override
    public Enemigo crearEnemigoEspecial(float x, float y, Nave4 jugador) {
        // Zombie rápido del bosque
        Ball2 zombieRapido = new Ball2(
            (int)x, (int)y, 
            60, 3, 3, 
            GestorAssets.get().getTextura("zombie"), 
            jugador
        );
        zombieRapido.setEstrategiaMovimiento(new MovimientoRapido());
        zombieRapido.setEstrategiaAtaque(new AtaqueSimple());
        return zombieRapido;
    }
    
    @Override
    public String getNombreEscenario() {
        return "Bosque Infectado";
    }
}