// EscenarioCiudadFactory.java  
package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;

/**
 * Factory para escenario urbano con zombies ágiles
 */
public class EscenarioCiudadFactory implements EscenarioFactory {
    
    @Override
    public Texture crearFondo() {
        return GestorAssets.get().getTextura("fondo-juego"); // Temporal
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
        // Zombies de ciudad se mueven en zigzag
        zombie.setEstrategiaMovimiento(new MovimientoZigZag());
        zombie.setEstrategiaAtaque(new AtaqueSimple());
        return zombie;
    }
    
    @Override
    public Enemigo crearEnemigoEspecial(float x, float y, Nave4 jugador) {
        // Zombie circular de ciudad
        Ball2 zombieCircular = new Ball2(
            (int)x, (int)y, 
            60, 2, 2, 
            GestorAssets.get().getTextura("zombie"), 
            jugador
        );
        zombieCircular.setEstrategiaMovimiento(new MovimientoCircular());
        zombieCircular.setEstrategiaAtaque(new AtaqueSimple());
        return zombieCircular;
    }
    
    @Override
    public String getNombreEscenario() {
        return "Ciudad Apocalíptica";
    }
}