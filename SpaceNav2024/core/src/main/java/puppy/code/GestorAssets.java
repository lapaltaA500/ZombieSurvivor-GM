package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor centralizado de assets del juego (Singleton)
 * Carga todos los recursos una sola vez y los reutiliza
 */
public class GestorAssets implements Disposable {
    private static GestorAssets instancia;
    
    private Map<String, Texture> texturas;
    private Map<String, Sound> sonidos;
    private Map<String, Music> musicas;
    
    private GestorAssets() {
        texturas = new HashMap<>();
        sonidos = new HashMap<>();
        musicas = new HashMap<>();
        cargarTodosLosAssets();
    }
    
    public static GestorAssets get() {
        if (instancia == null) {
            instancia = new GestorAssets();
        }
        return instancia;
    }
    
    private void cargarTodosLosAssets() {
        // Cargar texturas
        cargarTextura("survivor", "survivor.png");
        cargarTextura("zombie", "zombie.png");
        cargarTextura("bullet", "bullet.png");
        cargarTextura("mini-jefe", "mini-jefe.png");
        cargarTextura("fondo-juego", "fondo-juego.jpg");
        cargarTextura("fondo-menu", "fondo-menu.jpg");
        cargarTextura("fondo-gameover", "fondo-gameover.jpg");
        
        // Cargar sonidos
        cargarSonido("explosion", "zombie-death.ogg");
        cargarSonido("player-hurt", "player-hurt.ogg");
        cargarSonido("gun-shot", "gun-shot.ogg");
        
        // Cargar música
        cargarMusica("survival-theme", "survival-theme.wav");
    }
    
    private void cargarTextura(String nombre, String ruta) {
        texturas.put(nombre, new Texture(Gdx.files.internal(ruta)));
    }
    
    private void cargarSonido(String nombre, String ruta) {
        sonidos.put(nombre, Gdx.audio.newSound(Gdx.files.internal(ruta)));
    }
    
    private void cargarMusica(String nombre, String ruta) {
        musicas.put(nombre, Gdx.audio.newMusic(Gdx.files.internal(ruta)));
    }
    
    // Getters
    public Texture getTextura(String nombre) { return texturas.get(nombre); }
    public Sound getSonido(String nombre) { return sonidos.get(nombre); }
    public Music getMusica(String nombre) { return musicas.get(nombre); }
    
    @Override
    public void dispose() {
        for (Texture textura : texturas.values()) {
            textura.dispose();
        }
        for (Sound sonido : sonidos.values()) {
            sonido.dispose();
        }
        for (Music musica : musicas.values()) {
            musica.dispose();
        }
    }
}