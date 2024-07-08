package ovh.cosmicdan.simpledaylengthextender;

import net.minecraft.world.level.storage.LevelData;

public class TimeTocker {
    public final long phaseStartInTicks;
    public final long tockerInc;
    public final long tockerMax;

    long tocker = 0;

    /**
     *
     * Convert decimal to fraction stuff thanks to https://www.geeksforgeeks.org/convert-given-decimal-number-into-an-irreducible-fraction/
     */
    public TimeTocker(final double phaseMultiplier, final int phaseStartInTicks) {
        this.phaseStartInTicks = phaseStartInTicks;
        // Fetch integral value of the decimal
        double intVal = Math.floor(phaseMultiplier);
        // Fetch fractional part of the decimal
        double fVal = phaseMultiplier - intVal;
        // Consider precision value to convert fractional part to integral equivalent
        final long pVal = 1000000000;
        // Calculate GCD of integral equivalent of fractional part and precision value
        long gcdVal = gcd(Math.round(fVal * pVal), pVal);
        // Calculate numerator and denominator
        long numerator = pVal / gcdVal;
        long denominator = Math.round(fVal * pVal) / gcdVal;
        // Print the fraction
        //System.out.println((long)(intVal * numerator) + denominator + "/" + numerator);
        // Set Tocker values
        tockerInc = numerator;
        tockerMax = (long)(intVal * numerator) + denominator;
    }

    public boolean shouldAdvanceTime(LevelData levelData) {
        boolean shouldAdvance = false;
        if (tocker >= tockerMax) {
            shouldAdvance = true;
            tocker -= tockerMax;
        }
        tocker += tockerInc;
        return shouldAdvance;
    }

    /**
     * Thanks to https://www.geeksforgeeks.org/convert-given-decimal-number-into-an-irreducible-fraction/
     */
    private static long gcd(long a, long b)
    {
        if (a == 0)
            return b;
        else if (b == 0)
            return a;
        if (a < b)
            return gcd(a, b % a);
        else
            return gcd(b, a % b);
    }

    public void tock() {

    }

}
