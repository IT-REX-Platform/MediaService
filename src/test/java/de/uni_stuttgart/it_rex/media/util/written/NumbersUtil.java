package de.uni_stuttgart.it_rex.media.util.written;

import java.util.Random;

public final class NumbersUtil {
  private static final long seed = 42694201337L;
  private static final Random RANDOM = new Random(seed);

  /**
   * Generates a random integer between two numbers.
   *
   * @param lowerBound the upper bound
   * @param upperBound the lower bound
   * @return the integer
   */
  public static int generateRandomInteger(final int lowerBound, final int upperBound) {
    return RANDOM.nextInt(upperBound - lowerBound) + lowerBound;
  }

  /**
   * Generates a random long between two numbers.
   *
   * @param lowerBound the upper bound
   * @param upperBound the lower bound
   * @return the long
   */
  public static long generateRandomLong(final long lowerBound, final long upperBound) {
    return RANDOM.longs(lowerBound, (upperBound + 1)).limit(1).findFirst().getAsLong();
  }
}
