package puppy.code.enemigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.ataques.ProyectilMiniJefe;
import puppy.code.core.Enemigo;
import puppy.code.jugador.Nave4;

import java.util.ArrayList;
import java.util.List;

public class MiniJefe extends Enemigo {
    private Sprite spr;
    private static final int SALUD_BASE = 70;
    private static final int DANIO_BASE = 3;
    
    private float cooldownDisparo = 0f;
    
    // Campos para el sistema de proyectiles por escenario
    private Texture texturaProyectil;
    private float escalaProyectil;
    private int danioProyectil;
    
    // Cambiamos de List<Bullet> a List<ProyectilMiniJefe>
    private List<ProyectilMiniJefe> proyectiles;
    private java.util.function.Consumer<ProyectilMiniJefe> onDisparoCallback;
    
    // Campo para identificar el tipo de boss y su patrón de ataque
    private String tipoBoss;
    
    public MiniJefe(float x, float y, Texture textura, Nave4 jugador, 
                   Texture texturaProyectil, float escalaProyectil, int danioProyectil, String tipoBoss) {
        super(x, y, 120, 120, SALUD_BASE, DANIO_BASE, jugador);
        this.spr = new Sprite(textura);
        this.spr.setSize(120, 120);
        this.velocidadBase = 45f;
        
        // Configuración de proyectiles del escenario
        this.texturaProyectil = texturaProyectil;
        this.escalaProyectil = escalaProyectil;
        this.danioProyectil = danioProyectil;
        this.proyectiles = new ArrayList<>();
        
        // Identificar el tipo de boss para el patrón de ataque
        this.tipoBoss = tipoBoss;
    }
    
    // =========================================
    // IMPLEMENTACIÓN DE LAS OPERACIONES PRIMITIVAS
    // =========================================

    @Override
    protected void mover(float delta) {
        // MiniJefe también usa persecución base pero con diferente velocidad
        perseguirJugador(delta);
    }

    @Override
    protected void atacar(float delta) {
        cooldownDisparo -= delta;
        
        // Diferentes intervalos de disparo según el tipo de boss
        float intervaloDisparo;
        if ("CIUDAD".equals(tipoBoss)) {
            intervaloDisparo = 6.0f; // Boss Ciudad: dispara cada 6 segundos
        } else {
            intervaloDisparo = 1.8f; // Boss Bosque: dispara cada 1.8 segundos
        }
        
        if (cooldownDisparo <= 0 && jugador != null && jugador.estaVivo()) {
            // Usar patrón de ataque específico para cada boss
            realizarPatronAtaque();
            cooldownDisparo = intervaloDisparo;
        }
        
        // Actualizar proyectiles existentes
        actualizarProyectiles(delta);
    }
    
    /**
     * PATRÓN DE ATAQUE ESPECÍFICO para cada tipo de mini jefe
     * Boss Bosque: 3 proyectiles en abanico
     * Boss Ciudad: 1 proyectil grande directo
     */
    private void realizarPatronAtaque() {
        if ("CIUDAD".equals(tipoBoss)) {
            // Boss Ciudad - un solo disparo grande y directo
            float angulo = calcularAnguloHaciaJugador();
            crearProyectilGrande(angulo);
        } else {
            // Boss Bosque - patrón de abanico con 3 proyectiles
            float anguloBase = calcularAnguloHaciaJugador();
            crearProyectil(anguloBase);
            crearProyectil(anguloBase - 0.6f);
            crearProyectil(anguloBase + 0.6f);
        }
    }
    
    // Método para crear proyectiles del escenario (tamaño normal)
    private void crearProyectil(float angulo) {
        ProyectilMiniJefe proyectil = new ProyectilMiniJefe(
            posicion.x + area.width / 2 - 20,
            posicion.y + area.height / 2 - 20,
            angulo,
            texturaProyectil,
            danioProyectil,
            escalaProyectil,
            false // No es proyectil grande
        );
        
        if (onDisparoCallback != null) {
            onDisparoCallback.accept(proyectil);
        }
    }
    
    // Método para crear proyectiles grandes (solo para Boss Ciudad)
    private void crearProyectilGrande(float angulo) {
        // Proyectil más grande para Boss Ciudad
        ProyectilMiniJefe proyectil = new ProyectilMiniJefe(
            posicion.x + area.width / 2 - 30, // Posición ajustada para centro
            posicion.y + area.height / 2 - 30,
            angulo,
            texturaProyectil,
            danioProyectil * 2, // Doble daño para el proyectil grande
            escalaProyectil * 1.8f, // 80% más grande
            true // Es proyectil grande
        );
        
        if (onDisparoCallback != null) {
            onDisparoCallback.accept(proyectil);
        }
    }
    
    // Actualizar proyectiles existentes
    private void actualizarProyectiles(float delta) {
        proyectiles.removeIf(proyectil -> {
            proyectil.actualizar(delta);
            return proyectil.estaDestruido();
        });
    }

    private float calcularAnguloHaciaJugador() {
        float jugadorX = jugador.getXFloat() + jugador.getSprite().getWidth() / 2;
        float jugadorY = jugador.getYFloat() + jugador.getSprite().getHeight() / 2;
        float bossX = posicion.x + area.width / 2;
        float bossY = posicion.y + area.height / 2;
        
        return (float) Math.atan2(jugadorY - bossY, jugadorX - bossX);
    }

    // Cambiamos el callback para usar ProyectilMiniJefe en lugar de Bullet
    public void setOnDisparoCallback(java.util.function.Consumer<ProyectilMiniJefe> callback) {
        this.onDisparoCallback = callback;
    }

    @Override
    protected void actualizarSprite(float delta) {
        spr.setPosition(posicion.x, posicion.y);
    }

    @Override
    protected void verificarMuerte(float delta) {
        // La muerte se verifica en PantallaJuego
        // Podría añadir efectos especiales al morir aquí
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        spr.draw(batch);
        
        // Dibujar proyectiles activos
        for (ProyectilMiniJefe proyectil : proyectiles) {
            proyectil.dibujar(batch);
        }
    }
    
    @Override
    public void escalarPorRonda(int ronda) {
        // Sistema de escalado salud máxima
        float factor = 1.0f + (ronda - 1) * 0.3f;
        
        // Limitar salud máxima a 100
        int nuevaSalud = (int)(SALUD_BASE * factor);
        saludMaxima = Math.min(nuevaSalud, 100); // Máximo 100 de salud
        salud = saludMaxima; // Llenar la salud al máximo
        danio = (int)(DANIO_BASE * factor);
        velocidadBase = 45f * factor;
        
        // Los proyectiles también escalan con la ronda
        danioProyectil = (int)(danioProyectil * factor);
    }
}