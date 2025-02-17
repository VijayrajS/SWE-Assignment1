package com.cleancoder.args;

import java.util.Iterator;

public class BooleanArgumentMarshaler implements ArgumentMarshaler {
  private boolean booleanValue = false;

  public void set(Iterator<String> currentArgument) throws ArgsException {
    booleanValue = true;
  }

  public static boolean getValue(ArgumentMarshaler currentMarshaler) {
    if (currentMarshaler != null && currentMarshaler instanceof BooleanArgumentMarshaler)
      return ((BooleanArgumentMarshaler) currentMarshaler).booleanValue;
    else
      return false;
  }
}
