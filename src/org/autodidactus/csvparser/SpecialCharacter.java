package org.autodidactus.csvparser;

/**
 * Characters with special meaning in CSV file format.
 */
public enum SpecialCharacter {
  /**
   * Space.
   */
  SPACE(" "),

  /**
   * Tab. Treat the same as space.
   */
  TAB("\t"),

  /**
   * Line feed terminates UNIX lines.
   */
  LINE_FEED("\n"),

  /**
   * Carriage return terminate legacy OS lines.
   */
  CARRIAGE_RETURN("\r"),

  /**
   * Double quote is used for complex string tokens.
   */
  QUOTE("\""),

  /**
   * Comma separates tokens.
   */
  COMMA(",");

  /**
   * String representation of special character.
   */
  private final String mRepresentation;

  /**
   * Private enum constructor.
   *
   * @param representation character of the instance.
   */
  private SpecialCharacter(final String representation) {
    mRepresentation = representation;
  }

  /**
   * Returns the special character of the instance.
   *
   * @return special character string representation.
   */
  public String getRepresentation() {
    return mRepresentation;
  }
}
