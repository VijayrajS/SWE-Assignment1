/*******************************************************************************
* Args.java                                                                    *
* Author : Robert C. Martin (https://github.com/unclebob)                      *
* Code reviewed by: Vijayraj Shanmugaraj                                       *
*                                                                              *
* Advised Java version: 11 onwards                                             *
*******************************************************************************/

package com.cleancoder.args;

import java.util.*;

import static com.cleancoder.args.ArgsException.ErrorCode.*;

public class Args {
  private final String schema;
  private final List<String> argsList;

  private final Map<Character, ArgumentMarshaler> marshalers;
  private final Set<Character> argsFound;
  private ListIterator<String> currentArgument;

  public Args(final String argSchema, final String[] args) throws ArgsException {
    schema = argSchema;
    argsList = Arrays.asList(args);
    
    marshalers = new HashMap<Character, ArgumentMarshaler>();
    argsFound = new HashSet<Character>();
    
    parseSchema();
    parseArgsList();
  }

  // Function called to extract each schema element and call a function to parse the same
  private void parseSchema() throws ArgsException {
    for (final String element : schema.split(",")) {
      if (isNotEmpty(element))
        parseSchemaElement(element.trim());
    }
  }
  
  private boolean isNotEmpty(final String element) {
    return element.length() != 0;
  }

  // A function to call a function in order to validate each Schema element, and insert a marshaler for the same
  private void parseSchemaElement(final String element) throws ArgsException {
    final char elementId = element.charAt(0);
    validateSchemaElementId(elementId);
    insertInMarshaler(element);
  }
  
  private void validateSchemaElementId(final char elementId) throws ArgsException {
    if (isNotLetter(elementId))
      throw new ArgsException(INVALID_ARGUMENT_NAME, elementId, null);
  }

  private void insertInMarshaler(final String element) throws ArgsException {

    final char elementId = element.charAt(0);
    final String symbol = element.substring(1);

    /*
     In the case of adding a new Marshaler type, just add an entry of the form
     put("Symbol", marshalerType()); in the hashtable, and a corresponding get function 
     at the end of the class
     */
    
    final Map<String, ArgumentMarshaler> marshalerCorrespondingTo = new HashMap<String, ArgumentMarshaler>(){{
      put("*", new StringArgumentMarshaler());
      put("#", new IntegerArgumentMarshaler());
      put("##", new DoubleArgumentMarshaler());
      put("[*]", new StringArrayArgumentMarshaler());
      put("&", new MapArgumentMarshaler());
    }};;

    if (isEmpty(symbol)){
      // logical flags need no arguments
      marshalers.put(elementId, new BooleanArgumentMarshaler());
    }
    else if(marshalerCorrespondingTo.containsKey(symbol)){
      marshalers.put(elementId, marshalerCorrespondingTo.get(symbol));
    }
    else{
      throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, symbol);
    }
  }

  private boolean isEmpty(final String element) {
    return element.length() == 0;
  }
  
  private boolean isNotLetter(final char element) {
    return !Character.isLetter(element);
  }


  private void parseArgsList() throws ArgsException {
    currentArgument = argsList.listIterator();

    while(currentArgument.hasNext()){
      final String argString = currentArgument.next();
      if (argString.startsWith("-")) {
        parseArgumentCharacters(argString.substring(1));
      } 
      else {
        currentArgument.previous();
        break;
      }
    }
  }

  /*
   Function to take care of calling the function to set individual marshalers within a single 
   flag (ex. -xy)
  */

  private void parseArgumentCharacters(final String argChars) throws ArgsException {
    for (int i = 0; i < argChars.length(); i++)
      setMarshalerCorrespondingTo(argChars.charAt(i));
  }

  private void setMarshalerCorrespondingTo(final char charFlag) throws ArgsException {
    final ArgumentMarshaler marshalerReturned = marshalers.get(charFlag);
    if (marshalerReturned == null) {
      throw new ArgsException(UNEXPECTED_ARGUMENT, charFlag, null);
    }
    else {
      argsFound.add(charFlag);
      try {
        marshalerReturned.set(currentArgument);
      } 
      catch (final ArgsException e) {
        e.setErrorArgumentId(charFlag);
        throw e;
      }
    }
  }

  public boolean has(final char arg) {
    return argsFound.contains(arg);
  }

  public int nextArgument() {
    return currentArgument.nextIndex();
  }

  public boolean getBoolean(final char arg) {
    return BooleanArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public String getString(final char arg) {
    return StringArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public int getInt(final char arg) {
    return IntegerArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public double getDouble(final char arg) {
    return DoubleArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public String[] getStringArray(final char arg) {
    return StringArrayArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public Map<String, String> getMap(final char arg) {
    return MapArgumentMarshaler.getValue(marshalers.get(arg));
  }
}
