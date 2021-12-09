package socialnetwork.repository.database;

public class DatabaseCleaner {
    public static void clearDatabase(){
        FriendshipDatabaseTableSetter.tearDown();
        MessagesSenderReceiverDatabaseTableSetter.tearDown();
        ReplyDatabaseTableSetter.tearDown();
        UserCredentialDatabaseTableSetter.tearDown();
        UserDatabaseTableSetter.tearDown();
        MessageDatabaseTableSetter.tearDown();
    }
}
