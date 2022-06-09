# RebuildPRPCStatements

![image](https://user-images.githubusercontent.com/38896730/172811241-4ee1e6a7-9d85-43e7-bbbe-4102e10e283e.png)

Use this program to rebuild PRPC statements into SQL queries using the output from the PEGA Tracer.

### Install
Simply download the jar file and run it!
There is also a zip file that includes a .bat script. You can run it to invoke the jar file. However, on Windows this will open the Command Line Interface (see Known Issues)

### Run

You can run the rebuilder in several ways:

- Copying - pasting the PEGA Tracer output into your command line, invoking the jar file without params:
	java -jar RebuildPRPCStatements.jar

- Using a text file with your PEGA Tracer output by invoking the jar file providing the file name, like:
	java -jar RebuildPRPCStatements.jar query.txt
	
	Use our query.txt as an example.

- More to come!

### Known Issues

- Only when using the Windows Command Line Interface, limit for each input string is 8192 characters. On this OS, we recommend to use Git Bash.
- When providing a file, avoid multiple returns.

### (c) matteo-lombardi, meryan83
