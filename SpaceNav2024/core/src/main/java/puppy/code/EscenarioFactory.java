package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;

/**
 * Interface Abstract Factory para crear familias de elementos de escenario
 */
public interface EscenarioFactory {
    Texture crearFondo();
    Music crearMusica();
    Enemigo crearEnemigoBasico(float x, float y, Nave4 jugador);
    Enemigo crearEnemigoEspecial(float x, float y, Nave4 jugador);
    String getNombreEscenario();
}