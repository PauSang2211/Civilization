# Civilization Game Project

This project was inspired by Sid Meier's Civilization game series and my hobby in simulation games. This project was started a few years ago. 

# Goal:
To practice object-oriented programming <br />
To understand Java GUI, particularly Java AWT and Java Swing <br />
To analyze the results of (pseudo) random actions of players in the game

# Overview:
The game starts by generating a world using Perlin Noise. I did not write the code for this: I found it on a website but I am unable to trace the exact page. 
Once the map has been generated, multiple tribes randomly come alive and die based upon movement, number of members in the tribe, food, etc. 
When two tribes come into contact, there are two possibilities: they can fight and one tribe comes out alive, or they are both passive and allow each other to pass. Either possibility is calculated by a basic mathematical function. 
As tribes move around, they have a chance to evolve into a village. Tribes close to bodies of water have a higher chance of establishing a village.
A village accumulates money over turns and they can purchase units (like Explorers to expand territory) or buildings (like Farms for food or Houses for population control). 
