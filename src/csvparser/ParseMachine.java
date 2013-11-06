package csvparser;

public class ParseMachine {
  private final Context mContext;
  private ParseState mState;
  public ParseMachine(final Context context) {
    mContext = context;
    mState = ParseState.START;
  }
  public void parseCharacter(final String character) {
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
        throw new IllegalArgumentException("Unexpected state");
    }
  }
  private void escapedQuote(String character) {
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.QUOTED_TOKEN_SPACE_TRAIL;
    } else if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
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
      throw new IllegalArgumentException(
          "Unexpected letter after end quote: " + character);
    }
  }
  public void close() {
    mContext.pushToken();
    mContext.pushRow();
  }
  private void quotedTokenSpaceTrail(String character) {
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mState = ParseState.QUOTED_TOKEN_SPACE_TRAIL;
    } else if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
      mState = ParseState.END;
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mState = ParseState.START;
    } else {
      throw new IllegalArgumentException(
          "Unexpected letter after end quote: " + character);
    }
  }
  private void quotedToken(String character) {
    if (SpecialCharacter.QUOTE.getRepresentation().equals(character)) {
      mState = ParseState.ESCAPED_QUOTE;
    } else {
      mContext.pushTokenLetter(character);
      mState = ParseState.QUOTED_TOKEN;
    }
  }
  private void unquotedToken(String character) {
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mContext.pushSpace(character);
      mState = ParseState.UNQUOTED_TOKEN;
    } else if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
      mContext.pushToken();
      mContext.pushRow();
      mState = ParseState.END;
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.START;
    } else if (SpecialCharacter.QUOTE.getRepresentation().equals(character)) {
      throw new IllegalArgumentException("Unexpected quote in the middle of an unquoted token.");
    } else {
      mContext.pushSpaceTrail();
      mContext.pushTokenLetter(character);
      mState = ParseState.UNQUOTED_TOKEN;
    }
  }
  private void end(String character) {
    if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
      mState = ParseState.END;
    } else {
      start(character);
    }
  }
  private void start(String character) {
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mState = ParseState.START;
    } else if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
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
  public Context getContext() {
    return mContext;
  }
  public String toString() {
    return mState.name();
  }
}
