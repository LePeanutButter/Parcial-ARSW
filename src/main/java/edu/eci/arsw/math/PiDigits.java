package edu.eci.arsw.math;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @param N Number of threads
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int N) throws InterruptedException {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval for start.");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval for count.");
        }

        if (N < 0) {
            throw new RuntimeException("Invalid Interval for N.");
        }

        byte[] digits = new byte[count];

        int difference = count - start;
        int range = difference / N;
        int remainder = difference % N;
        boolean isDivisible = remainder == 0;

        PiDigitCalculator[] threads = new PiDigitCalculator[N];
        int endIndex = start - 1;

        for (int i = 0; i < N; i++) {
            int startIndex = endIndex + 1;
            endIndex = i == N - 1 ? startIndex + range : startIndex + range - 1;
            threads[i] = new PiDigitCalculator(startIndex, endIndex, digits);
        }

        if (!isDivisible) {
            int startIndex = endIndex;
            endIndex = startIndex + remainder;
            threads[range] = new PiDigitCalculator(startIndex, endIndex, digits);
        }

        for (PiDigitCalculator t: threads) {
            t.start();
        }

        for (PiDigitCalculator t: threads) {
            t.join();
        }

        return digits;
    }
}
