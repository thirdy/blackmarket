# blackmarket

#### Info:

*blackmarket* is a fan made application for the free to play MMORPG game Path of Exile. It aims to provide the people of wraeclast the ultimate way to find the best items in the market.

#### Screenshot:

![screenshot-1](https://github.com/thirdy/blackmarket/blob/master/blackmarket/srcsht-1.PNG)

#### Features

1. Search form
  a. most common fields shown first
  b. use radio/checkboxes over combobox for usability
2. Copy item to shortlist
3. Provide graphical view of the stats
4. Bookmark items if user is offline
5. Show improved view on account age and highest level. right now it's a1253h87

#### Hard to implement Features

1. Query language for searching
2. Search for upgrades based on item data in the clipboard
3. Activate application on hotkey press from PoE

#### How to run:

cd into blackmarket-ui directory
mvn exec:java -Dexec.mainClass="net.thirdy.blackmarket.MainApp"
