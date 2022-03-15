# Minesweeper

run it locally with `sbt run` on `localhost:9000`

how to play:
 - left click to reveal cell
 - right click to flag cell

REST API Docs: https://documenter.getpostman.com/view/7384429/S1Lr3qMm

## Decisions

Here's the list of features in the order I did them and how
I did them in the order in which made the most sense to grow the app incrementally

The only missing feature is time tracking, I ran out of time and didn't find it interesting but it'd be easy to implement as is


* **Minesweeper core**: I built the game logic first as its the most important part of the excercise. I did it TDD style. There is basically a `Grid` you can mark/sweep and a grid generator to build them. Also please check the tests out, they look nice and there's a fair ammount. Would love to refactor and write more tests if I had more time
  * **Detect when game is over**: Game is over when you sweep a bomb or you marked exactly all mines (which is a set, not very interesting)
  * **Ability to 'flag' a cell with a question mark or red flag**: Just adds the marked cell to said set
  * **When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat)**: Just used recursion
  * **Ability to select the game's number of rows, columns, and mines**: Hardest part is generating the grid, specifically placing the mines. 
  There is a logic/math way of satisfying a valid grid, with the time available it was easier to generate grids until one is valid 
* **Design and implement a documented RESTful API for the game (think of a mobile app for your API)**: Done with akka http, you can find the link above. It's fairly simple and RESTful except for the /sweep and /mark endpoints which are verbs. The RESTful way of implementing mark/sweep would be having a `/games/id/moves` resource and POST something like `{"x": 0, "y": 0, "type": "mark/sweep"}`. Another alternative would be to make the game keep a list of moves and using `PATCH` on it. Both approaches are ugly, I like it better as it is
* **Persistence**: Done with h2, while set to in memory in this repo, I changed it to be actually persistent when deployed (just changing the h2 JDBC URL). Used scalike because it was easier to just write the simple sql needed inside a couple of DAOs
* **Ability to support multiple users/accounts**: Uses BASIC HTTP Auth, retrieving passwords from the DB. Passwords are not hashed/salted because I didn't want to sink time into security, which wasn't a test requirement 
* **Ability to start a new game and preserve/resume the old ones**: Once I had users and persistance, it was easy to save a game row associated with its user
* **Implement an API client library for the API designed above. Ideally, in a different language, of your preference, to the one used for the API**: It's in javascript, used by the frontend (although it can be used in node, server side) you can navigate to it on the repo or find it [here](https://github.com/julianSelser/minesweeper/blob/master/src/main/resources/site/js/client.js) 
