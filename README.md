# JavaArgs 

##### Vijayraj Shanmugaraj [20171026]

## Instructions:
- Java 11 onwards recommended

### For Running code
      * install ant by running 'sudo apt-get install ant'
      * run 'ant compile'
      * run 'ant jar'
      * run 'java -cp build/jar/args.jar com.cleancoder.args.ArgsMain'
### For the tests
        * Run the command given below from the root folder of this repo
        * 'java -cp "lib/junit-4.13.jar:lib/hamcrest-core-1.3.jar:build/jar/args.jar" ./test/com/cleancoder/args/ArgsTest.java testCreateWithNoSchemaOrArguments'
    

## Code Cleanup
- Followed the rule "Public functions should follow the list of variables. We like to put the private utilities called by a public function right after the public function itself. This follows the stepdown rule." while adding functions to code.
- Declared all variables in the entire scope of the class *Args()* so that these attributes can be used in any method of the class without having to pass the variables as arguments to the methods. reducing the number of arguments that are passed in the functions *parseSchema()* and *parseArgumentList()* from one to zero
- Functions like isNotEmpty, isNotLetter and so on added for easier readability
- *parseArgumentStrings()* name changed to *parseArgsList()* for better understanding of what the function actually does.
- changed the for loop in *parseArgsList()* to a while loop, since the running loop is based on a condition not being satisfied, and makes sense during reading the code
- *parseSchemaElement()* divided into two different functions to strictly follow the rule that each function must do only one job. So, *parseSchemaElement()* only parses the schema element into the variable name and value. Validation and setting the appropriate type of marsheler to the appropriate key is done by *validateSchemaElementId()* and *insertElementInMarshaler()* respectively.
- Name of *parseArgumentCharacter()* changed to *setMarshalerCorrespondingTo()* because this function obtains the *marshelerElement* from the *marshelers* HashMap and sets its value according to the value specified. Name of variable *m* in the same function was changed to *marshalerReturned* for better understanding and readability.
- *setArgumentCharacterValue()* divided into two functions. The original function retrieves the marsheler element according to the argument specified along with error checking and *setMarshelerElement()* sets the value of the element to the value specified in the argument list.
- Indentation done as per normal standards which satisfies Visual Property. 
- Renamed variables for readabilitymarshalerReturned
- Exceptions added for all functions and methods
- All function/method names follow Verb/Noun naming convention
- Separated the insertion of an element into the map of a MapArgumentMarshaler into a separate function since the *.set()* function was too long.
- Added comments in necessary places to explain the Args.java file

## Tests
- Corrected *malFormedMapArgument()*'s Test
- Added a test for negative numbers
- Added a test discovering a multiple flag function i.e. -xy 2 2.3


## Out of the Box 
- New HashMap *marshalerCorrespondingTo* which maps each schema symbol to the corresponding marsheler, which makes the code **scalable**. If and when a new schema symbol and data type is added, rather than having to search all occurrences where the corresponding data type has to be instantiated in the code, the only change has to be made to *marshalerCorrespondingTo* to add the new symbol and data type to HashMap, and its appropriate Object. This does create instances which may not be used, but improves readability, since the size of the objects that are empty are very small, and do not take much time to create, hence not affecting the performance of the code, and improving readability & scalability. 
