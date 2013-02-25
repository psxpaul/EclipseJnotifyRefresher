EclipseRefresher
================

This is an Eclipse plugin that uses inotify on linux to automatically refresh files in your workspace. This is useful if you edit files outside of Eclipse often, and find yourself frequently F5ing your workspace.


Installation
================
- Exctract [the zip](https://github.com/psxpaul/EclipseRefresher/raw/master/inotify_plugin.zip) into {ECLIPSE_DIRECTORY}/dropins
- Start Eclipse
- Go to Window->Preferences->General->Workspace
- Enable the "Refresh using native hooks or polling" option


Normally, the above workspace option can cause performance issues on Linux, as there is no native hook implementation in Eclipse. This plugin leverages [jnotify](http://jnotify.sourceforge.net) to hook into the inotify library on Linux. The result, is that you see your file changes in realtime in Eclipse, with no performance hit!
