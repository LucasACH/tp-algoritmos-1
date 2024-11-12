package performance;

/**
 * Clase utilitaria para medir el tiempo de ejecución de bloques de código en
 * milisegundos o segundos.
 * Proporciona métodos para medir código en métodos específicos o bloques
 * enteros.
 */
public class PerformanceTimer {

    private long startTime;
    private long endTime;

    /**
     * Inicia el temporizador. Registra el tiempo actual en nanosegundos.
     * Este método debe llamarse antes del bloque de código a medir.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Detiene el temporizador. Registra el tiempo actual en nanosegundos.
     * Este método debe llamarse después del bloque de código a medir.
     */
    public void stop() {
        endTime = System.nanoTime();
    }

    /**
     * Devuelve el tiempo transcurrido en milisegundos entre las últimas llamadas a
     * start y stop.
     *
     * @return Tiempo transcurrido en milisegundos como un valor long.
     */
    public long getElapsedTimeMillis() {
        return (endTime - startTime) / 1_000_000;
    }

    /**
     * Devuelve el tiempo transcurrido en segundos entre las últimas llamadas a
     * start y stop.
     *
     * @return Tiempo transcurrido en segundos como un valor long.
     */
    public long getElapsedTimeSeconds() {
        return (endTime - startTime) / 1_000_000_000;
    }

    /**
     * Mide el tiempo de ejecución de una operación específica pasada como un
     * Runnable.
     * Inicia, ejecuta y detiene el temporizador alrededor del Runnable
     * proporcionado.
     *
     * @param operation El código a medir, pasado como una expresión lambda o
     *                  referencia de método.
     * @return Tiempo de ejecución en milisegundos como un valor long.
     */
    public static long measure(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }
}
