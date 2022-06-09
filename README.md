### RebuildPRPCStatements

![image](https://user-images.githubusercontent.com/38896730/172811241-4ee1e6a7-9d85-43e7-bbbe-4102e10e283e.png)

Use this program to rebuild PRPC statements into SQL queries using the output from the PEGA Tracer.

You can do that in several ways:

- Using a text file with your PEGA Tracer output by simply providing the file name, like:
		java -jar RebuildPRPCStatements.jar file.txt

- Providing the PEGA Tracer output from command line interface

- More to come!

Known issues:
- Only when using the Windows Command Line Interface, limit for each input string is 8192 characters. On this OS, we recommend to use Git Bash.

(c) matteo-lombardi, meryan83
