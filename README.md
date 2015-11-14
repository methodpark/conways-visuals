# Conways Game Of Life Visualisation

## Rationale

Running this project will start a http server listening on port 10555.
When opening http://localhost:10555/ with a browser should see a `Conways game of live` board.
The board itself is not calculated by the server, it's only displaying it.
To put a new board to the server one has to send a HTTP PUT request to the
`/board` route.
The new board has to be in JSON format and has to be of the following scheme:
```javascript
{"living": [<list of living cells>]}
```
Where <list of living cells> are arrays of x and y coordinates on a two dimensional
board. [0 0] represents the upper left corner. Negative coordinates are not rendered by the
clients as of now.

So when repeatedly pushing updated board states to the server the clients should be displaying
the world changing as they poll the server in short cycles.

### Starting the server

To start the server open a console in the projects main directory and run `java -jar bin/visual.jar`.
You should then see a output like:

```
2015-12-04 10:39:17.011:INFO::main: Logging initialized @2440ms
Starting web server on port 10555.
2015-12-04 10:39:17.142:INFO:oejs.Server:main: jetty-9.2.z-SNAPSHOT
2015-12-04 10:39:17.163:INFO:oejs.ServerConnector:main: Started ServerConnector@3f54dd88{HTTP/1.1}{0.0.0.0:10555}
2015-12-04 10:39:17.163:INFO:oejs.Server:main: Started @2592ms
```
Then point your browser to http://localhost:10555/ and start sending new boards to the server.

## Disclaimer

This code was put together in a hurry at the global day of code retreat 2015.
Its sole purpose was to visualise the outcome of the other participants.
Hence the code quality in itself has to be taken with a grain of salt. ;-)


## Development

Open a terminal and type `lein repl` to start a Clojure REPL
(interactive prompt).

In the REPL, type

```clojure
(run)
(browser-repl)
```

The call to `(run)` does two things, it starts the webserver at port
10555, and also the Figwheel server which takes care of live reloading
ClojureScript code and CSS. Give them some time to start.

Running `(browser-repl)` starts the Weasel REPL server, and drops you
into a ClojureScript REPL. Evaluating expressions here will only work
once you've loaded the page, so the browser can connect to Weasel.

When you see the line `Successfully compiled "resources/public/app.js"
in 21.36 seconds.`, you're ready to go. Browse to
`http://localhost:10555` and enjoy.

**Attention: It is not longer needed to run `lein figwheel`
  separately. This is now taken care of behind the scenes**

## Trying it out

If all is well you now have a browser window saying 'Hello Chestnut',
and a REPL prompt that looks like `cljs.user=>`.

Open `resources/public/css/style.css` and change some styling of the
H1 element. Notice how it's updated instantly in the browser.

Open `src/cljs/visual/core.cljs`, and change `dom/h1` to
`dom/h2`. As soon as you save the file, your browser is updated.

In the REPL, type

```
(ns visual.core)
(swap! app-state assoc :text "Interactivity FTW")
```

Notice again how the browser updates.

## Deploying to Heroku

This assumes you have a
[Heroku account](https://signup.heroku.com/dc), have installed the
[Heroku toolbelt](https://toolbelt.heroku.com/), and have done a
`heroku login` before.

``` sh
git init
git add -A
git commit
heroku create
git push heroku master:master
heroku open
```

## Running with Foreman

Heroku uses [Foreman](http://ddollar.github.io/foreman/) to run your
app, which uses the `Procfile` in your repository to figure out which
server command to run. Heroku also compiles and runs your code with a
Leiningen "production" profile, instead of "dev". To locally simulate
what Heroku does you can do:

``` sh
lein with-profile -dev,+production uberjar && foreman start
```

Now your app is running at
[http://localhost:5000](http://localhost:5000) in production mode.

## License

Copyright © 2014 Benjamin Klüglein

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Chestnut

Created with [Chestnut](http://plexus.github.io/chestnut/) 0.8.1 (c6d3243e).
