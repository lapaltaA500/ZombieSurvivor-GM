package puppy.code.gestores;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.Puntaje;
import puppy.code.Ventaja;
import puppy.code.Ventaja.Tipo;
import puppy.code.juego.PantallaJuego;
import puppy.code.jugador.Nave4;

/**
 * Gestor del sistema de ventajas del jugador
 */
public class GestorVentajas {
    private static GestorVentajas instancia;
    private List<Ventaja> ventajas;
    private Puntaje puntaje;
    
    private GestorVentajas() {
        ventajas = new ArrayList<>();
        puntaje = Puntaje.get();
        inicializarVentajas();
    }
    
    public static GestorVentajas get() {
        if (instancia == null) {
            instancia = new GestorVentajas();
        }
        return instancia;
    }
    
    private void inicializarVentajas() {
        // Ventaja 1: Disparo automático (150 puntos, 10 segundos)
        ventajas.add(new Ventaja(
            Ventaja.Tipo.DISPARO_AUTOMATICO,
            150, 
            10.0f, 
            "1", 
            "Disparo Automático", 
            "Dispara continuamente por 10 segundos"
        ));
        
        // Ventaja 2: Inmortalidad (300 puntos, 10 segundos)
        ventajas.add(new Ventaja(
            Ventaja.Tipo.INMORTALIDAD,
            300, 
            10.0f, 
            "2", 
            "Inmortalidad", 
            "Inmune a todo daño por 10 segundos"
        ));
        
        // Ventaja 3: Vidas extra (200 puntos, permanente)
        ventajas.add(new Ventaja(
            Ventaja.Tipo.VIDAS_EXTRA,
            200, 
            0, // Duración 0 = efecto instantáneo
            "3", 
            "Vidas Extra", 
            "Añade 2 vidas inmediatamente"
        ));
    }
    
    public void actualizar(float delta) {
        int puntajeActual = puntaje.getScore();
        for (Ventaja ventaja : ventajas) {
            ventaja.actualizar(delta, puntajeActual);
        }
    }
    
    public void activarVentaja(int indice, Nave4 jugador) {
        if (indice >= 0 && indice < ventajas.size()) {
            Ventaja ventaja = ventajas.get(indice);
            if (ventaja.isDisponible() && !ventaja.isActiva()) {
                // Verificar que tenga puntos suficientes
                if (puntaje.getScore() >= ventaja.getCosto()) {
                    // Aplicar ventaja
                    aplicarVentaja(ventaja, jugador);
                    // Restar puntos
                    puntaje.sumar(-ventaja.getCosto());
                }
            }
        }
    }
    
    private void aplicarVentaja(Ventaja ventaja, Nave4 jugador) {
        switch (ventaja.getTipo()) {
            case DISPARO_AUTOMATICO:
                jugador.activarDisparoAutomatico(ventaja.getDuracion());
                ventaja.intentarActivar();
                break;
            case INMORTALIDAD:
                jugador.activarInmortalidad(ventaja.getDuracion());
                ventaja.intentarActivar();
                break;
            case VIDAS_EXTRA:
                jugador.agregarVidas(2);
                // Las vidas extra son instantáneas, no necesitan activarse
                break;
        }
    }
    
    public void dibujar(SpriteBatch batch) {
        // Ajustar posición de las ventajas para mejor visualización
        float x = PantallaJuego.WORLD_WIDTH - 220; // Más a la izquierda
        float y = PantallaJuego.WORLD_HEIGHT - 150; // Más abajo para evitar superposiciones
        
        for (Ventaja ventaja : ventajas) {
            ventaja.dibujar(batch, x, y);
            y -= 120; // Más espacio entre ventajas
        }
    }
    
    public List<Ventaja> getVentajas() {
        return ventajas;
    }
}