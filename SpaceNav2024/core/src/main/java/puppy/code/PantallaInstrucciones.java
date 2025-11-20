package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Pantalla de instrucciones - Diseño básico y bonito
 */
public class PantallaInstrucciones implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Sprite spriteFondo;

    // Constantes del mundo lógico
    private static final float WORLD_WIDTH = 1200f;
    private static final float WORLD_HEIGHT = 800f;

    public PantallaInstrucciones(SpaceNavigation game) {
        this.game = game;
        
        // Sistema de viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        
        // Cargar fondo usando GestorAssets
        Texture texturaFondo = GestorAssets.get().getTextura("fondo-instrucciones");
        spriteFondo = new Sprite(texturaFondo);
        spriteFondo.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        spriteFondo.setPosition(0, 0);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);

        viewport.apply();
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        // Dibujar fondo
        spriteFondo.draw(batch);
        
        // Título principal
        game.getFont().getData().setScale(2.5f);
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(batch, "INSTRUCCIONES", 400, 750);
        
        // Resetear escala y color para el contenido
        game.getFont().getData().setScale(1.5f);
        game.getFont().setColor(Color.WHITE);
        
        // ========== CONTROLES ==========
        game.getFont().setColor(Color.CYAN);
        game.getFont().draw(batch, "CONTROLES", 100, 680);
        
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Movimiento: WASD o Flechas", 120, 630);
        game.getFont().draw(batch, "Apuntar: Mouse", 120, 590);
        game.getFont().draw(batch, "Disparar: Clic o Espacio", 120, 550);
        
        // ========== OBJETIVO ==========
        game.getFont().setColor(Color.CYAN);
        game.getFont().draw(batch, "OBJETIVO", 600, 680);
        
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Sobrevive a las oleadas", 620, 630);
        game.getFont().draw(batch, "Derrota a los jefes", 620, 590);
        game.getFont().draw(batch, "Consigue alta puntuación", 620, 550);
        
        // ========== BIOMAS ==========
        game.getFont().setColor(Color.CYAN);
        game.getFont().draw(batch, "BIOMAS", 100, 480);
        
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Bosque: Zombies rápidos", 120, 430);
        game.getFont().draw(batch, "Ciudad: Zombies estratégicos", 120, 390);
        
        // ========== CONSEJOS ==========
        game.getFont().setColor(Color.CYAN);
        game.getFont().draw(batch, "CONSEJOS", 600, 480);
        
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Mantén la distancia", 620, 430);
        game.getFont().draw(batch, "Muévete constantemente", 620, 390);
        game.getFont().draw(batch, "Apunta a la cabeza", 620, 350);
        
        // Línea decorativa
        game.getFont().setColor(Color.GOLD);
        for (int i = 100; i < 1100; i += 20) {
            batch.draw(crearPixelBlanco(), i, 300, 10, 2);
        }
        
        // Información adicional
        game.getFont().setColor(Color.LIGHT_GRAY);
        game.getFont().getData().setScale(1.2f);
        game.getFont().draw(batch, "Cada ronda se vuelve más difícil", 400, 250);
        game.getFont().draw(batch, "Los jefes aparecen cada pocas rondas", 380, 210);
        
        // Instrucción para volver
        game.getFont().setColor(Color.GREEN);
        game.getFont().getData().setScale(1.3f);
        game.getFont().draw(batch, "Presiona ESC para volver al menú", 420, 100);
    
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Screen ss = new PantallaMenu(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }
    
    /**
     * Crea una textura blanca de 1x1 píxel para dibujar líneas
     */
    private Texture crearPixelBlanco() {
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
    
    @Override
    public void show() {}
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {
        // Liberar recursos si es necesario
    }
}
