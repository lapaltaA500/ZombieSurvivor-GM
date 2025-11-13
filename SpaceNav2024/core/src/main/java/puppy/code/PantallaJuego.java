package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * PantallaJuego refactorizada con gestión por managers
 * Delega responsabilidades en clases especializadas
 */
public class PantallaJuego implements Screen {
    private final SpaceNavigation game;
    private final SpriteBatch batch;

    // Sistema de viewport para coordenadas lógicas
    public static final float WORLD_WIDTH = 1200f;
    public static final float WORLD_HEIGHT = 800f;
    private OrthographicCamera camera;
    private Viewport viewport;

    // Entidad jugador
    private Nave4 nave;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;

    // Managers especializados
    private GestorAssets gestorAssets;
    private GestorEntidades gestorEntidades;
    private GestorSpawn gestorSpawn;
    private GestorUI gestorUI;

    // Elementos del juego
    private MiniJefe miniJefe;
    private Music musicaFondo;
    private Sprite spriteFondo;

    // Estados del juego 
    private int estadoActual = EstadosJuego.JUGANDO_ENEMIGOS;
    private int tiempoTransicion = 0;
    private static final int TIEMPO_TRANSICION_MAX = 120;

    public PantallaJuego(SpaceNavigation game,
            int ronda,
            int vidas,
            int score,
            int velXAsteroides,
            int velYAsteroides,
            int cantAsteroides) {
    	
		this.game = game;
		this.batch = game.getBatch(); 
		this.ronda = ronda;
		
		// Configuración de dificultad
		this.velXAsteroides = velXAsteroides + 1;
		this.velYAsteroides = velYAsteroides + 1; 
		this.cantAsteroides = cantAsteroides + 2;
		
		// Inicializar sistemas básicos
		inicializarSistemasBasicos();
		
		// Inicializar gestor de assets
		gestorAssets = GestorAssets.get();
		
		// Configurar puntuación
		Puntaje.get().reset();
		if (score > 0) Puntaje.get().sumar(score);
		
		// Inicializar jugador
		inicializarJugador(vidas);
		
		// Inicializar managers
		inicializarManagers();
		
		// Configurar música de fondo
		musicaFondo = gestorAssets.getMusica("survival-theme");
		musicaFondo.setLooping(true);
		musicaFondo.setVolume(0.7f);
		musicaFondo.play();
	}

    /**
     * Inicializa los sistemas básicos del juego
     */
    private void inicializarSistemasBasicos() {
        // Sistema de cámara y viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        
        // Fondo del juego
        gestorAssets = GestorAssets.get();
        spriteFondo = new Sprite(gestorAssets.getTextura("fondo-juego"));
        spriteFondo.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        spriteFondo.setPosition(0, 0);
    }

    /**
     * Inicializa la entidad jugador
     */
    private void inicializarJugador(int vidas) {
        Texture txNave = gestorAssets.getTextura("survivor");
        Texture txBala = gestorAssets.getTextura("bullet");
        Sound sonidoChoque = gestorAssets.getSonido("player-hurt");
        Sound sonidoBala = gestorAssets.getSonido("gun-shot");
        
        nave = new Nave4((int)(WORLD_WIDTH/2 - 22), 60, txNave, sonidoChoque, txBala, sonidoBala);
        nave.setVidas(vidas);
    }

    /**
     * Inicializa todos los managers especializados
     */
    private void inicializarManagers() {
        // Gestor de entidades (enemigos y balas)
        gestorEntidades = new GestorEntidades(nave);
        
        // Gestor de spawn de enemigos
        gestorSpawn = new GestorSpawn(this, gestorEntidades);
        gestorSpawn.iniciarNuevaOleada(ronda);
        
        // Gestor de interfaz de usuario
        gestorUI = new GestorUI(game, nave, gestorSpawn);
        
        estadoActual = EstadosJuego.JUGANDO_ENEMIGOS;
    }

    // ==================================================
    // Ciclo de renderizado principal
    // ==================================================
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Aplicar viewport para escalado consistente
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        
        // Dibujar fondo
        spriteFondo.draw(batch);
        
        // Actualizar y dibujar UI
        gestorUI.setRonda(ronda);
        gestorUI.setEstadoActual(estadoActual);
        gestorUI.dibujar(batch);

        // Ejecutar lógica según estado actual 
        if (estadoActual == EstadosJuego.JUGANDO_ENEMIGOS) {
            renderJugandoEnemigos(delta);
        } else if (estadoActual == EstadosJuego.MINIJEFE) {
            renderMiniJefe(delta);
        } else if (estadoActual == EstadosJuego.TRANSICION_RONDA) {
            renderTransicion();
        } else if (estadoActual == EstadosJuego.GAME_OVER) {
            // Manejo de game over...
        }

        batch.end();

        verificarCambioEstado();
    }

    /**
     * Renderiza el estado normal de juego con enemigos
     */
    private void renderJugandoEnemigos(float delta) {
        // Actualizar rotación del jugador
        actualizarRotacionNave();
        
        // Actualizar jugador
        nave.draw(batch, this);
        
        // Actualizar sistemas de spawn y entidades
        gestorSpawn.actualizar(delta);
        gestorEntidades.actualizar(delta);
        gestorEntidades.dibujar(batch);
    }

    /**
     * Renderiza el estado de mini jefe
     */
    private void renderMiniJefe(float delta) {
        actualizarRotacionNave();
        
        if (miniJefe != null) {
            // Actualizar mini jefe
            miniJefe.actualizar(delta);
            miniJefe.dibujar(batch);

            // Verificar colisiones de balas con mini jefe
            for (Bullet bala : gestorEntidades.getBalas()) {
                if (!bala.isDestroyed() && bala.getArea().overlaps(miniJefe.getArea())) {
                    miniJefe.recibirDanio(1);
                    bala.destruir();
                    
                    if (!miniJefe.estaVivo()) {
                        Puntaje.get().sumar(100);
                        estadoActual = EstadosJuego.TRANSICION_RONDA;
                        tiempoTransicion = TIEMPO_TRANSICION_MAX;
                        break;
                    }
                }
            }
            
            // Limpiar balas destruidas
            gestorEntidades.getBalas().removeIf(Bullet::isDestroyed);
            
            // Dibujar balas restantes
            for (Bullet bala : gestorEntidades.getBalas()) {
                if (!bala.isDestroyed()) {
                    bala.draw(batch);
                }
            }

            // Colisión mini jefe - jugador
            if (!nave.estaInvulnerable() && miniJefe.getArea().overlaps(nave.getArea())) {
                nave.recibirDanio(miniJefe.getDanio());
            }
        }
        
        nave.draw(batch, this);
    }

    /**
     * Renderiza la transición entre rondas
     */
    private void renderTransicion() {
        game.getFont().draw(batch,
                "¡Ronda " + (ronda + 1) + " completada!",
                WORLD_WIDTH / 2f - 100,
                WORLD_HEIGHT / 2f
        );
        tiempoTransicion--;
    }

    /**
     * Actualiza la rotación de la nave basándose en el mouse
     */
    private void actualizarRotacionNave() {
        com.badlogic.gdx.math.Vector3 posicionMouse = new com.badlogic.gdx.math.Vector3(
            Gdx.input.getX(), 
            Gdx.input.getY(), 
            0
        );
        
        camera.unproject(posicionMouse);
        nave.actualizarRotacionMouse(posicionMouse.x, posicionMouse.y);
    }

    /**
     * Verifica y realiza cambios de estado del juego
     */
    private void verificarCambioEstado() {
        // Transición de oleada normal a mini jefe
        if (estadoActual == EstadosJuego.JUGANDO_ENEMIGOS && 
            gestorSpawn.getEnemigosSpawneados() >= gestorSpawn.getTotalEnemigosRonda() && 
            gestorEntidades.getCantidadEnemigos() == 0) {
            
            // Generar mini jefe
            miniJefe = new MiniJefe(
                    WORLD_WIDTH / 2f,
                    WORLD_HEIGHT - 100f,
                    gestorAssets.getTextura("mini-jefe"),
                    nave
            );
            miniJefe.escalarPorRonda(ronda);
            estadoActual = EstadosJuego.MINIJEFE;
        }

        // Transición a siguiente ronda
        if (estadoActual == EstadosJuego.TRANSICION_RONDA && tiempoTransicion <= 0) {
            Screen siguientePantalla = new PantallaJuego(
                    game,
                    ronda + 1,
                    nave.getVidas(),
                    Puntaje.get().getScore(),
                    velXAsteroides + 1,
                    velYAsteroides + 1,
                    cantAsteroides + 2
            );
            siguientePantalla.resize(1200, 800);
            game.setScreen(siguientePantalla);
            dispose();
        }

        // Game over
        if (!nave.estaVivo()) {
            if (musicaFondo != null) {
                musicaFondo.stop();
            }
            
            if (Puntaje.get().getScore() > Puntaje.get().getHighScore())
                Puntaje.get().setHighScore(Puntaje.get().getScore());
                
            Screen pantallaGameOver = new PantallaGameOver(game);
            pantallaGameOver.resize(1200, 800);
            game.setScreen(pantallaGameOver);
            dispose();
        }
    }

    // ==================================================
    // Métodos de compatibilidad
    // ==================================================
    
    /** Permite a Nave4 registrar balas */
    public void agregarBala(Bullet b) {
        gestorEntidades.agregarBala(b);
    }

    // Getters para los managers
    public int getRonda() { return ronda; }
    public int getVelXAsteroides() { return velXAsteroides; }
    public int getVelYAsteroides() { return velYAsteroides; }
    public Nave4 getJugador() { return nave; }

    // ==================================================
    // Métodos del ciclo de vida Screen
    // ==================================================
    @Override
    public void show() {
        if (musicaFondo != null && !musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        if (musicaFondo != null && musicaFondo.isPlaying()) {
            musicaFondo.pause();
        }
    }

    @Override
    public void resume() {
        if (musicaFondo != null && !musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    @Override
    public void hide() {
        if (musicaFondo != null) {
            musicaFondo.pause();
        }
    }

    @Override
    public void dispose() {
        // Los assets son gestionados por GestorAssets
        // No es necesario liberarlos aquí individualmente
    }
}
