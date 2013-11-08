package org.autodidactus.csvparser;

/**
 * States of the ParseMachine which accepts the CSV file language.
 */
public enum ParseState {
  /**
   * CSV parsing begins here.
   */
  START,

  /**
   * If EOL is encountered, arrive here. After first line, all lines begin with this state.
   */
  END,

  /**
   * While parsing the middle of an unquoted token.
   */
  UNQUOTED_TOKEN,

  /**
   * While parsing the middle of a quoted token.
   */
  QUOTED_TOKEN,

  /**
   * Double quotes are used to escape themselves.
   * Is the encountered quote an escape quote or a token end quote?
   */
  ESCAPED_QUOTE,

  /**
   * Trailing spaces after a quoted token has terminated.
   */
  QUOTED_TOKEN_SPACE_TRAIL
}
