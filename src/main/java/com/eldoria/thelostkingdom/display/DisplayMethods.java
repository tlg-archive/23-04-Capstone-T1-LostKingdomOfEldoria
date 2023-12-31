package com.eldoria.thelostkingdom.display;

import com.eldoria.thelostkingdom.Main;
import com.eldoria.thelostkingdom.gamelogic.GameMethods;
import com.eldoria.thelostkingdom.rooms.Rooms;
import com.eldoria.thelostkingdom.view.GameWindow;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;

public class DisplayMethods extends Colors {  //NEW CODE: all cyan previously blue

    public static void printTextJsonFile(String fileName) {
        try {
            Map<String, String> textMap = GameMethods.loadJSONTextFile(fileName, new TypeToken<Map<String, String>>() {
            });
            System.out.println(textMap.get("text"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printHeader() {
        GameWindow.textArea.append("\nCharacter:\n" + Main.player);
        GameWindow.textArea.append("\nDescription:\n" + Rooms.getRoomById(Main.player.getCurrentRoomId()).getDescription() + "\n");
    }

    public static void printRoomItems() {
        if (Rooms.getRoomById(Main.player.getCurrentRoomId()).getItems().size() > 0) {
            GameWindow.textArea.append("\nItems in the Room: | ");
            for (int i = 0; i < Rooms.getRoomById(Main.player.getCurrentRoomId()).getItems().size(); ++i) {
                GameWindow.textArea.append(Rooms.getRoomById(Main.player.getCurrentRoomId()).getItems().get(i).get("name") + " | \n");
            }
        } else {
            GameWindow.textArea.append("\nItems in the Room: |" + " No items in the room |" + "\n");
        }
    }

    public static void printRoomNPC() {
        try {
            Object roomNPC = Rooms.getRoomById(Main.player.getCurrentRoomId()).getNPC().get("name");
            GameWindow.textArea.append("\nCharacters in the Room: | " + roomNPC  + " |" + "\n");
        } catch (Exception e) {
            GameWindow.textArea.append("\nCharacters in the Room: |" + " No characters in the room | \n");
        }
    }

    public static void printSuccessfulAttack() {
        try {
            Object roomNPC = Rooms.getRoomById(Main.player.getCurrentRoomId()).getNPC().get("name");
            System.out.println("You have successfully attacked: | " + roomNPC + " |" + "\n");
        } catch (Exception e) {
            System.out.println("There was no character to attack!!!"+ "\n");
        }
    }

    public static void printTextFile(String fileName) {
        // Prints the opening splash screen
        //noinspection ConstantConditions
        try (BufferedReader br = new BufferedReader(new InputStreamReader(DisplayMethods.class.getClassLoader().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.print("Press any key to continue...");
        // Wait for the player to press Enter before continuing
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void printTextMap() {

        String path = "textFiles/Castle_Map.txt";
        String spaces32 = new String(new char[32]).replace('\0', ' ');

        int inRoom = Main.player.getCurrentRoomId();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(DisplayMethods.class.getClassLoader().getResourceAsStream(path)))) {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
                switch (i) {
                    case 9:
                        if (inRoom == 9)
                            System.out.println(spaces32 + "##     " + Colors.cyan + "X" + Colors.green + "    ##");
                        break;
                    case 18:
                        if (inRoom == 10)
                            System.out.println("##         " + Colors.cyan + "X" + Colors.green + "              ##                  ##                          ##");
                        if (inRoom == 5)
                            System.out.println("##                        ##         " + Colors.cyan + "X" + Colors.green + "        ##                          ##");
                        if (inRoom == 8)
                            System.out.println("##                        ##                  ##            " + Colors.cyan + "X" + Colors.green + "             ##");
                        break;
                    case 26:
                        if (inRoom == 2)
                            System.out.println("##                    ##             " + Colors.cyan + "X" + Colors.green + "             ##                     ##");
                        if (inRoom == 6)
                            System.out.println("##        " + Colors.cyan + "X" + Colors.green + "           ##                           ##                     ##");
                        if (inRoom == 7)
                            System.out.println("##                    ##                           ##        " + Colors.cyan + "X" + Colors.green + "            ##");
                        break;
                    case 35:
                        if (inRoom == 4)
                            System.out.println("##    #   " + "X" + "  #                                             #      #       ##");
                        if (inRoom == 1)
                            System.out.println("##    #      #                       " + "X" + "                     #      #       ##");
                        if (inRoom == 3)
                            System.out.println("##    #      #                                             #   " + "X" + "  #       ##");
                        break;
                    default:
                        System.out.println(line);
                        break;
                }
                i++;
            }
            System.out.println("\n" + "X" + " - denotes players current position; player can go north, south, east or west; up/down for stairs");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.print("Press any key to continue...");
        // Wait for the player to press Enter before continuing
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
        ////===================old clearScreen function ============================///
    //    public static void clearScreen() {
//        // Print 50 empty lines to simulate screen clearing
//        for (int i = 0; i < 50; i++) {
//            System.out.println();
//        }
//    }

    //============RMR new ClearScreen engine 8.16.23============================//
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromResourceFile(String fileName) {

        String fileContent = "";

        // First, try to access the resource from the classpath (e.g., JAR file)
        InputStream inputStream = DisplayMethods.class.getResourceAsStream("/" + fileName);

        if (inputStream != null) {
            // Resource is found (in JAR), process the input stream as needed
            try {
                // Read content from the resource
                byte[] resourceBytes = inputStream.readAllBytes();
                fileContent = new String(resourceBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileContent + "\n";
    }

}
