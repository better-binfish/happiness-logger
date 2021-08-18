### Initialize.

```
// Importation
import xyz.binfish.logger.Logger;
import xyz.binfish.logger.LoggerConfig;

// Initialization
Logger.createLogger(
	new LoggerConfig("YOUR_LOG_DIRECTORY")); // You can give an argument to the class constructor
	                                         // to specify the directory for the log files.
```

This record will return a ready-made logger object. So you can immediately start interacting with it like this:
```
Logger logger = Logger.createLogger(...);
logger.log("Hello World!");
```

### Table all log levels.

+----------+---------------------------+
| .error() | for error messages.       |
| .warn()  | for any warnings.         |
| .info()  | for information messages. |
| .debug() | for debugging.            |
+----------+---------------------------+

	Note: The debug function supports collections. (only this function)


### Using.

For example, let's get an instance of our logger in another file and use it:

```
// ...

Logger logger = Logger.getLogger(); // If the logger has been initialized, its instance 
                                    // will be returned, otherwise an exception will be thrown.

logger.info("Important message! This function was called from another file!");

int[] myIntArray = new int[] { 4, 5, 6, 10, 23 };

if(args.length != 0 && args[0].equals("1")) {
	logger.debug(myIntArray); // 'debug' function can help debug your program
}

logger.info("An integer array has been created!");

logger.closeLogger(); // If you are not going to use the logger, then you can close it.
                      // You can create a new instance after this.

// ...
```

In order to use colors, you must enable the `useColor` flag in the logger configuration,
this can be done with the `LoggerConfig#setUseColors()` method of the LoggerConfig class.

An example of using colors.

```
// ...

logger.info("%greenAll modules loaded successfully!%reset"); // This message will be green
                                                             // `%reset` needed to reset the color otherwise 
                                                             // all other messages will be green.
// ...
```

Table of all available colors.
+---------+-----------------------+
| %reset  | used to reset colors. |
| %black  |                       |
| %red    |                       |
| %green  |                       |
| %yellow |                       |
| %blue   |                       |
| %purple |                       |
| %cyan   |                       |
| %white  |                       |
+---------+-----------------------+

### Configuration Options Reference.

```
String logDirectory - path to directory for storing logs

	Note: You do not need to specify the directory for storing logs!

String formatTimePattern - date and time pattern for formatter (Default: "dd-MM-yyyy HH:mm:ss")
boolean autoCleaning - flag responsible for automatic logs cleanup (Default: false)
boolean notifyDeleteFiles - flag responsible for notification about cleared files (Default: false)
boolean showThread - flag responsible for showing the thread during logging (Default: false)
boolean useColors - flag responsible for formatting colors in ANSI codes. (Default: false)
int minimumAgeInMinutes - this is the minimum age in minutes everything older will be deleted (Default: 0)

	Note: If "minimumAgeInMinutes" will equal 0, then after each new launch the entire file will be deleted!

```

Example of creating a custom config:

```
LoggerConfig lgConf = new LoggerConfig()
	.setAutoCleaning(true)
	.setNotifyDeleteFiles(true)
	.setMinimumAgeInMinutes(60 * 24 * 3) // equals 3 days
	.setUseColors(true)
	.setDirectory("my_log_files/")
	.setFormatTimePattern("yyyy/MM/dd HH:mm:ss");

Logger.createLogger(lgConf);
```
