			Squishy Block
		       (Working title)
		      A game in progress


This is a small preview of what is to come. Nothing much happening yet, but some
features added.

QuickStart guide:
In a terminal run "make run"
AD and SPACE control the player (little blue man)
arrows controls the tetronimo (tetris-like block)
right control brings death to the enemies
Q shows the paths of the enemies


OLD README BELOW THE DOUBLE LINE OF "="


A* pathfinding has been added! Q shows this nicely! It was optimized with the use of clever data structures and skip-thinking!

The bees have a tendency to chill even when you can see them, this is so you can see the A* in action!
They also have a Finite State machine set for the extra 20%! They shift from waiting, to finding to chasing as needed!

The ghosts are incredibly stupid! They only head for the player, but, as ghosts, can go through walls!!


THREE WHOLE FUN LEVELS!
Calvin





================================================================================
================================================================================
I decided to do my own thing from scratch rather than the supplied blocky thing.
Learnt a lot.

Implemented Features(detailed breakdown lower):
- Player animation
- PowerUps (gems)
- Screen Shake
- Win on reaching end
- Player life
- Two! Types of enemy
- Cool Menu
- First (and only for now) level tries to tech you tricks without throwing them in your face
- Some, if I may say so myself, really clean code

Player Animation:
	The little guy walks!

PowerUps:
	Simple healing gems, but would be easy to create different kinds

Screen Shake:
	Because honestly? You know it looks awesome.

Win on getting to Door:
	Didn't think it was a feature, but it's in the assignment list, so yeah!

Player Life:
	Hearts on a nice-looking hud.

Different enemies, more to come:
	Bees and blocks, and both come for you with vengence.

Cool Menus:
	These literally took me literal minutes to make.

First Level:
	I tried to do this so people learn the game without something in the game telling
them about it. If you press control as you start, you crush the gem, "oh, that kills things"
Then a bee comes at you, maybe you get hit, lose some life, no biggy. Next a jump you can't 
make unless you use the block as a platform.. etc

Really Clean Code:
	I mean look at it! GabeN would be proud.
