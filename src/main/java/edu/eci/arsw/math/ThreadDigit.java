package edu.eci.arsw.math;

public class ThreadDigit extends Thread{
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private int start;
    private int count;
    private byte[] digits;
    public Object lock;
    private Boolean execute = true;

    public ThreadDigit(int start, int count, Object lock) {
        this.start = start;
        this.count = count;
        this.digits = new byte[count]; //1. Se inicializa la lista de bytes del cálculo de digitos de PI.
        this.lock = lock;
    }

    public void run(){
        if (start < 0 || count < 0) { //2. Se condensa en una sola linea los condicionales para asegurar el flujo del proceso.
            throw new RuntimeException("Invalid Interval");
        }

        double sum = 0;
        long startTime = System.currentTimeMillis(); //3. Se pide por primera vez el tiempo al sistema.
        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                long end = System.currentTimeMillis(); //4. Se realiza nuevamente la petición del tiempo.
			    long elapsedTime = end - startTime; // 5. Se calcula el tiempo que ha transcurrido.
                if (elapsedTime >= 5000){ // 6. Se realiza un condicional que nos dice si el es mayor a 5 segundos entonces ingrese.
                    statePause(); //7. Activamos la bandera que nos indica cuando un hilo esta en pausa.
                    synchronized (lock){ // 8. Realizamos el bloque sincronizado para hacer uso de wait(), haciendo referencia a un objeto creado como identificador del hilo en ejecución
                        try{	
                            lock.wait();//9. Con el identificador realizamos la llamada a wait y pone en pausa el hilo.
                            startTime = System.currentTimeMillis();//10. Pedimos nuevamente el tiempo al sistema para recalcular
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                    }
                }
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
            System.out.println(Main.bytesToHex(digits));//11. Imprimimos los número calculados hasta el momento, los imprimimos haciendo el llamdo a la método dentro de MAIN que los convierta en hexadecimal.
        }

    }

    public void statePause(){//12. Método que activa la bandera en pausa.
		execute = false;
	}

	public void stateContinue(){//13. Método que activa la bandera en ejecución.
		execute = true;
	}

	public boolean isRunning(){//14. Método que nos muestra el estado de la bandera.
		return execute;
	}

    public byte[] getDigits() {//15. Método que guarda la digitos dentro de una lista y la retorna.
        return digits;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }
    
}
