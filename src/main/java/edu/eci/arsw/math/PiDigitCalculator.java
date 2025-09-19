package edu.eci.arsw.math;

public class PiDigitCalculator extends Thread {
    private int startIndex;
    private final int endIndex;
    private final byte[] digits;
    private static final int DigitsPerSum = 8;
    private static final double Epsilon = 1e-17;

    public PiDigitCalculator(int startIndex, int endIndex, byte[] digits) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.digits = digits;
    }

    private void setDigits() {
        double sum = 0;

        for (int i = startIndex; i < endIndex; i++) {


            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, startIndex)
                        - 2 * sum(4, startIndex)
                        - sum(5, startIndex)
                        - sum(6, startIndex);

                startIndex += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }
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

    @Override
    public void run() {
        setDigits();
    }
}
