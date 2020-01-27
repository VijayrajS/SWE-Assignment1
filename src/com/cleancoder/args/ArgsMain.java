package com.cleancoder.args;

public class ArgsMain {
  public static void main(String[] args) {
    try {
      Args arg = new Args("l,p#,d*,b#", args);
      boolean logging = arg.getBoolean('l');
      
      int port = arg.getInt('p');
      int b = arg.getInt('b');
      String directory = arg.getString('d');
      executeApplication(logging, port, directory, b);
    } catch (ArgsException e) {
      System.out.printf("Argument error: %s\n", e.errorMessage());
    }
  }

  private static void executeApplication(boolean logging, int port, String directory,int bee) {
    System.out.printf("logging is %s, port:%d, directory:%s %d\n",logging, port, directory, bee);
  }
}
