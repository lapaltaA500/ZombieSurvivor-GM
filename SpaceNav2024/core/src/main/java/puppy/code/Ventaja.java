package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.gestores.GestorAssets;
import puppy.code.juego.SpaceNavigation;

/**
 * Sistema de ventajas que el jugador puede comprar con puntos
 */
public class Ventaja {
    public enum Tipo {
        DISPARO_AUTOMATICO,
        INMORTALIDAD,
        VIDAS_EXTRA
    }
    
    private Tipo tipo;
    private int costo;
    private float duracion;
    private String tecla;
    private String nombre;
    private Texture icono;
    private Texture iconoGris;
    private boolean disponible;
    private boolean activa;
    private float tiempoRestante;
    
    public Ventaja(Tipo tipo, int costo, float duracion, String tecla, String nombre, String descripcion) {
        this.tipo = tipo;
        this.costo = costo;
        this.duracion = duracion;
        this.tecla = tecla;
        this.nombre = nombre;
        this.disponible = false;
        this.activa = false;
        this.tiempoRestante = 0;
        
        // Cargar iconos
        cargarIconos();
    }
    
    private void cargarIconos() {
        GestorAssets gestorAssets = GestorAssets.get();
        switch (tipo) {
            case DISPARO_AUTOMATICO:
                icono = gestorAssets.getTextura("ventaja-disparo");
                iconoGris = gestorAssets.getTextura("ventaja-disparo-gris");
                break;
            case INMORTALIDAD:
                icono = gestorAssets.getTextura("ventaja-inmortalidad");
                iconoGris = gestorAssets.getTextura("ventaja-inmortalidad-gris");
                break;
            case VIDAS_EXTRA:
                icono = gestorAssets.getTextura("ventaja-vidas");
                iconoGris = gestorAssets.getTextura("ventaja-vidas-gris");
                break;
        }
        
        // Verificación de carga de iconos
        // Si no se cargan los iconos, usar un placeholder para evitar null
        if (icono == null) {
            System.err.println("ERROR: No se pudo cargar el icono para " + nombre);
            // Usar una textura existente como fallback
            icono = gestorAssets.getTextura("bullet");
        }
        if (iconoGris == null) {
            iconoGris = gestorAssets.getTextura("bullet");
        }
    }
    
    public void actualizar(float delta, int puntajeActual) {
        // Actualizar disponibilidad según el puntaje
        disponible = puntajeActual >= costo;
        
        // Actualizar tiempo si está activa
        if (activa && duracion > 0) {
            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                desactivar();
            }
        }
    }
    
    public boolean intentarActivar() {
        if (disponible && !activa) {
            activar();
            return true;
        }
        return false;
    }
    
    private void activar() {
        activa = true;
        tiempoRestante = duracion;
        // El costo se deduce en el GestorVentajas
    }
    
    private void desactivar() {
        activa = false;
        tiempoRestante = 0;
    }
    
    public void dibujar(SpriteBatch batch, float x, float y) {
        Texture iconoADibujar = disponible ? icono : iconoGris;
        if (iconoADibujar != null) {
            // Dibujar icono con tamaño consistente
            batch.draw(iconoADibujar, x, y, 60, 60);
            
            SpaceNavigation juego = SpaceNavigation.get(); 
            // Reducir escala de fuente para mejor legibilidad
            juego.getFont().getData().setScale(1.0f);
            
            // Dibujar tecla de activación (centrada en el icono)
            float textoX = x + 25;
            float textoY = y + 75;
            juego.getFont().draw(batch, "[" + tecla + "]", textoX - 10, textoY);
            
            // Dibujar nombre de la ventaja
            juego.getFont().draw(batch, nombre, x, y - 5);
            
            // Dibujar costo si no está disponible
            if (!disponible) {
                juego.getFont().draw(batch, costo + " pts", x + 5, y - 25);
            }
            
            // Dibujar tiempo restante si está activa
            if (activa && duracion > 0) {
                juego.getFont().draw(batch, String.format("%.1fs", tiempoRestante), x, y + 90);
            }
            
            // Restaurar escala de fuente para no afectar otros textos
            juego.getFont().getData().setScale(2f);
        } else {
            // Mensaje de error si el icono es nulo
            System.err.println("ERROR: Icono nulo para ventaja: " + nombre);
        }
    }
    
    // Getters
    public Tipo getTipo() { return tipo; }
    public int getCosto() { return costo; }
    public boolean isDisponible() { return disponible; }
    public boolean isActiva() { return activa; }
    public float getTiempoRestante() { return tiempoRestante; }
    public String getTecla() { return tecla; }
    public String getNombre() { return nombre; }

	public float getDuracion() {
		return duracion;
	}
}