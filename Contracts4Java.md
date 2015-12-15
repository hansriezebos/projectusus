## Enabling Contracts ##

Step 1: Create a folder somewhere on your harddisk (let's call it $FOLDER from now on) and add the following two JAR-Files to it:

  * https://github.com/usus/usus-plugins/raw/master/org.projectusus.core/lib/c4j.jar
  * https://github.com/usus/usus-plugins/raw/master/org.projectusus.core/lib/javassist.jar

Step 2: Edit eclipse.ini and add the following arguments at the end (after `-vmargs`):

```
-cp $FOLDER/javassist.jar
-javaagent:$FOLDER/c4j.jar=trace=false,loglevel=warn,exit-on-unknown-contract=false
-ea
```

Of course $FOLDER should be replaced by the full path to the folder you created in the first step.

## Viewing Results ##

If any of the contracts fails, this will be logged to
```
theCurrentWorkspace/.metadata/.plugins/org.projectusus.core/c4j.log
```
If you notice anything that gets logged to this file, we'd be glad to hear about this!