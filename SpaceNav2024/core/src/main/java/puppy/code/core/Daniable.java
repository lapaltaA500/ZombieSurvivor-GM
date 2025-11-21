package puppy.code.core;

public interface Daniable {
    void recibirDanio(int danio);
    boolean estaVivo();
    int getSalud();
    void setInvulnerable(boolean invulnerable);
    boolean estaInvulnerable();
}