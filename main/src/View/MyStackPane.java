/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author joakimstoor
 */

/**
 * A stackPane that retains a specified fraction of a parent panes size
 */
public class MyStackPane extends StackPane {
    
    protected final Pane parentPane;
        
    /* Initializes a stackpane with specified width (x) and height (y) fractions
     * of the parent pane*/
    public MyStackPane(Pane parentPane, double x, double y) {
        
        this.parentPane = parentPane;
        
        // Min and max operations sets the size for the stackpane
        this.setMaxSize(parentPane.getWidth()/x, parentPane.getHeight()/y);
        this.setMinSize(parentPane.getWidth()/x, parentPane.getHeight()/y);
        // Transparent background and grey borders
        this.setStyle("-fx-background-color: transparent; -fx-border-color: grey;");
        
        /* Listeners two change the size of the stackpane when the parent pane 
         * changes its size*/
        parentPane.widthProperty().addListener(e -> {
            this.setMaxWidth(parentPane.getWidth()/x);
            this.setMinWidth(parentPane.getWidth()/x);
        });
        parentPane.heightProperty().addListener(e -> {
            this.setMaxHeight(parentPane.getHeight()/y);
            this.setMinHeight(parentPane.getHeight()/y); 
        });
    }
}
