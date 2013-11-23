package org.autodidactus.csvparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Util class containing a hodgepodge of parsing functions such as read from file.
 */
public final class CSVUtils {
  /**
   * Private constructor for util class.
   */
  private CSVUtils() {
  }

  /**
   * Reads CSV file and returns data.
   *
   * @param path to file
   * @return CSV data as list of list of string tokens
   * @throws IOException if file read fails
   */
  public List<? extends List<String>> readCSVFile(final String path) throws IOException {
    final BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    String file = "";
    try {
      String line = bufferedReader.readLine();
      while (null != line) {
        file += line + "\n";
      }
    } finally {
      bufferedReader.close();
    }
    final ParseMachine machine = new ParseMachine(new Context());
    for (int i = 0; i < file.length(); i++) {
      machine.parseCharacter(file.substring(i, i + 1));
    }
    return machine.getContext().getData();
  }
}
