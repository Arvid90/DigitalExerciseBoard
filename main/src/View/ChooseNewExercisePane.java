package View;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.image.Image;

import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;

import Model.Exercises;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;

/**
 * @author joakimstoor
 */

/* Defines a pane showing image views of exercises that has 
 * not yet been chosen */
public class ChooseNewExercisePane extends Pane {
    private MyBorderPane borderPane;
    private List<Exercises> exerciseList;
    private List<ExerciseImageView> exerciseImageList;
    private FlowPane flowPane;
    private ScrollPane scrollPane;
    private Button exerciseControlButton;
    private ExerciseImageView activeExerciseImageView;
    private StackPane an;
    private MainClass mainClass;
    private Label label;
    
            
    /* Initializes a pane where users can choose a new exercise. The exerciseList 
     * is initially full of all the exercises. */
    public ChooseNewExercisePane(MainClass mainClass, List<Exercises> exerciseList) {
        this.exerciseImageList = new ArrayList();
        this.exerciseList = exerciseList;
        this.mainClass = mainClass;
        activeExerciseImageView = null;
        
        // More exercises than whats possible to included in the pane space
        // are needed, i.e. a scroll pane is used
        scrollPane = new ScrollPane();
        flowPane = new FlowPane(); // Images of exercise are stored in a flow pane
        borderPane = new MyBorderPane(mainClass, true, true);
        initializeImageViews(); 
        initialzeControlButton();
        
        this.setStyle("-fx-background-color: black");
        this.getChildren().add(borderPane);
    }
    
    
    /* Initializes all the Image views by looping through the arrayList with 
     * the exercise enums*/
    private void initializeImageViews() {
        an = new StackPane(); // Is the base structure of the image view
        flowPane.setHgap(ProgramConstants.WINDOWWIDTH/20);
        flowPane.setVgap(ProgramConstants.WINDOWHEIGHT/20);
        flowPane.setPadding(new Insets(10, 10, 10, 10));
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER); // Only horizontal bar
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        // Fixed size for the main structure, i.e. therefore for the 
        // flow- and scrollpane also
        an.setMaxSize(ProgramConstants.WINDOWWIDTH/1.5, 
                ProgramConstants.WINDOWHEIGHT/1.3);
        an.setMinSize(ProgramConstants.WINDOWWIDTH/1.5, 
                ProgramConstants.WINDOWHEIGHT/1.3);
        scrollPane.setFitToWidth(true);
        
        // Activates an exercise image and adds it to the exerciseImageList and
        // to the flowPane
        for (int i = 0; i < exerciseList.size(); i++) {
            activateImagePDL(exerciseList.get(i));
        }

        flowPane.setAlignment(Pos.CENTER);
        an.getChildren().add(scrollPane);
        scrollPane.setContent(flowPane);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        flowPane.setStyle("-fx-background-color: black;");
        scrollPane.getStyleClass().add("scroll-pane");
        
        // This is different from the other styling
        scrollPane.setStyle("-fx-border-color: white; -fx-background: black");
        an.setStyle("-fx-background-color: black");
        an.setAlignment(Pos.CENTER);
        borderPane.setCenter(an);
    }
    
    /* Creates the ImageView and adds it to the two used lists*/
    private void activateImagePDL(Exercises e) {
        String url = StaticHelpMethods.getURLFromExercises(e);
        Image image = new Image(url);
        ExerciseImageView pn = new ExerciseImageView(e, image, 5, 2.5, borderPane, 
                false); 
            
        // Activates the ExerciseImageView to handle a mouseClickedEvent
        pn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // If an imageView is already active if this is being clicked 
                // that one "inactivated"
                if (activeExerciseImageView != null) { 
                    activeExerciseImageView.getRectangle().setStroke(
                            ProgramConstants.PATIENTBACKGROUNDCOLOR);
                    activeExerciseImageView.setStyle("-fx-border-color: black");
                    label.setText("");
                }
                // If it equals the active Image View, the Active Image View 
                // is set to null, if not it's set to the image view being 
                // clicked, which is activated to the user shown by a red stroke
                if(pn.equals(activeExerciseImageView)) {
                    activeExerciseImageView.getRectangle().setStroke(
                            ProgramConstants.PATIENTBACKGROUNDCOLOR);
                    activeExerciseImageView.setStyle("-fx-border-color: black");
                    activeExerciseImageView = null;
                    exerciseControlButton.setDisable(true);
                    label.setText(" ");
                }
                else {
                    activeExerciseImageView = pn;
                    activeExerciseImageView.getRectangle().setStroke(Color.RED);
                    activeExerciseImageView.setStyle("-fx-border-color: red");
                    label.setText(StaticHelpMethods.returnFormattedString(
                            activeExerciseImageView.getExerciseEnum()));
                    exerciseControlButton.setDisable(false);
                }
            }
        });
            
        exerciseImageList.add(pn);
        flowPane.getChildren().add(pn);
    } 
    
    /* Initializes the control button to include an activated exercise
     * Is inactivated when no exercise is active*/
    private void initialzeControlButton() {
        exerciseControlButton = new Button("Lägg till");
        exerciseControlButton.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        exerciseControlButton.setMinSize(ProgramConstants.WINDOWWIDTH/6,
                    ProgramConstants.WINDOWHEIGHT/10);
        exerciseControlButton.setMaxSize(ProgramConstants.WINDOWWIDTH/6,
                    ProgramConstants.WINDOWHEIGHT/10);
        exerciseControlButton.setStyle("-fx-background-color: black; "
                + "-fx-border-color: white; -fx-border-radius: 3px;  "
                + "-fx-text-align: center; -fx-font-family: Times; "
                + "-fx-font-size: " + Integer.toString(ProgramConstants.EXERCIZE_TEXT_SIZE));
        /*exerciseControlButton.translateXProperty().bind(
                borderPane.widthProperty().divide(-25));
        exerciseControlButton.translateYProperty().bind(
                this.heightProperty().divide(2));*/
        
        VBox vb = new VBox();
        vb.spacingProperty().bind(borderPane.heightProperty().divide(30));
        
        initLabel();
        vb.getChildren().addAll(exerciseControlButton, label);
        vb.setAlignment(Pos.CENTER);
        
        Group g = new Group();
        g.getChildren().add(vb);
        g.translateXProperty().bind(borderPane.widthProperty().divide(-25));
        g.translateYProperty().bind(borderPane.heightProperty().divide(2));
        
        borderPane.setRight(g);
        exerciseControlButton.setDisable(true);
        
        // Creates new exercise if the button is clicked when the exercise
        // is activated
        exerciseControlButton.setOnAction(e -> {
            
            mainClass.getController().createNewExercise(
                    activeExerciseImageView.getExerciseEnum());
            
            exerciseControlButton.setDisable(true);
            activeExerciseImageView.getRectangle().setStroke(
                                ProgramConstants.PATIENTBACKGROUNDCOLOR);
            activeExerciseImageView.setStyle("-fx-border-color: black");
            deleteExerciseImageView(activeExerciseImageView.getExerciseEnum());
            activeExerciseImageView = null;
            label.setText(" ");
            if (mainClass.getController().getPatient().getAmountOfExercises()==4) {
                mainClass.setScene(mainClass.getSettingsPaneExercise());
            }
        });
    }
    
    /* Initializes the label showing the name of the exercise when its image view
     * is highlighted*/
    private void initLabel() {
        label = new Label(" ");
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
        label.setTextFill(Color.WHITE);
        label.setMaxWidth(ProgramConstants.WINDOWWIDTH/6);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
    }
    
    /* Deletes the exercise Image view determined by the exercise enum*/
    public void deleteExerciseImageView(Exercises e) {
        for (ExerciseImageView eIV : exerciseImageList) {
            if(eIV.getExerciseEnum().equals(e)) {
                exerciseImageList.remove(eIV);
                flowPane.getChildren().remove(eIV);; 
                break;
            }
        }
    }
    
    /* Adds new exercise from the exercise enum. This is an exercise
     * from that has just recently been deleted*/
    public void  addExercise(Exercises e) {
        activateImagePDL(e);
    }
    
    /* Sets new geometric values for the components that listens to the underlying
     * pane when that pane changes its width*/
    public void fixWidthOfNewExercisePane(double value) {
        an.setMaxWidth(value/1.5);
        an.setMinWidth(value/1.5);
        borderPane.setMinWidth(value);
        borderPane.setMaxWidth(value);
        exerciseControlButton.setMinWidth(value/6);
        exerciseControlButton.setMaxWidth(value/6);
        label.setMaxWidth(value/6);
    }
    
    /* Sets new geometric values for the components that listens to the underlying
     * pane when that pane changes its height*/
    public void fixHeightOfNewExercisePane(double value) {
        an.setMaxHeight(value/1.3);
        an.setMinHeight(value/1.3);
        borderPane.setMinHeight(value);
        borderPane.setMaxHeight(value);
        exerciseControlButton.setMinHeight(value/10);
        exerciseControlButton.setMaxHeight(value/10);
    }
    
    /* Returns the return button*/
    public Button getReturnButton() {
        return borderPane.getReturnButton();
    }
    
    /* Returns the new exercise button*/
    public Button getNewExercisesButton() {
        return exerciseControlButton;
    }
    
}
