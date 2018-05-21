package View;

import Model.Exercises;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.shape.Rectangle;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * @author joakimstoor
 */

/* Defines a StackPane consisting of an image view representing a specific exercise*/
public class ExerciseImageView extends MyStackPane {
   
    private Exercises exerciseEnum;
    private ImageView imageview;
    private Rectangle rectangle;
    boolean isPatient;
    
    /* Inits a stack pane consisting of an imageView with an a specific exercise
     * image. Also looks if the stack pane is used in the patient view. If it is, 
      * it can be used for scheduling to. */
    public ExerciseImageView(Exercises exerciseEnum, Image image, double x,
            double y, Pane parentPane, boolean isPatient) {
        super(parentPane, x, y); // this is a StackPane extending MyStackPane
        this.exerciseEnum = exerciseEnum;
        this.isPatient = isPatient;
        imageview = new ImageView();
        initializeImageView(image, x, y, parentPane);
        
        this.getChildren().add(rectangle); // Rectangle is in the background
        this.getChildren().add(imageview);
    }
    
    /* Initializes the image view*/
    private void initializeImageView(Image image, double widthFraction, 
            double heightFraction, Pane parentPane) { 
            
        // Calculated and tested fraction, works fine
        double height = (3/2.5)*heightFraction;
        double width = (3/2.5)*widthFraction;
        
        /* A rectangle is used in the background of the stackpane to be able to 
         * highlight it when pressed if !isPatient*/
        rectangle = new Rectangle();
        rectangle.heightProperty().bind(
                    parentPane.heightProperty().divide(heightFraction));
        rectangle.widthProperty().bind(
                    parentPane.widthProperty().divide(widthFraction));
        rectangle.setFill(ProgramConstants.PATIENTOUTLINECOLOR);
        rectangle.setStroke(ProgramConstants.PATIENTBACKGROUNDCOLOR);
        
        if (!isPatient) { this.setStyle("-fx-border-color: black"); }
               
        imageview.setImage(image);
        imageview.setPreserveRatio(true); 
        
        imageview.fitHeightProperty().bind(parentPane.heightProperty().divide(height));
        imageview.fitWidthProperty().bind(parentPane.widthProperty().divide(width));
        
        imageview.setSmooth(true);
        imageview.setCache(true);
    }
    
    /* Returns the imageView stored in this stack pane*/
    public ImageView getImageView() {
        return imageview;
    }
    
    /* Returns the rectangle stored in this stack pane*/
    public Rectangle getRectangle() {
        return rectangle;
    }
    
    /* Returns the exercise enum of this stack pane*/
    public Exercises getExerciseEnum() {
        return this.exerciseEnum;
    }
    
}
