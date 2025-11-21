package puppy.code.escenarios;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.core.Enemigo;
import puppy.code.jugador.Nave4;

import com.badlogic.gdx.audio.Music;

/**
 * Interface Abstract Factory para crear familias de elementos de escenario
 */
public interface EscenarioFactory {
    Texture crearFondo();
    Music crearMusica();
    Enemigo crearEnemigoBasico(float x, float y, Nave4 jugador);
    Enemigo crearEnemigoEspecial(float x, float y, Nave4 jugador);
    
    // NUEVO MÉTODO QUE FALTA
    Enemigo crearBoss(float x, float y, Nave4 jugador);
    
 // Métodos para proyectiles del mini jefe
    Texture getTexturaProyectilMiniJefe();
    float getEscalaProyectilMiniJefe();
    int getDanioProyectilMiniJefe();
    
    String getNombreEscenario();
}