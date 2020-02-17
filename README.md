# reagent-web-demo

Demo project for reagent and figwheel.

## Development

To get an interactive development environment run:

    lein fig:build

Or import the project in Cursive, select the `figwheel REPL` run
configuration and press Play.

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

	lein clean

## License

Copyright © 2020 Pilloxa

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
