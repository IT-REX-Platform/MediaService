package de.uni_stuttgart.it_rex.media.util.written;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Collectors;

import static de.uni_stuttgart.it_rex.media.util.written.NumbersUtil.generateRandomInteger;

public final class StringUtil {
  private static final long seed = 7777777777L;
  private static final Random RANDOM = new Random(seed);

  /**
   * Generates a random String with a length between two numbers.
   *
   * @param lowerBound the upper bound
   * @param upperBound the lower bound
   * @return the String
   */
  public static String generateRandomString(final int lowerBound, final int upperBound) {
    byte[] array = new byte[generateRandomInteger(lowerBound, upperBound)];
    RANDOM.nextBytes(array);
    return new String(array, Charset.forName("UTF-8"));
  }

  public static String loadLongString() throws IOException {
    return Files.lines(Path.of("src/test/resources/CourseDescription.txt")).collect(Collectors.joining("\n"));
  }
}
