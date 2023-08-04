import java.util.Scanner;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Load JSON files
        try {
            Rooms.loadRoomsFromJSON();
            Items.loadItemsFromJSON();
        } catch (IOException e) {
            System.out.println("Error loading game data: " + e.getMessage());
            return;
        }

        //Display Functions to act as a preface to the game starting.
        DisplayMethods.printTextFile("src/main/resources/textFiles/Welcome_Screen.txt");
        DisplayMethods.displayIntro();

        // Game loop starts here
        boolean isGameOver = false;
        int currentRoomId = 1; // Start in room ID 1
        Scanner scanner = new Scanner(System.in);

        // Ask the player if they want to start a new game
        if (GameMethods.startNewGame(scanner)) {
            DisplayMethods.clearScreen();
            while (!isGameOver) {
                // Get the current room based on the currentRoomId
                Room currentRoom = Rooms.getRoomById(currentRoomId);

                // Display the room name and description
                System.out.println("=== " + currentRoom.getName() + " ===");
                System.out.println(currentRoom.getDescription());

                // Display the item name if there is one in the room
                if (!currentRoom.getItems().isEmpty()) {
                    System.out.print("Items in the room: ");
                    for (Integer itemId : currentRoom.getItems()) {
                        String itemName = Items.getItemById(itemId).getName();
                        System.out.print(itemName + " ");
                    }
                    System.out.println();
                }

                // Display the prompt to the player and read their input
                System.out.print("What would you like to do? > ");
                String input = scanner.nextLine().trim().toLowerCase();

                // Process the player's input and handle game actions
                if (input.equals("quit")) {
                    // Player wants to quit, ask for confirmation
                    if (GameMethods.confirmQuit(scanner)) {
                        // Player confirmed to quit, set isGameOver to true to end the game loop
                        isGameOver = true;
                    } else {
                        // Player did not confirm, continue the game loop
                        System.out.println("Resuming game...");
                    }
                } else {
                    // Handle other game actions based on the input
                    // For example, process movement, interactions, etc.
                    System.out.println("You entered: " + input);
                }
            }
        } else {
            // Player chose not to start a new game, exit the program
            System.out.println("Thank you for considering The Lost Kingdom of Eldoria!");
        }

        // Game loop ends here
        System.out.println("Thank you for playing The Lost Kingdom of Eldoria!");
    }
}