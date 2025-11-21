package puppy.code.escenarios;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.ataques.AtaqueSimple;
import puppy.code.bosses.BossCiudad;
import puppy.code.core.Enemigo;
import puppy.code.enemigos.Ball2;
import puppy.code.gestores.GestorAssets;
import puppy.code.jugador.Nave4;
import puppy.code.movimiento.MovimientoCircular;
import puppy.code.movimiento.MovimientoZigZag;

import com.badlogic.gdx.audio.Music;

/**
 * Factory para escenario urbano con zombies ágiles
 */
public class EscenarioCiudadFactory implements EscenarioFactory {
    
    @Override
    public Texture crearFondo() {
        return GestorAssets.get().getTextura("fondo-ciudad"); 
    }
    
    @Override
    public Music crearMusica() {
        return GestorAssets.get().getMusica("musica-ciudad"); 
    }
    
    @Override
    public Enemigo crearEnemigoBasico(float x, float y, Nave4 jugador) {
        Ball2 zombie = new Ball2(
            (int)x, (int)y, 
            60, 2, 2, 
            GestorAssets.get().getTextura("zombie-ciudad"), 
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
            GestorAssets.get().getTextura("zombie-ciudad"), 
            jugador
        );
        zombieCircular.setEstrategiaMovimiento(new MovimientoCircular());
        zombieCircular.setEstrategiaAtaque(new AtaqueSimple());
        return zombieCircular;
    }
    
    @Override
    public Enemigo crearBoss(float x, float y, Nave4 jugador) {
        return new BossCiudad(x, y, GestorAssets.get().getTextura("boss-ciudad"), jugador);
    }
    
 // Proyectiles para ciudad (autos)
    @Override
    public Texture getTexturaProyectilMiniJefe() {
        return GestorAssets.get().getTextura("proyectil-auto");
    }
    
    @Override
    public float getEscalaProyectilMiniJefe() {
        return 0.6f; // Autos más grandes
    }
    
    @Override
    public int getDanioProyectilMiniJefe() {
        return 2; // Daño mayor de autos
    }
    
    @Override
    public String getNombreEscenario() {
        return "Ciudad Apocalíptica";
    }
}