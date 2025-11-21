package puppy.code.jugador;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import puppy.code.ataques.Bullet;
import puppy.code.core.Daniable;
import puppy.code.core.EntidadJuego;
import puppy.code.enemigos.Ball2;
import puppy.code.juego.PantallaJuego;

public class Nave4 extends EntidadJuego implements Daniable {
	// === Estado base ===
	private boolean destruida = false;
	private int vidas = 5; 

	// === Movimiento mejorado ===
	private float xVel = 0;
	private float yVel = 0;

	private Sprite spr;
	private Sound sonidoHerido;
	private Sound soundBala;
	private Texture txBala;

	// === SISTEMA DE I-FRAMES SIMPLE ===
	private boolean invulnerable = false;
	private float tiempoInvulnerable = 0f;
	private static final float DURACION_INVULNERABILIDAD = 1.5f; // 1.5 segundos de invulnerabilidad

	// Velocidad base aumentada para mejor respuesta
	private final float VELOCIDAD_BASE = 5.0f; 
	
	// Rotación controlada por mouse
	private float anguloRotacion = 0f;

	// MOD: Constantes del mundo lógico
	private static final float WORLD_WIDTH = 1200f;
	private static final float WORLD_HEIGHT = 800f;
	
	private java.util.function.Consumer<Bullet> onDisparo;

	// Campos para ventajas temporales
	private boolean disparoAutomatico = false;
	private float tiempoDisparoAutomatico = 0;
	private boolean inmortalidadTemporal = false;
	private float tiempoInmortalidad = 0;

	// TemplateCompat: Constructor modificado para usar EntidadJuego
	public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
		super(x, y, 80, 80); // EntidadJuego: posición y tamaño base
		sonidoHerido = soundChoque;
		this.soundBala = soundBala;
		this.txBala = txBala;
		spr = new Sprite(tx);
		spr.setPosition(x, y);
		spr.setBounds(x, y, 80, 80);
		
		// Configurar centro de rotación para giro preciso
		spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);
	}

	// =========================
	// TemplateCompat: Métodos de EntidadJuego mejorados
	// =========================
	@Override
	public void actualizar(float delta) {
		if (destruida) return;

		float x = getPosicion().x;
		float y = getPosicion().y;

		// === ACTUALIZAR SISTEMA DE I-FRAMES ===
		if (invulnerable) {
			tiempoInvulnerable -= delta;
			if (tiempoInvulnerable <= 0) {
				invulnerable = false;
			}
		}

		// Actualizar ventajas temporales
		if (disparoAutomatico) {
			tiempoDisparoAutomatico -= delta;
			if (tiempoDisparoAutomatico <= 0) {
				disparoAutomatico = false;
			}
		}

		if (inmortalidadTemporal) {
			tiempoInmortalidad -= delta;
			if (tiempoInmortalidad <= 0) {
				inmortalidadTemporal = false;
				setInvulnerable(false); // Restaurar invulnerabilidad normal
			}
		}

		{
			// MOD: Movimiento continuo mejorado con aceleración suave
			float aceleracion = 0.2f;
			float friccion = 0.85f; // Reducida para menos "flotante"
			
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) 
				xVel = Math.max(xVel - aceleracion, -VELOCIDAD_BASE);
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) 
				xVel = Math.min(xVel + aceleracion, VELOCIDAD_BASE);
			else 
				xVel *= friccion;
			
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) 
				yVel = Math.max(yVel - aceleracion, -VELOCIDAD_BASE);
			else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) 
				yVel = Math.min(yVel + aceleracion, VELOCIDAD_BASE);
			else 
				yVel *= friccion;
			
			// MOD: Normalización mejorada para movimiento diagonal
			float magnitud = (float) Math.sqrt(xVel * xVel + yVel * yVel);
			if (magnitud > VELOCIDAD_BASE) {
				xVel = (xVel / magnitud) * VELOCIDAD_BASE;
				yVel = (yVel / magnitud) * VELOCIDAD_BASE;
			}

			// MOD: Usar límites del mundo lógico en lugar de la ventana
			if (x+xVel < 0 || x+xVel+spr.getWidth() > WORLD_WIDTH)
				xVel = 0;
			if (y+yVel < 0 || y+yVel+spr.getHeight() > WORLD_HEIGHT)
				yVel = 0;

			// Actualizar posición (EntidadJuego + sprite)
			x += xVel * delta * 60f; // conservar "sensación" original basada en VELOCIDAD_BASE por frame
			y += yVel * delta * 60f;
			setPosicion(x, y);
			spr.setPosition(x, y);
		}
	}
	
	public void setOnDisparo(java.util.function.Consumer<Bullet> callback) {
	    this.onDisparo = callback;
	}

	@Override
	public void dibujar(SpriteBatch batch) {
		if (destruida) return;

		// === EFECTO VISUAL DE I-FRAMES: PARPADEO SIMPLE ===
		// Efecto visual para inmortalidad temporal (parpadeo más rápido)
		if (inmortalidadTemporal) {
			if ((int)(tiempoInmortalidad * 20) % 2 == 0) {
				spr.draw(batch);
			}
		} else if (!invulnerable || (int)(tiempoInvulnerable * 10) % 2 == 0) {
			spr.draw(batch);
		}
	}

	// =========================
	// Método de compatibilidad con el proyecto original
	// =========================
	public void draw(SpriteBatch batch, PantallaJuego juego) {
	    actualizar(Gdx.graphics.getDeltaTime());
	    dibujar(batch);

	    // Disparo orientado por rotación - NUEVO CAMBIO: Modificado para ventaja de disparo automático
	    if (!destruida && !invulnerable) {
	        boolean debeDisparar = false;
	        
	        // Disparo normal o automático
	        if (disparoAutomatico) {
	            // En modo automático, dispara continuamente mientras se presiona
	            debeDisparar = Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(0);
	        } else {
	            // Disparo normal (solo al presionar)
	            debeDisparar = Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(0);
	        }
	        
	        if (debeDisparar) {          
	            // Calcular posición del muzzle (punta del arma) basada en rotación actual
	            float centroX = spr.getX() + spr.getWidth() / 2;
	            float centroY = spr.getY() + spr.getHeight() / 2;
	            float angulo = getAnguloRotacionRadians();
	            
	            // Offset del muzzle desde el centro del sprite
	            float offsetMuzzle = 35f;
	            float muzzleX = centroX + MathUtils.cos(angulo) * offsetMuzzle - 10;
	            float muzzleY = centroY + MathUtils.sin(angulo) * offsetMuzzle - 10;
	            
	            // Crear bala con dirección orientada
	            Bullet bala = new Bullet(muzzleX, muzzleY, angulo, txBala);
	            
	            // USAR CALLBACK en lugar de acceso directo a PantallaJuego
	            if (onDisparo != null) {
	                onDisparo.accept(bala);
	            } else {
	                // Fallback por compatibilidad
	                juego.agregarBala(bala);
	            }
	            
	            if (soundBala != null) soundBala.play();
	        }
	    }
	}

	public Sprite getSprite() {
		return spr;
	}

	// =========================
	// Rotación controlada por mouse
	// =========================
	
	/**
	 * Actualiza la rotación del jugador basándose en la posición del cursor
	 * @param mouseMundoX Coordenada X del mouse en espacio mundial
	 * @param mouseMundoY Coordenada Y del mouse en espacio mundial
	 */
	public void actualizarRotacionMouse(float mouseMundoX, float mouseMundoY) {
		float centroJugadorX = getXFloat() + spr.getWidth() / 2;
		float centroJugadorY = getYFloat() + spr.getHeight() / 2;
		
		float deltaX = mouseMundoX - centroJugadorX;
		float deltaY = mouseMundoY - centroJugadorY;
		
		// Calcular ángulo en radianes y convertir a grados para LibGDX
		anguloRotacion = MathUtils.radiansToDegrees * MathUtils.atan2(deltaY, deltaX);
		spr.setRotation(anguloRotacion);
	}
	
	/**
	 * @return Ángulo de rotación actual en grados (para compatibilidad con Sprite.rotate)
	 */
	public float getAnguloRotacion() {
		return anguloRotacion;
	}
	
	/**
	 * @return Ángulo de rotación actual en radianes (para cálculos matemáticos)
	 */
	public float getAnguloRotacionRadians() {
		return anguloRotacion * MathUtils.degreesToRadians;
	}

	// =========================
	// Colisión / daño
	// =========================
	public boolean checkCollision(Ball2 b) {
		// No recibir daño durante inmortalidad temporal
		if(!invulnerable && !inmortalidadTemporal && b.getArea().overlaps(spr.getBoundingRectangle())){
			// Comportamiento de colisión - solo daña al jugador, no rebote
			vidas--;
			activarInvulnerabilidad();
			if (sonidoHerido != null) sonidoHerido.play();
			if (vidas<=0) 
				destruida = true; 
			return true;
		}
		return false;
	}

	// =========================
	// Daniable
	// =========================
	@Override
	public void recibirDanio(int danio) {
		// No recibir daño durante inmortalidad temporal
		if (!invulnerable && !inmortalidadTemporal && !destruida) {
			vidas -= danio;
			activarInvulnerabilidad();
			if (sonidoHerido != null) sonidoHerido.play();
			if (vidas <= 0) {
				destruida = true;
			}
		}
	}
	
	/**
	 * Activa el período de invulnerabilidad temporal
	 */
	private void activarInvulnerabilidad() {
		invulnerable = true;
		tiempoInvulnerable = DURACION_INVULNERABILIDAD;
	}

	@Override
	public boolean estaVivo() {
		return vidas > 0 && !destruida;
	}

	@Override
	public int getSalud() {
		return vidas;
	}

	@Override
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
		if (invulnerable) {
			tiempoInvulnerable = DURACION_INVULNERABILIDAD;
		}
	}

	@Override
	public boolean estaInvulnerable() {
		return invulnerable;
	}

	// =========================
	// Getters/compatibilidad del proyecto original
	// =========================
	public boolean estaDestruido() {
		return !invulnerable && destruida;
	}
	public int getVidas() {return vidas;}
	public int getX() {return (int) spr.getX();}
	public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}

	// Métodos para que los enemigos puedan seguir al jugador
	public float getXFloat() { return spr.getX(); }
	public float getYFloat() { return spr.getY(); }
	
	public com.badlogic.gdx.math.Rectangle getArea(){
	    return spr.getBoundingRectangle();
	}

	// =========================
	// Métodos para activar ventajas
	// =========================
	public void activarDisparoAutomatico(float duracion) {
		this.disparoAutomatico = true;
		this.tiempoDisparoAutomatico = duracion;
	}

	public void activarInmortalidad(float duracion) {
		this.inmortalidadTemporal = true;
		this.tiempoInmortalidad = duracion;
		setInvulnerable(true); // Hacer invulnerable inmediatamente
	}

	public void agregarVidas(int cantidad) {
		this.vidas += cantidad;
	}

	// Getters para verificar estados de ventajas
	public boolean isDisparoAutomatico() {
		return disparoAutomatico;
	}

	public boolean isInmortalidadTemporal() {
		return inmortalidadTemporal;
	}

	public float getTiempoDisparoAutomatico() {
		return tiempoDisparoAutomatico;
	}

	public float getTiempoInmortalidad() {
		return tiempoInmortalidad;
	}
}