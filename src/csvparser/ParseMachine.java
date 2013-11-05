package csvparser;

import java.io.InputStream;

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
      case QUOTED_TOKEN_SPACE_TRAIL:
        quotedTokenSpaceTrail(character);
        break;
      default:
        throw new IllegalArgumentException("Unexpected state");
    }
  }
  public void close() {
    mContext.pushToken();
    mContext.pushRow();
  }
  private void quotedTokenSpaceTrail(String character) {

  }
  private void quotedToken(String character) {

  }
  private void unquotedToken(String character) {
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mContext.pushSpace(character);
      mState = ParseState.UNQUOTED_TOKEN;
    } else if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
      mContext.pushToken();
      mContext.pushRow();
      if (SpecialCharacter.EOL.getRepresentation().length() > 1) {
        mState = ParseState.END;
      } else {
        mState = ParseState.START;
      }
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.START;
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
      
    }
    mState = ParseState.START;
  }
  private void start(String character) {
    if (SpecialCharacter.SPACE.getRepresentation().equals(character)
        || SpecialCharacter.TAB.getRepresentation().equals(character)) {
      mState = ParseState.START;
    } else if (SpecialCharacter.EOL.getRepresentation().contains(character)) {
      mContext.pushToken();
      mContext.pushRow();
      if (SpecialCharacter.EOL.getRepresentation().length() > 1) {
        mState = ParseState.END;
      } else {
        mState = ParseState.START;
      }
    } else if (SpecialCharacter.COMMA.getRepresentation().equals(character)) {
      mContext.pushToken();
      mState = ParseState.START;
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

  public static void main(String[] args) {
    ParseMachine machine = new ParseMachine(new Context());
    // try " , , ,"
    String test = "cookies, pets,     \t why, so,\tserial,, t t,";
    System.out.println(machine
        + " row buffer: " + machine.getContext().getRowBuffer()
        + ", token buffer: " + machine.getContext().getTokenBuffer());
    for (int i = 0; i < test.length(); i++) {
      machine.parseCharacter("" + test.charAt(i));
      System.out.println(machine
          + " row buffer: " + machine.getContext().getRowBuffer() + " " + machine.getContext().getRowBuffer().size()
          + ", token buffer: " + machine.getContext().getTokenBuffer() + " " + machine.getContext().getTokenBuffer().length());
    }
    machine.close();
    System.out.println(machine
        + " row buffer: " + machine.getContext().getRowBuffer() + " " + machine.getContext().getRowBuffer().size()
        + ", token buffer: " + machine.getContext().getTokenBuffer() + " " + machine.getContext().getTokenBuffer().length());
    System.out.println(machine.getContext().getData());
  }
}
