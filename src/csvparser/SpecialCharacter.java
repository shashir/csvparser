package csvparser;

public enum SpecialCharacter {
  SPACE(" "),
  TAB("\t"),
  EOL(System.getProperty("line.separator")),
  QUOTE("\""),
  COMMA(",");
  private final String mRepresentation;
  private SpecialCharacter(final String representation) {
    mRepresentation = representation;
  }
  public String getRepresentation() {
    return mRepresentation;
  }
}
