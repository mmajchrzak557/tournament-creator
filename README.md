# tournament-creator
<h1>About the app :iphone:</h1>
The app is supposed to help in creating and keeping track of tournaments of any type. For example, it might come in handy when you organize a FIFA tournament with your friends. Right now, it supports two types of tournaments - single elimination tournament (simple bracket, like in NBA Playoffs) or single round robin (all vs. all). You specify the tournament type, name, amount of players participating and the names of the players. Then the application creates a view that lets you edit scores in selected matches. You can also save and load tournaments.

<h1>Goals of the project :heavy_check_mark:</h1>
This project was created with 3 main goals in mind:

* Learning Kotlin

* Making the layout and views somewhat nice compared to previous apps

* Publishing the app to the Play Store (still in progress, the app still needs polishing and some debugging)

<h1>Overview :eyes:</h1>
This is how the boring layouts look:


<p float="left" align="center" style="margin-top:10px;">
  <img src="/readme_assets/main.jpg" width="250"/>
  <img src="/readme_assets/tournament_creation_dialog.jpg" width="250" /> 
  <img src="/readme_assets/changing_players.jpg" width="250" />
</p>


And these are the bread and butter of the app:


<p float="left" align="center">
  <img src="/readme_assets/table_demo.gif" width="250"/>
  <img src="/readme_assets/bracket_demo.gif" width="250" /> 
</p>

<h1>The code :clipboard:</h1>
The code is not super interesting, because the app is pretty simple. One nice thing is how polymorphism is used to make adding new tournament types easier. To create a new tournament one needs only to create 2 classes: one extending <b><i>Tournament</b></i> and second extending <b><i>TournamentView</b></i>. Saving and loading will be taken care off without almost any changes to the code. There is still a lot of refactoring to do, especially in <b><i>BracketView</b></i> class (seriously, don't try reading it), but also in other places. The visuals also need some work. One thing that looks really bad is the main screen, when there are no saved tournaments. Once these problems are solved I'll try to post this app to the Play Store.
