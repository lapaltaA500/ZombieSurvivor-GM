package puppy.code;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Gestor que controla qué factory usar según la ronda
 */
public class GestorEscenarios {
    private Map<Integer, EscenarioFactory> factoriesPorRonda;
    private Random random;
    
    public GestorEscenarios() {
        factoriesPorRonda = new HashMap<>();
        random = new Random();
        inicializarFactories();
    }
    
    private void inicializarFactories() {
        // Asignar factories a rondas específicas
        factoriesPorRonda.put(1, new EscenarioBosqueFactory());
        factoriesPorRonda.put(2, new EscenarioBosqueFactory());
        factoriesPorRonda.put(3, new EscenarioCiudadFactory());
        factoriesPorRonda.put(4, new EscenarioCiudadFactory());
        // Podemos añadir más factories para rondas superiores
    }
    
    public EscenarioFactory getFactoryParaRonda(int ronda) {
        // Si no hay factory específica, usar una aleatoria
        EscenarioFactory factory = factoriesPorRonda.get(ronda);
        if (factory == null) {
            // Ronda no definida, usar aleatorio
            int tipo = random.nextInt(2);
            if (tipo == 0) {
                factory = new EscenarioBosqueFactory();
            } else {
                factory = new EscenarioCiudadFactory();
            }
        }
        return factory;
    }
}