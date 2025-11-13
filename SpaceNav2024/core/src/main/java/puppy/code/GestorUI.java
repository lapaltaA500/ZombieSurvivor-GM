package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Gestor de interfaz de usuario
 * Centraliza todo el dibujado de elementos de UI
 */
public class GestorUI {
    private SpaceNavigation juego;
    private Nave4 jugador;
    private GestorSpawn gestorSpawn;
    private int ronda;
    private int estadoActual; 
    private String nombreEscenario = "";
    
    public GestorUI(SpaceNavigation juego, Nave4 jugador, GestorSpawn gestorSpawn) {
        this.juego = juego;
        this.jugador = jugador;
        this.gestorSpawn = gestorSpawn;
    }
    
    /**
     * Dibuja todos los elementos de la interfaz
     */
    public void dibujar(SpriteBatch batch) {
        dibujarEncabezado(batch);
    }
    
    /**
     * Dibuja el encabezado con información del juego
     */
    private void dibujarEncabezado(SpriteBatch batch) {
        // Información básica: vidas y ronda
        CharSequence textoVidas = "Vidas: " + jugador.getVidas() + " Oleada: " + ronda;
        juego.getFont().getData().setScale(2f);
        juego.getFont().draw(batch, textoVidas, 10, 30);
        
        // Puntuación actual
        juego.getFont().draw(batch, "Score:" + Puntaje.get().getScore(),
                PantallaJuego.WORLD_WIDTH - 150, 30);
        
        // Mejor puntuación
        juego.getFont().draw(batch, "HighScore:" + Puntaje.get().getHighScore(),
                PantallaJuego.WORLD_WIDTH / 2f - 100, 30);

        // Progreso de la oleada
        int enemigosTotales = gestorSpawn.getTotalEnemigosRonda();
        String progresoOleada = "Zombis: " + gestorSpawn.getEnemigosSpawneados() + "/" + enemigosTotales;
        juego.getFont().draw(batch, progresoOleada, PantallaJuego.WORLD_WIDTH / 2f - 100, 60);

        // Estado especial del juego
        if (estadoActual == EstadosJuego.MINIJEFE) {
            juego.getFont().draw(batch, "¡MINI JEFE!",
                    PantallaJuego.WORLD_WIDTH / 2f - 50, 90);
        } else if (estadoActual == EstadosJuego.TRANSICION_RONDA) {
            juego.getFont().draw(batch, "¡Oleada " + ronda + " completada!",
                    PantallaJuego.WORLD_WIDTH / 2f - 120, 90);
        }

        // Nombre del escenario (NUEVO)
        if (!nombreEscenario.isEmpty()) {
            juego.getFont().draw(batch, nombreEscenario, 
                    PantallaJuego.WORLD_WIDTH / 2f - 150, PantallaJuego.WORLD_HEIGHT - 20);
        }
    }
    
    // Setters para actualizar estado
    public void setRonda(int ronda) {
        this.ronda = ronda;
    }
    
    public void setEstadoActual(int estadoActual) {
        this.estadoActual = estadoActual;
    }

    // NUEVO: Setter para el nombre del escenario
    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }
}