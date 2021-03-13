package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.enumeration.MIMETYPE;

public class EnumUtil {
  /**
   * Generates a random mimetype.
   *
   * @return the mimetype
   */
  public static MIMETYPE generateRandomMimeType() {
    return MIMETYPE.values()[NumbersUtil.generateRandomInteger(0, MIMETYPE.values().length)];
  }
}
