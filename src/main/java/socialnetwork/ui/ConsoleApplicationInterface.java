package socialnetwork.ui;



import socialnetwork.controllers.NetworkController;
import socialnetwork.domain.models.Friendship;
import socialnetwork.domain.models.User;
import socialnetwork.exceptions.ExceptionBaseClass;
import socialnetwork.exceptions.InvalidNumericalValueException;

import java.util.*;
import java.util.function.Consumer;

class Command{
    public static final String EXIT = "exit";
    public static final String ADD_USER = "add user";
    public static final String REMOVE_USER = "remove user";
    public static final String FIND_USER = "find user";
    public static final String UPDATE_USER = "update user";
    public static final String GET_ALL_USERS = "get all users";

    public static final String ADD_FRIENDSHIP = "add friendship";
    public static final String REMOVE_FRIENDSHIP = "remove friendship";
    public static final String FIND_FRIENDSHIP = "find friendship";

    public static final String COUNT_COMMUNITIES = "count communities";
    public static final String MOST_SOCIAL = "most social";

}

public class ConsoleApplicationInterface {

    private NetworkController networkController;

    private Scanner inputReader = new Scanner(System.in);
    private Map<String, Runnable> commandMap = new HashMap<>();

    private static final int MENU_INDENTATION = 5;

    public ConsoleApplicationInterface(NetworkController networkController){
        this.networkController = networkController;
        initializeCommandMap();
    }

    public void run(){
        while(true){
            printMainMenu();
            System.out.print(">> ");
            String userCommand = readStringFromUser();

            if(userCommand.compareTo(Command.EXIT) == 0){
                inputReader.close();
                return;
            }

            if(commandMap.containsKey(userCommand))
                try {
                    commandMap.get(userCommand).run();
                } catch (ExceptionBaseClass exception){
                    System.out.println(exception.getMessage());
                }
            else
                System.out.println("Invalid command");
        }
    }

    private void printMainMenu(){
        String menuCommandsFormat = "-- %s".indent(MENU_INDENTATION);
        System.out.print("SOCIAL NETWORK APPLICATION".indent(MENU_INDENTATION));
        System.out.print("MAIN MENU".indent(MENU_INDENTATION));
        System.out.printf(menuCommandsFormat, Command.ADD_USER);
        System.out.printf(menuCommandsFormat, Command.REMOVE_USER);
        System.out.printf(menuCommandsFormat, Command.FIND_USER);
        System.out.printf(menuCommandsFormat, Command.UPDATE_USER);

        System.out.printf(menuCommandsFormat, Command.GET_ALL_USERS);
        System.out.printf(menuCommandsFormat, Command.ADD_FRIENDSHIP);
        System.out.printf(menuCommandsFormat, Command.REMOVE_FRIENDSHIP);
        System.out.printf(menuCommandsFormat, Command.FIND_FRIENDSHIP);
        System.out.printf(menuCommandsFormat, Command.COUNT_COMMUNITIES);
        System.out.printf(menuCommandsFormat, Command.MOST_SOCIAL);
        System.out.printf(menuCommandsFormat, Command.EXIT);
    }

    private void initializeCommandMap(){
        commandMap.put(Command.ADD_USER, this::addUser);
        commandMap.put(Command.REMOVE_USER, this::removeUser);
        commandMap.put(Command.FIND_USER, this::findUser);
        commandMap.put(Command.UPDATE_USER, this::updateUser);
        commandMap.put(Command.GET_ALL_USERS, this::getAllUsersWithTheirFriends);

        commandMap.put(Command.ADD_FRIENDSHIP, this::addFriendship);
        commandMap.put(Command.REMOVE_FRIENDSHIP, this::removeFriendship);
        commandMap.put(Command.FIND_FRIENDSHIP, this::findFriendship);
        commandMap.put(Command.COUNT_COMMUNITIES, this::countCommunities);
        commandMap.put(Command.MOST_SOCIAL, this::findMostSocialCommunity);
    }

    private void findFriendship() {
        System.out.print("Id of first user: ");
        Long idOfFirstUser = readLongFromUser("Invalid value for the id of the first user");

        System.out.print("Id of second user: ");
        Long idOfSecondUser = readLongFromUser("Invalid value for the id of the second user");

        Optional<Friendship> existingFriendshipOptional = networkController.findFriendship(idOfFirstUser, idOfSecondUser);
        if(existingFriendshipOptional.isEmpty())
            System.out.printf("Users with id %d and %d are not friends\n", idOfFirstUser, idOfSecondUser);
        else
            System.out.println(existingFriendshipOptional.get());
    }

    private void updateUser() {
        System.out.print("Id: ");
        Long id = readLongFromUser("Invalid value for id");
        System.out.print("New first name: ");
        String newFirstName = readStringFromUser();
        System.out.print("New last name: ");
        String newLastName = readStringFromUser();

        Optional<User> oldUserOptional = networkController.updateUser(id, newFirstName, newLastName);

        if(oldUserOptional.isEmpty())
            System.out.println("User doesn't exist");
        else
            System.out.printf("User %s has been updated\n", oldUserOptional.get());
    }

    private void findUser() {
        System.out.print("Id: ");
        Long id = readLongFromUser("Invalid value for id");
        Optional<User> existingUserOptional = networkController.findUserById(id);

        if(existingUserOptional.isEmpty())
            System.out.printf("User with id %d doesn't exist\n", id);
        else
            System.out.println(existingUserOptional.get());
    }

    private void findMostSocialCommunity() {
        List<User> usersOfMostSocialCommunity = networkController.getMostSocialCommunity();
        System.out.println("Most social community is:");
        usersOfMostSocialCommunity.forEach(System.out::println);
    }

    private void countCommunities() {
        int numberOfCommunities = networkController.getNumberOfCommunitiesInNetwork();

        if(numberOfCommunities > 1)
            System.out.printf("%d communities are in the network\n", numberOfCommunities);
        else if(numberOfCommunities == 1)
            System.out.println("1 community is in the network");
        else if(numberOfCommunities == 0)
            System.out.println("There aren't any communities in the network\n");
    }

    private String readStringFromUser(){
        return inputReader
                .nextLine()
                .stripLeading()
                .stripTrailing();
    }

    private long readLongFromUser(){
        long userInput = inputReader
                .nextLong();
        inputReader.nextLine();
        return userInput;
    }

    private long readLongFromUser(String invalidNumericalValueExceptionMessage){
        long userInput;
        try{
            userInput = readLongFromUser();
        } catch (InputMismatchException exception){
            inputReader.nextLine();
            throw new InvalidNumericalValueException(invalidNumericalValueExceptionMessage);
        }
        return userInput;
    }

    private void addUser(){
        System.out.print("Id: ");
        long id = readLongFromUser("Invalid value for id");

        System.out.print("First name: ");
        String firstName = readStringFromUser();

        System.out.print("Last name: ");
        String lastName = readStringFromUser();

        Optional<User> existingUserOptional = networkController.addUser(id, firstName, lastName);

        if(existingUserOptional.isPresent()){
            User existingUser = existingUserOptional.get();
            System.out.println("User with same id already exists: ".concat(existingUser.toString()));
        }
        else
            System.out.println("User has been added");
    }

    private void removeUser() {
        System.out.print("Id: ");
        long id = readLongFromUser("Invalid value for id");
        Optional<User> removedUserOptional = networkController.removeUser(id);

        if(removedUserOptional.isPresent()){
            User removedUser = removedUserOptional.get();
            System.out.println("User ".concat(removedUser.toString()).concat(" has been removed"));
        }
        else
            System.out.println("User with the given id doesn't exist");
    }

    private void getAllUsersWithTheirFriends(){
        List<User> allUserList = networkController.getAllUsersAndTheirFriends();
        Consumer<User> userPrinterConsumer = user ->{
            System.out.println(user.toString());
        };
        Consumer<User> userPrinterConsumerWithFriends = user -> {
            userPrinterConsumer.accept(user);
            System.out.println("Friends: ");
            if(user.getFriendsList().size() == 0)
                System.out.println("None");
            else
                user.getFriendsList().forEach(userPrinterConsumer);
            System.out.println();
        };
        allUserList.forEach(userPrinterConsumerWithFriends);
    }

    private void addFriendship(){
        System.out.print("Id of first user: ");
        long idOfFirstUser = readLongFromUser("Invalid value for id of the first user");
        System.out.print("Id of second user: ");
        long idOfSecondUser = readLongFromUser("Invalid value for id of the second user");
        Optional<Friendship> existingFriendShipOptional = networkController.addFriendship(idOfFirstUser, idOfSecondUser);
        if(existingFriendShipOptional.isPresent())
            System.out.printf("Friendship between %d and %d already exists\n", idOfFirstUser, idOfSecondUser);
        else
            System.out.printf("Friendship between %d and %d has been added\n", idOfFirstUser, idOfSecondUser);
    }

    private void removeFriendship() {
        System.out.print("Id of first user: ");
        long idOfFirstUser = readLongFromUser("Invalid value for id of the first user");
        System.out.print("Id of second user: ");
        long idOfSecondUser = readLongFromUser("Invalid value for id of the second user");
        Optional<Friendship> existingFriendShipOptional = networkController.removeFriendship(idOfFirstUser, idOfSecondUser);
        if(existingFriendShipOptional.isPresent())
            System.out.printf("Friendship between %d and %d has been removed\n", idOfFirstUser, idOfSecondUser);
        else
            System.out.printf("Friendship between %d and %d doesn't exist\n", idOfFirstUser, idOfSecondUser);
    }
}

