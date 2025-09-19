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
        int range = N / difference;
        int remainder = N % difference;
        boolean isDivisible = remainder == 0;
        int size = isDivisible ? range : range + 1;

        PiDigitCalculator[] threads = new PiDigitCalculator[size];

        for (int i = 0; i < range; i++) {
            int startIndex = start * (i + 1);
            int endIndex = startIndex + difference - 1;
            System.out.println("StartIndex: " + startIndex);
            System.out.println("EndIndex: " + endIndex);
            threads[i] = new PiDigitCalculator(startIndex, endIndex, digits);
        }

        if (!isDivisible) {
            int startIndex = start * (range + 1);
            int endIndex = startIndex + remainder;
            System.out.println("StartIndex: " + startIndex);
            System.out.println("EndIndex: " + endIndex);
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
