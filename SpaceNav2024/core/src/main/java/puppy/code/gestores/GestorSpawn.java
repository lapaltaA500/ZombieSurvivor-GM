package puppy.code.gestores;

import java.util.Random;

import puppy.code.core.Enemigo;
import puppy.code.enemigos.Ball2;
import puppy.code.juego.PantallaJuego;

/**
 * Gestor de aparición de enemigos
 * Controla el spawn dinámico con frecuencia progresiva por rondas
 */
public class GestorSpawn {
    private PantallaJuego pantalla;
    private GestorEntidades gestorEntidades;
    private float temporizadorSpawn = 0f;
    private int enemigosSpawneados = 0;
    private int enemigosRestantesOleada = 0;
    
    // Configuración del sistema de spawn
    private static final float INTERVALO_SPAWN_BASE = 1.5f;
    private static final float INTERVALO_SPAWN_MINIMO = 0.4f;
    private static final float ACELERACION_POR_RONDA = 0.15f;
    private static final int ENEMIGOS_BASE_POR_RONDA = 12;
    private static final int INCREMENTO_ENEMIGOS_POR_RONDA = 4;
    
    public GestorSpawn(PantallaJuego pantalla, GestorEntidades gestorEntidades) {
        this.pantalla = pantalla;
        this.gestorEntidades = gestorEntidades;
    }
    
    /**
     * Inicia una nueva oleada de enemigos
     */
    public void iniciarNuevaOleada(int ronda) {
        enemigosSpawneados = 0;
        enemigosRestantesOleada = calcularTotalEnemigosRonda(ronda);
        temporizadorSpawn = 0f;
    }
    
    /**
     * Actualiza la lógica de spawn
     */
    public void actualizar(float delta) {
        if (enemigosSpawneados < enemigosRestantesOleada) {
            temporizadorSpawn += delta;
            float intervaloActual = calcularIntervaloSpawnActual(pantalla.getRonda());
            
            if (temporizadorSpawn >= intervaloActual) {
                spawnearEnemigo();
                temporizadorSpawn = 0f;
                enemigosSpawneados++;
            }
        }
    }
    
    /**
     * Crea un nuevo enemigo en posición aleatoria del borde
     */
    private void spawnearEnemigo() {
        Random random = new Random();
        
        // Determinar posición de spawn (código existente)
        float spawnX, spawnY;
        int lado = random.nextInt(4);
        
        switch (lado) {
            case 0: // arriba
                spawnX = random.nextInt((int) PantallaJuego.WORLD_WIDTH);
                spawnY = PantallaJuego.WORLD_HEIGHT + 50;
                break;
            case 1: // derecha
                spawnX = PantallaJuego.WORLD_WIDTH + 50;
                spawnY = random.nextInt((int) PantallaJuego.WORLD_HEIGHT);
                break;
            case 2: // abajo
                spawnX = random.nextInt((int) PantallaJuego.WORLD_WIDTH);
                spawnY = -50;
                break;
            case 3: // izquierda
            default:
                spawnX = -50;
                spawnY = random.nextInt((int) PantallaJuego.WORLD_HEIGHT);
        }
        
        // USAR ABSTRACT FACTORY PARA CREAR ENEMIGOS
        Enemigo nuevoEnemigo;
        
        if (random.nextFloat() < 0.8f) { // 80% enemigos básicos
            nuevoEnemigo = pantalla.getEscenarioFactory().crearEnemigoBasico(spawnX, spawnY, pantalla.getJugador());
        } else { // 20% enemigos especiales
            nuevoEnemigo = pantalla.getEscenarioFactory().crearEnemigoEspecial(spawnX, spawnY, pantalla.getJugador());
        }
        
        // Aplicar escalado por ronda
        nuevoEnemigo.escalarPorRonda(pantalla.getRonda());
        gestorEntidades.agregarEnemigo((Ball2) nuevoEnemigo);
    }

    /**
     * Calcula el intervalo de spawn actual considerando la ronda
     */
    private float calcularIntervaloSpawnActual(int ronda) {
        float intervalo = INTERVALO_SPAWN_BASE - (ronda - 1) * ACELERACION_POR_RONDA;
        return Math.max(INTERVALO_SPAWN_MINIMO, intervalo);
    }
    
    /**
     * Calcula el total de enemigos para la ronda actual
     */
    private int calcularTotalEnemigosRonda(int ronda) {
        return ENEMIGOS_BASE_POR_RONDA + (ronda - 1) * INCREMENTO_ENEMIGOS_POR_RONDA;
    }
    
    // Getters para el sistema de UI
    public int getEnemigosRestantesOleada() {
        return enemigosRestantesOleada;
    }
    
    public int getEnemigosSpawneados() {
        return enemigosSpawneados;
    }
    
    public int getTotalEnemigosRonda() {
        return calcularTotalEnemigosRonda(pantalla.getRonda());
    }
}