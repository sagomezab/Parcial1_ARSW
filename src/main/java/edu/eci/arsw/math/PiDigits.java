package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PiDigits {

    public static Object lock = new Object(); //1. Creación del objeto para identificación de los hilos.
    
    public static byte[] getDigits(int start, int count, int N) {
        int total = count / N; //2. Realizamos el cálculo de los n segmentos en los que se dividara el intervalo.
        ArrayList<ThreadDigit> threadsOnRoad = new ArrayList<>(); //3. Se crea una ArrayList de la clase de hilos.
        byte[] result = new byte[count];
        List<Byte> digitOnThread = new ArrayList<Byte>();//4. Creamos una lista de bytes donde se alojara lo recolectado por los hilos.
        boolean executing = true;
        for (int i = 0; i < N; i++) { // 5. Realizamos un ciclo de la n cantidad de segmentos  
            int startsSegment = start + (total*i); //6. Calculamos el valor de inicio del segmento que sera el mismo valor de incio más el cálculo de los n segmentos multiplicado por i que sera las veces que se tendra que aumentar este valor.
            if (i == N - 1) { //7. Realizamos una verifiación de i es igual a n menos 1 con el fin de tener en cuenta el valor final del segmento.
                total = count - (total * i); //8. Se asigna el valor final del segmento.
            } else {
                total = total;
            }
            ThreadDigit thread = new ThreadDigit(startsSegment, total, lock);//9. Se crea el hilo y le mandamos como parametro el valor de los segmentos calculados y el objeto para la indetifiación del hilo.
            threadsOnRoad.add(thread); //10. Se agrega el hilo al la lista de hilos creada anteriormente.
            thread.start();

            while (executing){

                try {
                    Thread.sleep(5000);//11. Se duerme durante 5 segundos el hilo de Pidigits.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Scanner scanner = new Scanner(System.in);
                System.out.println("Press enter to activate the threads...");//12. Se realiza la petición del enter para continuar con la ejecución de los hilos.
                String enter = scanner.nextLine();

                if (enter.trim().isEmpty()) {
                    System.out.println("Resuming threads.");
                    thread.stateContinue(); //13. El hilo que esta en ejecucuón envia activa la bandera para que continue con la ejecución.
                    synchronized(lock){ //14. Creamos el bloque sincronizado.
                        lock.notifyAll(); //15. Notificamos a todos los hilos que continuen con su ejecución.
                    }
                }

                if (Thread.activeCount() <= N){ //16. Se hace una verificación para conocer que los hilos terminaron su ejecución y pueda terminar el ciclo.
                    executing = false;
                }
		    }
        }

        for (ThreadDigit thread : threadsOnRoad) { //17. Se crea un bucle que recorre la lista de hilos.
            try {
                thread.join();//18. Hacemos uso de join() para esperar cualquier hilo que este en ejecución.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (ThreadDigit thread : threadsOnRoad) { //19. Se hace nuevamente un bucle que recorre la lista de hilos.
            ArrayList<Byte> byteList = new ArrayList<Byte>(); //20. Creamos una lista de bytes.
            for (byte b: thread.getDigits()) { //21.Recorremos los bytes calculados por los hilos.
                byteList.add(b);//22.Agremos los bytes a lista que creamos anteriormente.
            }
            for (byte b : byteList) {//23.Recorremos la lista de bytes creada.
                digitOnThread.add(b);//24.Agregamos los bytes a la lista que se creo al inicio con esta aseguramos el orden de los digitos calculados 
            }
        }

        for (int i = 0; i < digitOnThread.size(); i++) {//25.Recorremos la lista de bytes creada al inicio.
        result[i] = digitOnThread.get(i);//26. Agregamos los bytes alojados dentro de esta lista digitOnRoas a la lista result que se envia para que se realice la conversión a hexadecimal.
    }
        return result;
        
    }
}

