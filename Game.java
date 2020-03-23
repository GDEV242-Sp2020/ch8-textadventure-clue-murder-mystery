/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
      
        // create the rooms
        Room lobby = new Room("in the lobby");
        Room billiards = new Room("in the billiards room");
        Room closet = new Room("in the closet");
        Room kitchen = new Room("in the kitchen");
        Room living = new Room("in the living room");
        Room study = new Room("in the study");
        Room hallway = new Room("in the upstairs hallway");
        Room masterBed = new Room("in the master bedroom");
        Room masterBath = new Room("in the master bathroom");
        Room bedroom1 = new Room("in guest bedroom 1");
        Room bathroom1 = new Room("in guest bathroom 1");
        Room bedroom2 = new Room("in guest bedroom 2");
        Room bathroom2 = new Room("in guest bathroom 2");
        Room garden = new Room("in the garden");
        Room gameRoom = new Room("in the game room");
        
        // initialise room exits
        lobby.setExit("study", study);
        lobby.setExit("billiards", billiards);
        lobby.setExit("kitchen", kitchen);
        lobby.setExit("closet", closet);
        lobby.setExit("living", living);
        lobby.setExit("upstairs", hallway);

        study.setExit("lobby", lobby);

        billiards.setExit("lobby", lobby);

        closet.setExit("lobby", lobby);
        
        kitchen.setExit("lobby", lobby);

        living.setExit("lobby", lobby);
        
        hallway.setExit("downstairs", lobby);
        hallway.setExit("masterBed", masterBed);
        hallway.setExit("masterBath", masterBath);
        hallway.setExit("garden", garden);
        hallway.setExit("game", gameRoom);
        hallway.setExit("bedroom1", bedroom1);
        hallway.setExit("bathroom1", bathroom1);
        hallway.setExit("bedroom2", bedroom2);
        hallway.setExit("bathroom2", bathroom2);
        
        masterBed.setExit("hallway", hallway);
        
        masterBath.setExit("hallway", hallway);
        
        gameRoom.setExit("hallway", hallway);
        
        garden.setExit("hallway", hallway);
        
        bedroom1.setExit("hallway", hallway);
        
        bathroom1.setExit("hallway", hallway);
        
        bedroom2.setExit("hallway", hallway);
        
        bathroom2.setExit("hallway", hallway);

        currentRoom = lobby;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Clue!");
        System.out.println("A murder has been commited and it's up to you to find the culprit");
        System.out.println("Your three suspects are the butler, the maid, and the house owner's wife.");
        System.out.println("To make a guess, type 'guess' and the suspect (butler, maid, wife).");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case LOOK:
                lookDescription();
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /**
     * Gives the player a visual description of the room they are in.
     * Requires the player the use the LOOK command.
     */
    private void lookDescription() {
        if(currentRoom.getShortDescription().equals("in the lobby")) {
            System.out.println("Nothing in the lobby really captures your attention.");
            System.out.println("There is a grand staircase and a few doors.");
        } else if(currentRoom.getShortDescription().equals("in the billiards room")) {
            System.out.println("As the name of the room suggests, there is a pool table in the middle of the room.");
            System.out.println("However, the 2 ball, the 11 ball, and one of the pool sticks are missing.");
        } else if(currentRoom.getShortDescription().equals("in the closet")) {
            System.out.println("Many nice and beautiful suits and dresses are hung in this closet.");
            System.out.println("Other than that, nothing else catches your attention.");
        } else if(currentRoom.getShortDescription().equals("in the kitchen")) {
            System.out.println("The kitchen appears to have not been touched.");
            System.out.println("The only thing you notice is a single knife out that is in pristine condition.");
        } else if(currentRoom.getShortDescription().equals("in the living room")) {
             System.out.println("The scene of the crime happened here.");
             System.out.println("You immediately notice two things:");
             System.out.println("One, the television is still on. It appears the man was watching ESPN when he was killed.");
             System.out.println("Two, the blood stains are dripping down the back of the couch.");
        } else if(currentRoom.getShortDescription().equals("in the study")) {
             System.out.println("Books seem to be thrown all over the place here.");
             System.out.println("After a search through the study, you do find a note.");
             System.out.println("It appears to be a small shopping list.");
             System.out.println("The two things on the list are eggs and a knife both written in cursive.");
        } else if(currentRoom.getShortDescription().equals("in the upstairs hallway")) {
             System.out.println("Many doors lead into separate rooms in this hallway.");
        } else if(currentRoom.getShortDescription().equals("in the master bedroom")) {
             System.out.println("After a thorough search of the master bedroom, you eventually find a notebook.");
             System.out.println("Since you don't have time to read the whole notebook, you skim it.");
             System.out.println("The notebook is written in cursive and doesn't appear to have any useful information.");
        } else if(currentRoom.getShortDescription().equals("in the master bathroom")) {
             System.out.println("This room appears to be untouched.");
             System.out.println("Nothing catches your eye as evidence.");
        } else if(currentRoom.getShortDescription().equals("in guest bedroom 1")) {
             System.out.println("This room appears to be untouched.");
             System.out.println("You do, however, find a to-do list in an open drawer.");
             System.out.println("The list reads, 'Ask for raise, make cake for party.'");
        } else if(currentRoom.getShortDescription().equals("in guest bathroom 1")) {
             System.out.println("This room appears to be untouched.");
             System.out.println("Nothing catches your eye as evidence.");
        } else if(currentRoom.getShortDescription().equals("in guest bedroom 2")) {
             System.out.println("This room appears to be untouched.");
             System.out.println("Nothing catches your eye as evidence.");
        } else if(currentRoom.getShortDescription().equals("in guest bathroom 2")) {
             System.out.println("This room appears to be untouched.");
             System.out.println("Nothing catches your eye as evidence.");
        } else if(currentRoom.getShortDescription().equals("in the garden")) {
             System.out.println("The only thing that catches your eye here are the amount of venus flytraps.");
             System.out.println("Out of the 20 plants here, half of them are flytraps.");
             System.out.println("Nothing else catches your attention.");
        } else if(currentRoom.getShortDescription().equals("in the game room")) {
             System.out.println("This small room has a small, round table in the middle of it.");
             System.out.println("It also has a 2 ball, an 11 ball, and a pool stick from a billiards game in it.");
        } else {
            System.out.println("This line should never run.");
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
