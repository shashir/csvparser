package csvparser;

import java.io.InputStream;

public class ParseMachine {
  private final Context mContext;
  private ParseState mState;
  public ParseMachine(final Context context) {
    mContext = context;
  }
  public ParseState parseCharacter(final String character, final ParseState state) {
    switch(state) {
      case START:
        return start(character);
      case END:
        return end(character);
      case UNQUOTED_TOKEN:
        return unquotedToken(character);
      case UNQUOTED_TOKEN_SPACE_TRAIL:
        return unquotedTokenSpaceTrail(character);
      case QUOTED_TOKEN:
        return quotedToken(character);
      case QUOTED_TOKEN_SPACE_TRAIL:
        return quotedTokenSpaceTrail(character);
      default:
        throw new IllegalArgumentException("Unexpected state");
    }
  }
  private ParseState quotedTokenSpaceTrail(String character) {
    // TODO Auto-generated method stub
    return null;
  }
  private ParseState quotedToken(String character) {
    // TODO Auto-generated method stub
    return null;
  }
  private ParseState unquotedTokenSpaceTrail(String character) {
    // TODO Auto-generated method stub
    return null;
  }
  private ParseState unquotedToken(String character) {
    // TODO Auto-generated method stub
    return null;
  }
  private ParseState end(String character) {
    // TODO Auto-generated method stub
    return null;
  }
  private ParseState start(String character) {
    if (SpecialCharacter.SPACE.getRepresentation() == character
        || SpecialCharacter.TAB.getRepresentation() == character) {
      return ParseState.START;
    } else if (SpecialCharacter.EOL.getRepresentation() == character) {
      return ParseState.START;
    }
  }

}
