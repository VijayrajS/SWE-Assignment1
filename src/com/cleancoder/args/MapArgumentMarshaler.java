package com.cleancoder.args;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.cleancoder.args.ArgsException.ErrorCode.*;

public class MapArgumentMarshaler implements ArgumentMarshaler {
  private Map<String, String> map = new HashMap<>();

  public void set(Iterator<String> currentArgument) throws ArgsException {
    try {
      String[] mapEntries = currentArgument.next().split(",");

      for (String entry : mapEntries)
        insertIntoMap(entry);
    }
    catch (NoSuchElementException e) {
      throw new ArgsException(MISSING_MAP);
    }
  }
  
  public void insertIntoMap(final String entry)throws ArgsException{
    String[] entrySplitted = entry.split(":");
    
    if (entrySplitted.length != 2)
      throw new ArgsException(MALFORMED_MAP);
    
    String key = entrySplitted[0];
    String value = entrySplitted[1];
    
    map.put(key, value);
  }

  public static Map<String, String> getValue(ArgumentMarshaler currentMarshaler) {
    if (currentMarshaler != null && currentMarshaler instanceof MapArgumentMarshaler)
      return ((MapArgumentMarshaler) currentMarshaler).map;
    else
      return new HashMap<>();
  }
}
