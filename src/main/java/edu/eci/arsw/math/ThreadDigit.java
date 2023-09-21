package edu.eci.arsw.math;

public class ThreadDigit extends Thread{

    private int start;
    private int count;
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private byte[] digits;


    public ThreadDigit(int start, int count){
        super();
        this.start = start;
        this.count = count;
    }

    public void run(){
        byte[] digitsRun = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            System.out.println(sum);
            digitsRun[i] = (byte) sum;
        }

        saveDigits(digitsRun);
    }

    public void saveDigits(byte[] digits){
        this.digits = digits;
    }

    public byte[] getDigits(){
        return digits;
    }

    public void stateContinue(){

    }

    public void statePause(){

    }

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
