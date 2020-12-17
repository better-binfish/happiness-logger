### Initialize

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

### List all log levels

```
.error() - for error messages
.warn()  - for any warnings
.info()  - for information messages
.debug() - for debugging
```

	Note: The debug function supports collections. (only this function)


### Using

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


### Configuration Options Reference

```
String logDirectory - path to directory for storing logs

	Note: You do not need to specify the directory for storing logs!

String formatTimePattern - date and time pattern for formatter (Default: "dd-MM-yyyy HH:mm:ss")

boolean autoCleaning - flag responsible for automatic logs cleanup (Default: false)
boolean notifyDeleteFiles - flag responsible for notification about cleared files (Default: false)
int minAgeInMinutes - this is the minimum age in minutes everything older will be deleted (Default: 0)

	Note: If "minAgeInMinutes" will equal 0, then after each new launch the entire file will be deleted!

```

Example of creating a custom config:

```
LoggerConfig lgConf = new LoggerConfig();

lgConf.autoCleaning = true;
lgConf.notifyDeleteFiles = true;
lgConf.cleaningMinuteInterval = 60 * 24 * 3; // equals 3 days

lgConf.logDirectory = "my_log_files/";
lgConf.formatTimePattern = "yyyy/MM/dd HH:mm:ss";

Logger.createLogger(lgConf);
```
