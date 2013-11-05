package csvparser;

public enum ParseState {
  START,
  END,
  UNQUOTED_TOKEN,
  QUOTED_TOKEN,
  QUOTED_TOKEN_SPACE_TRAIL,
  UNEXPECTED_CHARACTER;
}
