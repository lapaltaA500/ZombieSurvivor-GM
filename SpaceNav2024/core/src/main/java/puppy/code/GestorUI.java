package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

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
    
    // Referencia al boss actual
    private Enemigo bossActual;
    private Texture texturaBlanca;
    
    public GestorUI(SpaceNavigation juego, Nave4 jugador, GestorSpawn gestorSpawn) {
        this.juego = juego;
        this.jugador = jugador;
        this.gestorSpawn = gestorSpawn;
        crearTexturaBlanca();
    }
    
    /**
     * Crea una textura blanca de 1x1 píxel para dibujar formas
     */
    private void crearTexturaBlanca() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texturaBlanca = new Texture(pixmap);
        pixmap.dispose();
    }
    
    /**
     * Dibuja todos los elementos de la interfaz
     */
    public void dibujar(SpriteBatch batch) {
        dibujarEncabezado(batch);
        dibujarBarraVidaBoss(batch);
    }
    
    /**
     * Dibuja la barra de vida del boss como elemento UI separado
     */
    private void dibujarBarraVidaBoss(SpriteBatch batch) {
        if (bossActual != null && bossActual.estaVivo() && estadoActual == EstadosJuego.MINIJEFE) {
            // Configuración de la barra - coordenadas fijas de pantalla
            float barraAncho = 400f;
            float barraAlto = 25f;
            float barraX = PantallaJuego.WORLD_WIDTH / 2f - barraAncho / 2f;
            float barraY = PantallaJuego.WORLD_HEIGHT - 50f;
            
            // Calcular porcentaje de vida
            int saludActual = bossActual.getSalud();
            int saludMaxima = bossActual.getSaludMaxima();
            float porcentajeVida = Math.max(0f, Math.min(1f, (float) saludActual / saludMaxima));
            
            // Guardar color original
            Color colorOriginal = batch.getColor();
            
            // Fondo de la barra (rojo oscuro)
            batch.setColor(0.3f, 0f, 0f, 0.9f);
            batch.draw(texturaBlanca, barraX, barraY, barraAncho, barraAlto);
            
            // Vida actual (verde que se vuelve rojo)
            float r = 2.0f * (1 - porcentajeVida);
            float g = 2.0f * porcentajeVida;
            batch.setColor(r, g, 0f, 1f);
            batch.draw(texturaBlanca, barraX, barraY, barraAncho * porcentajeVida, barraAlto);
            
            // Borde blanco
            batch.setColor(1f, 1f, 1f, 1f);
            float grosorBorde = 2f;
            // Lados superior e inferior
            batch.draw(texturaBlanca, barraX, barraY, barraAncho, grosorBorde);
            batch.draw(texturaBlanca, barraX, barraY + barraAlto - grosorBorde, barraAncho, grosorBorde);
            // Lados izquierdo y derecho
            batch.draw(texturaBlanca, barraX, barraY, grosorBorde, barraAlto);
            batch.draw(texturaBlanca, barraX + barraAncho - grosorBorde, barraY, grosorBorde, barraAlto);
            
            // Restaurar color
            batch.setColor(colorOriginal);
            
            // Texto de vida
            juego.getFont().getData().setScale(1.3f);
            String textoVida = "BOSS: " + saludActual + " / " + saludMaxima;
            float textoWidth = 150f; // Ancho aproximado
            juego.getFont().draw(batch, textoVida, barraX + (barraAncho - textoWidth) / 2f, barraY + barraAlto + 20f);
            
            // Restaurar escala de fuente para otros textos
            juego.getFont().getData().setScale(2f);
        }
    }
    
    /**
     * Encabezado existente 
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

        // Nombre del escenario
        if (!nombreEscenario.isEmpty()) {
            juego.getFont().draw(batch, nombreEscenario, 
                    PantallaJuego.WORLD_WIDTH / 2f - 150, PantallaJuego.WORLD_HEIGHT - 20);
        }
    }
    
    /**
     * Actualizar referencia al boss actual
     */
    public void setBossActual(Enemigo boss) {
        this.bossActual = boss;
    }
    
    // Setters existentes
    public void setRonda(int ronda) {
        this.ronda = ronda;
    }
    
    public void setEstadoActual(int estadoActual) {
        this.estadoActual = estadoActual;
    }

    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }
    
    /**
     * Liberar recursos
     */
    public void dispose() {
        if (texturaBlanca != null) {
            texturaBlanca.dispose();
        }
    }
}