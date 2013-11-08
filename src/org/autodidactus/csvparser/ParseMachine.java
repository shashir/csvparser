package org.autodidactus.csvparser;

/**
 * A ParseMachine builds an in-memory representation of a CSV file
 * by ingesting one character at a time. This representation is stored in the context.
 *
 * A parse machine maintains the state and performs the transitions
 * of the DFA which accepts the CSV language. While performing the transitions,
 * it has the side effect of deserializing the CSV file.
 */
public class ParseMachine {
  /**
   * Encapsulates the in-memory representation of the CSV file being built.
   */
  private final Context mContext;

  /**
   * Current state of the ParseMachine.
   */
  private ParseState mState;

  /**
   * Line number.
   */
  private int mLine = 1;

  /**
   * Character position.
   */
  private int mPosition = 1;

  /**
   * Construct a ParseMachine using a Context.
   *
   * @param context contains the in-memory representation of the CSV file.
   */
  public ParseMachine(final Context context) {
    mContext = context;
    mState = ParseState.START;
  }

  /**
   * Parses one character of the CSV file at a time.
   * The character may trigger transitions or may be appended to the Context.
   *
   * @param character in CSV file.
   * @throws CSVParseException if character can not be correctly parsed.
   */
  public void parseCharacter(final String character) throws CSVParseException {
    switch(mState) {
      case START:
        start(character);
        break;
      case END:
        end(character);
        break;
      case UNQUOTED_TOKEN:
        unquotedToken(character);
        break;
      case QUOTED_TOKEN:
        quotedToken(character);
        break;
      case ESCAPED_QUOTE:
        escapedQuote(character);
        break;
      case QUOTED_TOKEN_SPACE_TRAIL:
        quotedTokenSpaceTrail(character);
        break;
      default:
        throw new RuntimeException("Unexpected state");
    }
  }

  /**
   * Transition from the ESCAPED_QUOTE state to other states, per the DFA.
   *
   * @param character in the CSV file.
   * @throws CSVParseException if character can not be correctly parsed.
   */
  private void escapedQuote(String character) throws CSVParseException {
    mPosition++;
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.QUOTED_TOKEN_SPACE_TRAIL;
    } else if (SpecialCharacter.LINE_FEED.getRepresentation().equals(character)
        || SpecialCharacter.CARRIAGE_RETURN.getRepresentation().equals(character)) {
      mContext.pushToken();
      mContext.pushRow();
      mState = ParseState.END;
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.START;
    } else if (SpecialCharacter.QUOTE.getRepresentation().equals(character)) {
      mContext.pushTokenLetter(SpecialCharacter.QUOTE.getRepresentation());
      mState = ParseState.QUOTED_TOKEN;
    } else {
      throw new CSVParseException(
          "Unexpected character after end quote.", mLine, mPosition, character);
    }
  }

  /**
   * Signal that parsing the CSV is complete and commit the partial work to the Context.
   */
  public void close() {
    mContext.pushToken();
    mContext.pushRow();
  }

  /**
   * Transition from the QUOTED_TOKEN_SPACE_TRAIL state to other states, per the DFA.
   *
   * @param character in the CSV file.
   */
  private void quotedTokenSpaceTrail(String character) {
    mPosition++;
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mState = ParseState.QUOTED_TOKEN_SPACE_TRAIL;
    } else if (SpecialCharacter.LINE_FEED.getRepresentation().equals(character)
        || SpecialCharacter.CARRIAGE_RETURN.getRepresentation().equals(character)) {
      mState = ParseState.END;
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mState = ParseState.START;
    } else {
      throw new IllegalArgumentException(
          "Unexpected letter after end quote: " + character);
    }
  }

  /**
   * Transition from the QUOTED_TOKEN state to other states, per the DFA.
   *
   * @param character in the CSV file.
   */
  private void quotedToken(String character) {
    mPosition++;
    if (SpecialCharacter.QUOTE.getRepresentation().equals(character)) {
      mState = ParseState.ESCAPED_QUOTE;
    } else {
      mContext.pushTokenLetter(character);
      mState = ParseState.QUOTED_TOKEN;
    }
  }

  /**
   * Transition from the UNQUOTED_TOKEN state to other states, per the DFA.
   *
   * @param character in the CSV file.
   * @throws CSVParseException if character can not be correctly parsed.
   */
  private void unquotedToken(String character) throws CSVParseException {
    mPosition++;
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mContext.pushSpace(character);
      mState = ParseState.UNQUOTED_TOKEN;
    } else if (SpecialCharacter.LINE_FEED.getRepresentation().equals(character)
        || SpecialCharacter.CARRIAGE_RETURN.getRepresentation().equals(character)) {
      mContext.pushToken();
      mContext.pushRow();
      mState = ParseState.END;
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.START;
    } else if (SpecialCharacter.QUOTE.getRepresentation().equals(character)) {
      throw new CSVParseException(
          "Unexpected quote in the middle of an unquoted token.", mLine, mPosition, character);
    } else {
      mContext.pushSpaceTrail();
      mContext.pushTokenLetter(character);
      mState = ParseState.UNQUOTED_TOKEN;
    }
  }

  /**
   * Transition from the END state to other states, per the DFA.
   *
   * @param character in the CSV file.
   */
  private void end(String character) {
    if (SpecialCharacter.LINE_FEED.getRepresentation().equals(character)
        || SpecialCharacter.CARRIAGE_RETURN.getRepresentation().equals(character)) {
      mState = ParseState.END;
    } else {
      mLine++;
      mPosition = 0;
      start(character);
    }
  }

  /**
   * Transition from the START state to other states, per the DFA.
   *
   * @param character in the CSV file.
   */
  private void start(String character) {
    mPosition++;
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mState = ParseState.START;
    } else if (SpecialCharacter.LINE_FEED.getRepresentation().equals(character)
        || SpecialCharacter.CARRIAGE_RETURN.getRepresentation().equals(character)) {
      mContext.pushToken();
      mContext.pushRow();
      mState = ParseState.END;
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.START;
    } else if (SpecialCharacter.QUOTE.getRepresentation().equals(character)) {
      mState = ParseState.QUOTED_TOKEN;
    } else {
      mContext.pushTokenLetter(character);
      mState = ParseState.UNQUOTED_TOKEN;
    }
  }

  /**
   * Gets the Context containing the currently built in-memory representation of the CSV file.
   *
   * @return context.
   */
  public Context getContext() {
    return mContext;
  }

  /**
   * Returns the current state.
   *
   * @return current state.
   */
  @Override
  public String toString() {
    return mState.name();
  }
}
