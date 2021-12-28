package socialnetwork.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import socialnetwork.Run;
import socialnetwork.domain.entities.Event;
import socialnetwork.domain.entities.EventParticipant;
import socialnetwork.domain.entities.User;
import socialnetwork.exceptions.ExceptionBaseClass;
import socialnetwork.service.SocialNetworkService;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventsPageController {
    private SocialNetworkService service;
    private User loggedUser;

    private List<Event> events = new ArrayList<>();
    private List<ToggleButton> signUpToggleButtons = new ArrayList<>();

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser(){
        return loggedUser;
    }

    public void setService(SocialNetworkService service) {
        this.service = service;
        setPaginationModel();
    }

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button fileChooserButton;
    @FXML
    private FileChooser fileChooser = new FileChooser();
    @FXML
    Pagination pagination;

    private File imageFile;

    @FXML
    public void initialize(){
        datePicker.setValue(LocalDate.now());

        fileChooserButton.setOnAction((ActionEvent event) -> {
            File file = fileChooser.showOpenDialog(Run.getPrimaryStage());
            if(file != null){
                imageFile = file;
                fileChooserButton.setText("Image chosen!");
            }
        });
    }

    @FXML
    public void handleClickOnAddEventButton(ActionEvent event){
        if(imageFile == null){
            Run.showPopUpWindow("Warning", "Must choose an image file!");
            return;
        }

        String name = nameTextField.getText();
        String description = descriptionTextField.getText();
        LocalDate date = datePicker.getValue();
        try{
            service.addEventService(name, description, date, imageFile.getAbsolutePath());

            setPaginationModel();
            pagination.setCurrentPageIndex(pagination.getPageCount()-1);

            nameTextField.clear();
            descriptionTextField.clear();
            datePicker.setValue(LocalDate.now());
            imageFile = null;
            fileChooserButton.setText("Choose an image!");
        }catch (ExceptionBaseClass exception) {
            Run.showPopUpWindow("Warning", exception.getMessage());
        }
    }

    private void handleClickOnSignUpToEventToggleButton(ActionEvent event, Long eventId, ToggleButton signUp){
        Optional<EventParticipant> eventParticipant = service.findOneEventParticipantService(loggedUser.getId(), eventId);
        if(eventParticipant.isPresent()){
            service.removeEventParticipantService(loggedUser.getId(), eventId);
            signUp.setSelected(false);
            signUp.setText("Sign Up");
        }
        else{
            service.addEventParticipantService(loggedUser.getId(), eventId);
            signUp.setSelected(true);
            signUp.setText("Signed Up");
        }
    }

    private void setSignUpToggleButtonState(ToggleButton signUp, Event event){
        if(ChronoUnit.DAYS.between(LocalDate.now(), event.getDate()) < 0){
            signUp.setText("Sign Up");
            signUp.setDisable(true);
            return;
        }
        Optional<EventParticipant> eventParticipant = service.findOneEventParticipantService(loggedUser.getId(), event.getId());
        if(eventParticipant.isPresent()){
            signUp.setSelected(true);
            signUp.setText("Signed Up");
        }
        else{
            signUp.setSelected(false);
            signUp.setText("Sign Up");
        }
    }

    private HBox createPage(int pageIndex){
        Event event = events.get(pageIndex);

        VBox vBox = new VBox();
        Label nameLabel = new Label(event.getName());
        Label descriptionLabel = new Label(event.getDescription());
        Label dateLabel = new Label(event.getDate().toString());
        ToggleButton signUp = new ToggleButton();
        setSignUpToggleButtonState(signUp, event);
        signUp.setOnAction((ActionEvent e) -> handleClickOnSignUpToEventToggleButton(e, event.getId(), signUp));
        signUpToggleButtons.add(signUp);
        vBox.getChildren().addAll(nameLabel, descriptionLabel, dateLabel, signUp);

        Image eventImage = new Image(String.valueOf(Run.class.getResource("rick.jpg")));
        ImageView imageView = new ImageView(eventImage);
        HBox hBox = new HBox(imageView, vBox);

        return hBox;
    }

    private void setPaginationModel(){
        signUpToggleButtons.removeAll(signUpToggleButtons);
        events = service.getAllEventsService();
        pagination.setPageCount(events.size());
        pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
    }
}
