		/*@author enick*/

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.HashMap;
import java.util.Map;


//JavaFX calculator class.
public class Calculator extends Application {
  
  //creating and designing a calculator application template
	private static final String[][] template = {
	{ "MC", "MR", "M+", "M-" },
	{ "%", "Srq", "X2", "1/x" },
	{ "CE", "c", "Del", "/" },
	{ "7", "8", "9", "*" },
	{ "4", "5", "6", "-" },
	{ "1", "2", "3", "+" },
	{ "±", "0", ".", "=" }
};


private final Map<String, Button> accelerators = new HashMap<>();

// Create and initialize stack value
private DoubleProperty stackValue = new SimpleDoubleProperty();

// Create and initialize a value
private DoubleProperty value = new SimpleDoubleProperty();

private enum Op { ENICK, ADD, SUBTRACT, MULTIPLY, DIVIDE }

private Op curOp = Op.ENICK;

private Op stackOp = Op.ENICK;


public static void main(String[] args) { launch(args); }

@Override // Override the start method in the Application class

public void start(Stage primaryStage) {
final TextField screen = screen();
final TilePane buttons = buttons();
primaryStage.setTitle("Enick's Calculator");
primaryStage.initStyle(StageStyle.UTILITY);
primaryStage.setResizable(false); /*setting and stricting the stage to be non resizable*/
primaryStage.setScene(new Scene(createLayout(screen, buttons)));
primaryStage.show();
}


private VBox createLayout(TextField screen, TilePane buttons) {

final VBox layout = new VBox(40); //creating a new vertical box layout

//setting a position of alignment and style of a calculator application
layout.setAlignment(Pos.CENTER);
layout.setStyle("-fx-background-color: BLACK; -fx-padding: 15; -fx-font-size: 20;");
layout.getChildren().setAll(screen, buttons);
handleAccelerators(layout);
screen.prefWidthProperty().bind(buttons.widthProperty());
return layout;
}

	/* Handle an accelerator event */
private void handleAccelerators(VBox layout) {
layout.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

@Override

public void handle(KeyEvent keyEvent) {
Button activated = accelerators.get(keyEvent.getText()); //getting the text or input
if (activated != null) {
activated.fire();
}
}
});
}


private TextField screen() {
final TextField screen = new TextField(); //creating screen textfield constant
screen.setStyle("-fx-background-color: WHITE;");
screen.setAlignment(Pos.CENTER_RIGHT);
screen.setEditable(false); //setting the screen not to be edited
screen.textProperty().bind(Bindings.format("%.0f", value));
return screen;
}

//creating the buttons and assigning their properties
private TilePane buttons() {
TilePane buttons = new TilePane();
buttons.setVgap(10);
buttons.setHgap(10);
buttons.setPrefColumns(template[0].length);
for (String[] r: template) {
for (String s: r) {
buttons.getChildren().add(createButton(s)); //placing the buttons in the scene
}
}
return buttons;
}


private Button createButton(final String s) {
Button button = makeStandardButton(s);
if (s.matches("[0-9]")) { /*if statement*/
makeNumericButton(s, button);
} else {
final ObjectProperty<Op> startOp = determineOperand(s);
if (startOp.get() != Op.ENICK) {
makeOperandButton(button, startOp);
} else if ("c".equals(s)) { //else if statement
makeClearButton(button);
} else if ("=".equals(s)) { //else if statement
makeEqualsButton(button);
}
}
return button;
}


private ObjectProperty<Op> determineOperand(String s) {
final ObjectProperty<Op> startOp = new SimpleObjectProperty<>(Op.ENICK);

//switch statements
switch (s) {
case "+": startOp.set(Op.ADD); break;
case "-": startOp.set(Op.SUBTRACT); break;
case "*": startOp.set(Op.MULTIPLY); break;
case "/": startOp.set(Op.DIVIDE); break;
}
return startOp;
}


private void makeOperandButton(Button button, final ObjectProperty<Op> startOp) {
button.setStyle("-fx-base: lightgray;");
button.setOnAction(new EventHandler<ActionEvent>() {

@Override

public void handle(ActionEvent actionEvent) {
curOp = startOp.get();
}
});
}


private Button makeStandardButton(String s) {
Button button = new Button(s);
button.setStyle("-fx-base: beige;");
accelerators.put(s, button);
button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
return button;
}


private void makeNumericButton(final String s, Button button) {
button.setOnAction(new EventHandler<ActionEvent>() {
@Override
public void handle(ActionEvent actionEvent) {
if (curOp == Op.ENICK) {
value.set(value.get() * 10 + Integer.parseInt(s));
} else {
stackValue.set(value.get());
value.set(Integer.parseInt(s));
stackOp = curOp;
curOp = Op.ENICK;
}
}
});
}

	//creating a clear button
private void makeClearButton(Button button) {
button.setStyle("-fx-base: mistyrose;"); //setting button style to be mistyrose

button.setOnAction(new EventHandler<ActionEvent>() { /*setting an action for clear button*/

@Override

public void handle(ActionEvent actionEvent) { //handle clear event
value.set(0);
}
});
}

//cleating an equal (=) button and setting its attributes
private void makeEqualsButton(Button button) {
button.setStyle("-fx-base: ghostwhite;");
button.setOnAction(new EventHandler<ActionEvent>() {


@Override
public void handle(ActionEvent actionEvent) { //handle equal button event

	//Switch statements
switch (stackOp) {
case ADD: value.set(stackValue.get() + value.get()); break;
case SUBTRACT: value.set(stackValue.get() - value.get()); break;
case MULTIPLY: value.set(stackValue.get() * value.get()); break;
case DIVIDE: value.set(stackValue.get() / value.get()); break;
}
}
});
}
}
