package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

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
        // ==================== TEXTURAS ====================
        
        // Jugador y elementos base
        cargarTextura("survivor", "survivor.png");
        cargarTextura("bullet", "bullet.png");
        cargarTextura("mini-jefe", "mini-jefe.png");
        
        // Fondos de pantallas
        cargarTextura("fondo-menu", "fondo-menu.jpg");
        cargarTextura("fondo-gameover", "fondo-gameover.jpg");
        
        // Fondos temáticos para escenarios
        cargarTextura("fondo-bosque", "fondo-bosque.jpg");
        cargarTextura("fondo-ciudad", "fondo-ciudad.jpg");
        
        // Zombies temáticos
        cargarTextura("zombie-bosque", "zombie-bosque.png");
        cargarTextura("zombie-ciudad", "zombie-ciudad.png");
        
        // ==================== SONIDOS ====================
        cargarSonido("explosion", "zombie-death.ogg");
        cargarSonido("player-hurt", "player-hurt.ogg");
        cargarSonido("gun-shot", "gun-shot.ogg");
        
        // ==================== MÚSICA ====================
        cargarMusica("musica-bosque", "musica-bosque.wav");
        cargarMusica("musica-ciudad", "musica-ciudad.wav");
    }
    
    private void cargarTextura(String nombre, String ruta) {
        try {
            texturas.put(nombre, new Texture(Gdx.files.internal(ruta)));
            System.out.println("✓ Textura cargada: " + nombre);
        } catch (Exception e) {
            System.err.println("✗ Error cargando textura: " + ruta);
            // No hacemos fallbacks automáticos, se manejará en getTextura
        }
    }
    
    private void cargarSonido(String nombre, String ruta) {
        try {
            sonidos.put(nombre, Gdx.audio.newSound(Gdx.files.internal(ruta)));
            System.out.println("✓ Sonido cargado: " + nombre);
        } catch (Exception e) {
            System.err.println("✗ Error cargando sonido: " + ruta);
        }
    }
    
    private void cargarMusica(String nombre, String ruta) {
        try {
            musicas.put(nombre, Gdx.audio.newMusic(Gdx.files.internal(ruta)));
            System.out.println("✓ Música cargada: " + nombre);
        } catch (Exception e) {
            System.err.println("✗ Error cargando música: " + ruta);
        }
    }
    
    // Getters con fallbacks inteligentes pero SIMPLES
    public Texture getTextura(String nombre) { 
        Texture textura = texturas.get(nombre);
        if (textura == null) {
            System.err.println("Textura no encontrada: " + nombre + ", usando fallback...");
            
            // Fallbacks específicos SIN Pixmap
            if (nombre.contains("fondo")) {
                if (texturas.containsKey("fondo-bosque")) return texturas.get("fondo-bosque");
                if (texturas.containsKey("fondo-ciudad")) return texturas.get("fondo-ciudad");
                if (texturas.containsKey("fondo-menu")) return texturas.get("fondo-menu");
            }
            if (nombre.contains("zombie")) {
                if (texturas.containsKey("zombie-bosque")) return texturas.get("zombie-bosque");
                if (texturas.containsKey("zombie-ciudad")) return texturas.get("zombie-ciudad");
            }
            
            // Último recurso: cualquier textura disponible
            if (!texturas.isEmpty()) {
                return texturas.values().iterator().next();
            }
        }
        return textura;
    }
    
    public Sound getSonido(String nombre) { 
        Sound sonido = sonidos.get(nombre);
        if (sonido == null) {
            System.err.println("Sonido no encontrado: " + nombre);
        }
        return sonido;
    }
    
    public Music getMusica(String nombre) { 
        Music musica = musicas.get(nombre);
        if (musica == null) {
            System.err.println("Música no encontrada: " + nombre);
            // Intentar cualquier música disponible
            if (!musicas.isEmpty()) {
                musica = musicas.values().iterator().next();
            }
        }
        return musica;
    }
    
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
    
    // Método para debug: listar assets cargados
    public void listarAssets() {
        System.out.println("=== TEXTURAS CARGADAS ===");
        for (String nombre : texturas.keySet()) {
            System.out.println("  " + nombre);
        }
        System.out.println("=== MÚSICAS CARGADAS ===");
        for (String nombre : musicas.keySet()) {
            System.out.println("  " + nombre);
        }
    }
}