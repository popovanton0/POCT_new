package sample;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * Created by popov on 13.04.2016.
 */
public class SplashScreen extends Stage {

    public SplashScreen(Stage owner) {
        super();
        initOwner(owner);
        setTitle("title");
        setOpacity(.90);
        Group root = new Group();
        Scene scene = new Scene(root, 250, 150, Color.WHITE);
        setScene(scene);

        Pane pane = new Pane(new ImageView(new Image(Main.class.getResourceAsStream("icons/splashscreen.png"))));
        root.getChildren().add(pane);

    }
}
