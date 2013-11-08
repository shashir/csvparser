package org.autodidactus.csvparser;

import java.io.IOException;

/**
 * Encapsulates all CSV parsing exceptions.
 */
public class CSVParseException extends IOException {

  /**
   * Unnecessary.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Construct exception with line, position, offending character, and message.
   *
   * @param message to explain exception.
   * @param offendingLine where character can not be correctly parsed.
   * @param offendingPosition of the character which can not be correctly parsed.
   * @param offendingCharacter which can not be correctly parsed.
   */
  public CSVParseException(final String message,
      final int offendingLine,
      final int offendingPosition,
      final String offendingCharacter) {
    super(String.format("On line %d, position %d, encountered %s. %s",
        offendingLine,
        offendingPosition,
        offendingCharacter,
        message));
  }

  /**
   * Construct exception with message.
   *
   * @param message to explain exception.
   */
  public CSVParseException(final String message) {
    super(message);
  }
}
