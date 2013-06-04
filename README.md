tamal
=====

A tech demo / learning experience based on Minicraft.

## Origin

Notch created [Minicraft](http://en.wikipedia.org/wiki/Minicraft) for Ludum Dare #22. I don't remember where I first heard about the game (probably [Boing Boing](http://boingboing.net)), but upon seeing that it was a complete game written only in Java, with no external dependencies, I realized that this was an application I could possibly wrap my head around.

Even as of this writing I haven't completely understood all of it, but I've definitely learned a lot by tearing apart the code and reconstituting it. My eventual goal is to create my own new game from it, but I've already been rewarded by learning how this game works its magic.

I snagged a copy of Minicraft from [Miserlou's copy](https://github.com/Miserlou/Minicraft) and started from the bottom, working my way up. I've had to disable entire mechanisms in the game for now just to get something basic going, and they may or may not be back depending on what my final game idea requires.

## Building

The game, such as it is, builds under Maven. There are no external dependencies!

## Running

There are currently only two runnable classes, under the us.havanki.tamal.gfx package. The Demo class simply renders a sprite; believe me, I felt this was an accomplishment all by itself.

The Demo2 class renders a randomized landscape and runs the game clock, which lets the grass grow. It's kinda neat. The main purpose for this demo was to get the tiles rendering properly. There is some crazy math involved there.

