# General Use #

This is a very simple time tracking application.

  * Add your task and clients by clicking "Add Task"
  * Click that task in the grid to start tracking time against it
  * After 8 hours have accrued, I suggest leaving work and going home

# Feature List #

  * Added idle time tracking - when you go idle you are asked what to do with the time when you return
  * Added +, -, and numeric hotkeys for mouseless time adjustments
  * MySQL Database Support
  * Many localizations available, including US English, German, French, Dutch, and Japanese
  * Export to CSV. Export options are available under File->Options
  * Sortable Columns
  * Minimizes to tray
  * Tracks time across time zones

# MySQL Database Support #

TimeTool allows you to pull task and description lists from a MySQL database.

  1. Open the Options screen
  1. Select the MySQL Tab (shown at right)
  1. Do not change the JDBC driver entry. MySQL is the only tested database at this time.
  1. Change the Database URL to be location of your database. The format for the url is "jdbc:mysql://servername/databasename"
  1. Enter the database user name in the User Name field.
  1. Enter the database password in the password field. Warning: this is stored as plain text on disk.
  1. Alter the SQL to return a query from your database with the following columns:
    * task\_id
    * task\_name
    * description\_id
    * description\_text
  1. Check the enabled checkbox
  1. Click OK

![http://www.hamletdarcy.com/timetool/MySQLOptions.png](http://www.hamletdarcy.com/timetool/MySQLOptions.png)

# Options Screen #

Once configured, the add task dialog will contain two
drop down lists instead of free form text entry.
Add Task