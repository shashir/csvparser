package org.autodidactus.csvparser;

import java.util.LinkedList;
import java.util.List;

/**
 * A Context represents a partially parsed CSV file in memory.
 * It can build tokens by pushing characters.
 * It can build rows by pushing tokens.
 * It can build a list of rows by pushing rows.
 */
public class Context {
  /**
   * Represents the CSV file in memory.
   */
  private final List<LinkedList<String>> mData = new LinkedList<LinkedList<String>>();

  /**
   * Latest token being built.
   */
  private String mTokenBuffer = "";

  /**
   * Keep track of trailing spaces and tabs until they can be discarded
   * or appended to the token as necessary.
   */
  private String mSpaceTrailBuffer = "";

  /**
   * Latest row being built.
   */
  private LinkedList<String> mRowBuffer = new LinkedList<String>();

  /**
   * Gets the list of rows of tokens representing the CSV file.
   *
   * @return the list of rows of tokens representing the CSV file.
   */
  public List<? extends List<String>> getData() {
    return mData;
  }

  /**
   * Append the a letter to the latest token being built.
   *
   * @param letter to append.
   */
  public void pushTokenLetter(final String letter) {
    mTokenBuffer += letter;
  }

  /**
   * Add token being built to the latest row being built. Clear token buffer for next token.
   */
  public void pushToken() {
    mRowBuffer.add(mTokenBuffer);
    mTokenBuffer = "";
  }

  /**
   * Add space or tab to the trailing space buffer.
   * These space may or may not be part of the token.
   *
   * @param space or tab to push to the trailing space buffer.
   */
  public void pushSpace(final String space) {
    mSpaceTrailBuffer += space;
  }

  /**
   * The trailing space buffer ought to be pushed to the token.
   */
  public void pushSpaceTrail() {
    mTokenBuffer += mSpaceTrailBuffer;
    clearSpaceTrail();
  }

  /**
   * Discard the trailing space buffer by clearing it.
   */
  public void clearSpaceTrail() {
    mSpaceTrailBuffer = "";
  }

  /**
   * Add row the list of rows representing the CSV file.
   */
  public void pushRow() {
    mData.add(mRowBuffer);
    mRowBuffer = new LinkedList<String>();
  }

  /**
   * Gets the token being built.
   *
   * @return token being built.
   */
  public String getTokenBuffer() {
    return mTokenBuffer;
  }

  /**
   * Gets the row being built.
   * @return row being built.
   */
  public List<String> getRowBuffer() {
    return mRowBuffer;
  }
}
