package socialnetwork.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import socialnetwork.Run;
import socialnetwork.domain.entities.User;
import socialnetwork.exceptions.ExceptionBaseClass;
import socialnetwork.pagination.UserSearchResultPaginationWithOpeningUserPage;
import socialnetwork.service.SocialNetworkService;

import java.io.IOException;
import java.util.List;

public class MainMenuController {
    private SocialNetworkService service;
    private User loggedUser;
    private NotificationCheckerThread notificationCheckerThread;
    private NotificationChecker checker;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser(){
        return loggedUser;
    }

    public void setService(SocialNetworkService service) {
        this.service = service;
    }

    @FXML
    TextField userSearchTextField;
    @FXML
    Button userSearchButton;
    @FXML
    Button notificationsButton;

    @FXML
    Button messagesButton;

    @FXML
    BorderPane mainMenuBorderPane;

    @FXML
    Button activityReportButton;

    @FXML
    Button messagesReportButton;

    @FXML
    public void initialize() throws IOException {
        Image imageSearch = new Image(Run.class.getResourceAsStream("search.png"));
        userSearchButton.setGraphic(new ImageView(imageSearch));
        Image imageNotifications = new Image(Run.class.getResourceAsStream("notification_bell.png"));
        notificationsButton.setGraphic(new ImageView(imageNotifications));
        Image imageMessages = new Image(Run.class.getResourceAsStream("chat.png"));
        messagesButton.setGraphic(new ImageView(imageMessages));

        }

    @FXML
    void handleClickOnMessagesReportButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("message-report.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Run.class.getResource("main-stylesheet.css").toExternalForm());
        MessageReportController controller = loader.getController();

        controller.setService(service);
        controller.setLoggedUser(loggedUser);

        mainMenuBorderPane.setCenter(scene.getRoot());
    }

    @FXML
    void handleClickOnActivityReportButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("activity-report.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Run.class.getResource("main-stylesheet.css").toExternalForm());
        ActivityReportController controller = loader.getController();

        controller.setLoggedUser(loggedUser);
        controller.setService(service);

        mainMenuBorderPane.setCenter(scene.getRoot());
    }

    @FXML
    void handleUserSearchButtonClick(ActionEvent event){
        String userNameSearchField = userSearchTextField.getText().strip();

        String userName = parseSearchUserName(userNameSearchField);
        int count = service.getNumberOfUsersThatHaveInTheirNameTheString(userName);
        if(count == 0) {
            mainMenuBorderPane.setCenter(null);
            return;
        }

        UserSearchResultPaginationWithOpeningUserPage pagination =
                new UserSearchResultPaginationWithOpeningUserPage(count, 10, userNameSearchField);
        pagination.setMainMenuBorderPane(mainMenuBorderPane);
        pagination.setService(service);
        pagination.setLoggedUser(loggedUser);
        service.setCurrentPageIndexOfUserFiltration(0);
        service.setNumberOfUserPerFiltrationByNamePage(10);
        mainMenuBorderPane.setCenter(pagination);
    }

    private String parseSearchUserName(String userNameSearchField) {
        userNameSearchField = userNameSearchField.strip();
        String[] attributes = userNameSearchField.split(" ");

        if (attributes.length == 0)
            return "";

        if (attributes.length == 1)
            return attributes[0];

        StringBuilder fullName = new StringBuilder(attributes[0]);

        for(int i = 1; i < attributes.length; i++)
            fullName.append(" ").append(attributes[i]);
        return fullName.toString();
    }


    @FXML
    void handleClickOnLogoutButton(ActionEvent event) throws IOException {

        checker.stop();
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("authentication.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Run.class.getResource("authentication-stylesheet.css").toExternalForm());
        AuthenticationController controller = loader.getController();
        controller.setService(service);
        Run.getPrimaryStage().setScene(scene);
    }

    @FXML
    void handleClickOnHomeButton(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("user-page.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Run.class.getResource("main-stylesheet.css").toExternalForm());
        UserPageController controller = loader.getController();
        controller.setService(service);
        controller.setUserThatOwnsThePage(loggedUser);
        controller.setLoggedUser(loggedUser);
        controller.loadUserInformationOnPage();
        mainMenuBorderPane.setCenter(scene.getRoot());
    }

    @FXML
    void handleClickOnEventsButton(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("events-page.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Run.class.getResource("main-stylesheet.css").toExternalForm());
        EventsPageController controller = loader.getController();
        controller.setLoggedUser(loggedUser);
        controller.setService(service);
        mainMenuBorderPane.setCenter(scene.getRoot());
    }

    @FXML
    void handleClickOnNotificationsButton(ActionEvent event) throws IOException{
        int notificationCount = service.countAcceptedFriendRequestsSentByUser(loggedUser.getId());
        notificationCount += service.countFriendRequestsReceivedByUser(loggedUser.getId());
        notificationCount += service.getAllEventsThatAreCloseToCurrentDateForUser(loggedUser.getId()).size();

        if (notificationCount == 0) {
            Label label = new Label("You don't have new notifications");
            mainMenuBorderPane.setCenter(label);
            return;
        }
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("notification-pagination.fxml"));
        Scene scene = new Scene(loader.load());
        NotificationController controller = loader.getController();
        controller.setService(service);
        controller.setLoggedUser(loggedUser);
        controller.init();
        mainMenuBorderPane.setCenter(scene.getRoot());
    }

    @FXML
    void handleClickOnMessagesButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(Run.class.getResource("conversation-view.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Run.class.getResource("main-stylesheet.css").toExternalForm());
        ConversationController controller = loader.getController();
        controller.setService(service);
        controller.setLoggedUser(loggedUser);
        controller.loadExistingConversations();
        service.addObserver(controller);
        mainMenuBorderPane.setCenter(scene.getRoot());
    }

    public void startEventNotificationChecking() {

        checker = new NotificationChecker(service, loggedUser);
        notificationCheckerThread = new NotificationCheckerThread(checker);
        notificationCheckerThread.start();
    }
}
