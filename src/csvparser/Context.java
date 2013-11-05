package csvparser;

import java.util.LinkedList;
import java.util.List;

public class Context {
  private final List<LinkedList<String>> mData = new LinkedList<LinkedList<String>>();
  private String mTokenBuffer = "";
  private String mSpaceTrailBuffer = "";
  private LinkedList<String> mRowBuffer = new LinkedList<String>();
  public List<? extends List<String>> getData() {
    return mData;
  }
  public void pushTokenLetter(final String letter) {
    mTokenBuffer += letter;
  }
  public void pushToken() {
    mRowBuffer.add(mTokenBuffer);
    mTokenBuffer = "";
  }
  public void pushSpace(final String space) {
    mSpaceTrailBuffer += space;
  }
  public void pushSpaceTrail() {
    mTokenBuffer += mSpaceTrailBuffer;
    clearSpaceTrail();
  }
  public void clearSpaceTrail() {
    mSpaceTrailBuffer = "";
  }
  public void pushRow() {
    mData.add(mRowBuffer);
    mRowBuffer = new LinkedList<String>();
  }
  public String getTokenBuffer() {
    return mTokenBuffer;
  }
  public List<String> getRowBuffer() {
    return mRowBuffer;
  }
}
