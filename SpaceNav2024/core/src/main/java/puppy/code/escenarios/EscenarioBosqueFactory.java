package puppy.code.escenarios;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.ataques.AtaqueSimple;
import puppy.code.bosses.BossBosque;
import puppy.code.core.Enemigo;
import puppy.code.enemigos.Ball2;
import puppy.code.gestores.GestorAssets;
import puppy.code.jugador.Nave4;
import puppy.code.movimiento.MovimientoPerseguir;
import puppy.code.movimiento.MovimientoRapido;

import com.badlogic.gdx.audio.Music;

/**
 * Factory para escenario de bosque con zombies naturales
 */
public class EscenarioBosqueFactory implements EscenarioFactory {
    
    @Override
    public Texture crearFondo() {
        return GestorAssets.get().getTextura("fondo-bosque"); 
    }
    
    @Override
    public Music crearMusica() {
        return GestorAssets.get().getMusica("musica-bosque");
    }
    
    @Override
    public Enemigo crearEnemigoBasico(float x, float y, Nave4 jugador) {
        Ball2 zombie = new Ball2(
            (int)x, (int)y, 
            60, 2, 2, 
            GestorAssets.get().getTextura("zombie-bosque"), 
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
            GestorAssets.get().getTextura("zombie-bosque"), 
            jugador
        );
        zombieRapido.setEstrategiaMovimiento(new MovimientoRapido());
        zombieRapido.setEstrategiaAtaque(new AtaqueSimple());
        return zombieRapido;
    }
    
    @Override
    public Enemigo crearBoss(float x, float y, Nave4 jugador) {
        return new BossBosque(x, y, GestorAssets.get().getTextura("boss-bosque"), jugador);
    }
    
 // Proyectiles para bosque (piedras)
    @Override
    public Texture getTexturaProyectilMiniJefe() {
        return GestorAssets.get().getTextura("proyectil-piedra");
    }
    
    @Override
    public float getEscalaProyectilMiniJefe() {
        return 0.3f; // Piedras más pequeñas
    }
    
    @Override
    public int getDanioProyectilMiniJefe() {
        return 1; // Daño base de piedras
    }
    
    @Override
    public String getNombreEscenario() {
        return "Bosque Infectado";
    }
}