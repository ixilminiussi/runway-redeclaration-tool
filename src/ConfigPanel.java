import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigPanel extends ScrollPane {

    private ComboBox<Runway> runwayPresetCombo;
    private ComboBox<Obstruction> obstPresetCombo;
    private Label runwayConfigText, runwayText, nameText, airportText, TORAText, TODAText,
            ASDAText, LDAText, displacedText, stripEndText, egrText, resaText,
            blastText, stopwayText, clearwayText, obstConfigText, obstText, obstName, obstHeightText,
            obstLengthText, distThreshText, distCentreText;
    private TextField nameTextField, airportTextField, TORATextField,
            TODATextField, ASDATextField, LDATextField, displacedTextField,
            stripEndTextField, egrTextField, resaTextField, blastTextField,
            stopwayTextField, clearwayTextField, obstNameTextField, obstHeightTextField,
            obstLengthTextField, distThreshTextField, distCentreTextField;
    private VBox configWindow;
    private Button saveRunwayPreset, removeRunwayPreset, applyRunway, saveObstPreset, removeObstPreset, applyObst;
    private ArrayList<TextField> runwayIntTextFields, runwayStringTextFields, obstIntTextFields, optionalFields;
    private ArrayList<Runway> presetRunways;
    private ArrayList<Obstruction> presetObstructions;
    private HistoryPanel historyPanel;
    private String invalidText;

    ConfigPanel(HistoryPanel historyPanel) {
        this.historyPanel = historyPanel;
        populateDefaults();
        setupScrollPane();
        createLabels();
        createTextFields();
        createComboBoxes();
        createButtons();
        addFieldsToConfig();


        runwayIntTextFields = new ArrayList<>(Arrays.asList(TORATextField,
                TODATextField, ASDATextField, LDATextField, displacedTextField,
                stripEndTextField, egrTextField, resaTextField, blastTextField,
                stopwayTextField, clearwayTextField));
        runwayStringTextFields = new ArrayList<>(Arrays.asList(nameTextField,
                airportTextField));
        obstIntTextFields = new ArrayList<>(Arrays.asList(obstHeightTextField,
                obstLengthTextField, distThreshTextField, distCentreTextField));
        optionalFields = new ArrayList<>(Arrays.asList(stripEndTextField,
                egrTextField, resaTextField, blastTextField,
                stopwayTextField, clearwayTextField));
    }

    // return AffectedRunway object of the current configuration
    public AffectedRunway getAffectedRunway() {

        Runway runway = getRunway();
        Obstruction obstruction = getObstruction();
        if(runway == null || obstruction == null) {
            return null;
        } else {
            return runway.recalculate(obstruction);
        }
    }

    // return currently outlined obstruction object
    public Obstruction getObstruction() {

        if(!areFieldsValid()) {
            return null;
        }

        ArrayList<Integer> obstValues = new ArrayList<>();
        for (TextField textField : obstIntTextFields) {
            String val = textField.textProperty().getValue();
            obstValues.add(Integer.parseInt(val));
        }
        String name = obstNameTextField.textProperty().getValue();
        return new Obstruction(name, obstValues.get(0), obstValues.get(1),
                obstValues.get(2), obstValues.get(3));
    }

    // return currently outlined runway object
    // TODO add proper support for defaults/optional fields (if blank use defaults)
    public Runway getRunway() {
    	

        if(!areFieldsValid()) {
            return null;
        }
        
        ArrayList<Integer> runwayValues = new ArrayList<>();
        for (TextField textField : runwayIntTextFields) {
            String val = textField.textProperty().getValue();
            runwayValues.add(Integer.parseInt(val));
        }

        String name = nameTextField.textProperty().getValue();

        String airport = airportTextField.textProperty().getValue();
        return new Runway(name, airport, runwayValues.get(0),
                runwayValues.get(1), runwayValues.get(2), runwayValues.get(3),
                runwayValues.get(4), runwayValues.get(5), runwayValues.get(6),
                runwayValues.get(7), runwayValues.get(8), runwayValues.get(9),
                runwayValues.get(10));
    }

    // check if the config currently has any invalid fields
    public Boolean areFieldsValid() {
        try {
        	
        	//Rules for input
        	int tora = Integer.parseInt(runwayIntTextFields.get(0).textProperty().getValue());
        	int toda = Integer.parseInt(runwayIntTextFields.get(1).textProperty().getValue());
        	int asda = Integer.parseInt(runwayIntTextFields.get(2).textProperty().getValue());
        	int stopway = Integer.parseInt(runwayIntTextFields.get(9).textProperty().getValue());
        	int clearway = Integer.parseInt(runwayIntTextFields.get(10).textProperty().getValue());
        	
        	if (tora < 400) {
        		invalidText = "TORA below minimum value";
        		return false;
        	}
        	
        	if (tora > 5000) {
        		invalidText = "TORA above maximum value";
        		return false;
        	}
        	
        	/**
        	//TODA = TORA + Clearway
        	if (toda != tora + clearway) {
        		invalidText = "TODA not equal to TORA + Clearway";
        		return false;
        	}
        	
        	//ASDA = TORA + Stopway
        	if (asda != tora + stopway) {
        		invalidText = "ASDA not equal to TORA + Stopway";
        		return false;
        	}
        	
        	**/

            for (TextField textField : obstIntTextFields) {
                String val = textField.textProperty().getValue();
                if (textField == obstHeightTextField || textField == obstLengthTextField) {
                    int value = Integer.parseInt(val);
                    if (value <= 0 || value >= 999999) {
                    	invalidText = "Object height or length cannot be negative";
                        return false;
                    }
                }
                else {
                    int value = Integer.parseInt(val);
                    if (value >= 999999) {
                    	invalidText = "Distance from threshold or centre line above maximum allowed value";
                        return false;
                    }
                }
            }
            
            for (TextField textField : runwayIntTextFields) {
        		String val = textField.textProperty().getValue();
            	if (!(val.equals("") && (optionalFields.contains(textField)))) {
            		int value = Integer.parseInt(val);
            		if (value < 0 || value >= 999999) {
            			invalidText = "One of the runway configuration values is negative or above the maximum allowed value";
            			return false;
            		}
                }
            }
            
            for (TextField textField : runwayStringTextFields) {
                String val = textField.textProperty().getValue();
                if (val.length() <= 0 || val.length() >= 32) { // arbitrary limit to name size
                	invalidText = "Invalid Airport name";
                    return false;
                }
            }

            String runName = nameTextField.textProperty().getValue();
            try {
                int deg = Integer.parseInt(runName + "0");
                if(deg < 0 || deg > 360) {
                	invalidText = "Invalid Runway name";
                    return false;
                }
            } catch (NumberFormatException e) {
                try {
                    int deg = Integer.parseInt(runName.substring(0, 2) + "0");
                    if(deg < 0 || deg > 360) {
                    	invalidText = "Invalid Runway name";
                        return false;
                    }
                    if (runName.length() > 3) {
                    	invalidText = "Invalid Runway name";
                    	return false;
                    }
                    String letter = runName.substring(2);
                    if (runName.length() == 3 && !(letter.equals("L") || letter.equals("R") || letter.equals("C"))) {
                    	invalidText = "Invalid Runway name";
                    	return false;
                    }
                    
                } catch (NumberFormatException e2) {
                	invalidText = "Invalid Runway name";
                    return false;
                }
            }

            String obstName = obstNameTextField.textProperty().getValue();
            return obstName.length() > 0 && obstName.length() < 64;
        } catch (NumberFormatException e) {
        	invalidText = "non integer input in a field requiring an integer";
            return false;
        }
    }

    // setup actual panel
    private void setupScrollPane() {
        this.setFitToWidth(true);
        configWindow = new VBox();
        this.setContent(configWindow);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setPadding(new Insets(5));
    }

    // initialise all the labels for fields
    private void createLabels() {
        runwayConfigText = new Label("Runway Configuration");
        Font defaultFont = Font.getDefault();
        runwayConfigText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, 15));
        runwayConfigText.setPadding(new Insets(10));

        runwayText = new Label("Runway: ");
        nameText = new Label("Name: ");
        airportText = new Label("Airport: ");
        TORAText = new Label("TORA: ");
        TODAText = new Label("TODA: ");
        ASDAText = new Label("ASDA: ");
        LDAText = new Label("LDA: ");
        displacedText = new Label("Displaced Threshold: ");
        stripEndText = new Label("Strip End: ");
        egrText = new Label("EGR: ");
        resaText = new Label("RESA: ");
        blastText = new Label("Blast Allowance: ");
        stopwayText = new Label("Stopway: ");
        clearwayText = new Label("Clearway: ");

        obstConfigText = new Label("Obstacle Configuration");
        obstConfigText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, 15));
        obstConfigText.setPadding(new Insets(10));

        obstText = new Label("Obstacle: ");
        obstName = new Label("Name: ");
        obstHeightText = new Label("Height: ");
        obstLengthText = new Label("Length: ");
        distThreshText = new Label("Distance from Threshold: ");
        distCentreText = new Label("Distance from Centreline: ");
    }

    // initialise all the textfields
    private void createTextFields() {
        nameTextField = new TextField();
        airportTextField = new TextField();
        TORATextField = new TextField();
        TODATextField = new TextField();
        ASDATextField = new TextField();
        LDATextField = new TextField();
        displacedTextField = new TextField();
        stripEndTextField = new TextField();
        egrTextField = new TextField();
        resaTextField = new TextField();
        blastTextField = new TextField();
        stopwayTextField = new TextField();
        clearwayTextField = new TextField();

        obstNameTextField = new TextField();
        obstHeightTextField = new TextField();
        obstLengthTextField = new TextField();
        distThreshTextField = new TextField();
        distCentreTextField = new TextField();
    }

    // initialise buttons
    private void createButtons() {
        saveRunwayPreset = new Button("Save Runway as Preset");
        saveRunwayPreset.setOnMouseClicked((event) -> {
            Runway runway = getRunway();
            if(runway == null) {
                configError("One of the parameters in the runway or obstruction was invalid: " + invalidText);
            } else {
                presetRunways.add(runway);
                runwayPresetCombo.getItems().add(runway);
                runwayPresetCombo.setValue(runway);
                populateRunwayFields(runway);
                try {
                    exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
                } catch (Exception e) {
                    configError("The file could not be exported.\n" + e);
                }
                historyPanel.addHistoryEntry("PRESETS: added " + runway.getName() + " - " + runway.getAirport() + " to presets");
            }
        });
        removeRunwayPreset = new Button("Delete Current Runway Preset");
        removeRunwayPreset.setOnMouseClicked((event) -> {
            Runway runway = getRunway();
            if(runway == null) {
                configError("One of the parameters in the runway or obstruction was invalid: " + invalidText);
            } else {
                Runway toRemove = getRunwayFromName(runway.getName(), runway.getAirport());
                if (toRemove == null) {
                    configError("This runway is not saved!");
                } else {
                    presetRunways.remove(toRemove);
                    runwayPresetCombo.getItems().remove(toRemove);
                    runwayPresetCombo.setValue(runwayPresetCombo.getItems().get(0));
                    populateRunwayFields(runwayPresetCombo.getValue());
                    try {
                        exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
                        historyPanel.addHistoryEntry("PRESETS: " + runway.getName() + " - " + runway.getAirport() + " removed from presets");
                    } catch (Exception e) {
                        configError("The file could not be exported.\n" + e);
                    }
                }
            }
        });
        applyRunway = new Button("Render");

        saveObstPreset = new Button("Save Obstacle as Preset");
        saveObstPreset.setOnMouseClicked((event) -> {
            Obstruction obstruction = getObstruction();
            if(obstruction == null) {
                configError("One of the parameters in the runway or obstruction was invalid: "  + invalidText);
            } else {
                presetObstructions.add(obstruction);
                obstPresetCombo.getItems().add(obstruction);
                obstPresetCombo.setValue(obstruction);
                populateObstFields(obstruction);
                try {
                    exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
                    historyPanel.addHistoryEntry("PRESETS: " + obstruction.getName() + " added to presets");
                } catch (Exception e) {
                    configError("The file could not be exported.\n" + e);
                }
            }
        });
        removeObstPreset = new Button("Delete Current Obstacle Preset");
        removeObstPreset.setOnMouseClicked((event) -> {
            Obstruction obstruction = getObstruction();
            if(obstruction == null) {
                configError("One of the parameters in the runway or obstruction was invalid: "  + invalidText);
            } else {
                Obstruction toRemove = getObstructionFromName(obstruction.getName());
                if (toRemove == null) {
                    configError("This obstruction is not saved!");
                } else {
                    presetObstructions.remove(toRemove);
                    obstPresetCombo.getItems().remove(toRemove);
                    obstPresetCombo.setValue(obstPresetCombo.getItems().get(0));
                    populateObstFields(obstPresetCombo.getValue());
                    try {
                        exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
                        historyPanel.addHistoryEntry("PRESETS: " + obstruction.getName() + " removed from presets");
                    } catch (Exception e) {
                        configError("The file could not be exported.\n" + e);
                    }
                }
            }
        });
        applyObst = new Button("Render");
    }

    private Runway getRunwayFromName(String name, String airport) {
        for(Runway runway : presetRunways) {
            if (runway.getName().equals(name) && runway.getAirport().equals(airport)) {
                return runway;
            }
        }
        return null;
    }

    private Obstruction getObstructionFromName(String name) {
        for(Obstruction obstruction : presetObstructions) {
            if (obstruction.getName().equals(name)) {
                return obstruction;
            }
        }
        return null;
    }

    public void addRunwaysFromXML(ArrayList<Runway> runways) {
        for(Runway runway : runways) {
            Runway exists = getRunwayFromName(runway.getName(), runway.getAirport());
            if(exists == null) {
                presetRunways.add(runway);
                runwayPresetCombo.getItems().add(runway);
            }
        }
        try {
            exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
        } catch (Exception e) {
            configError("The file could not be exported.\n" + e);
        }
        runwayPresetCombo.setValue(presetRunways.get(0));
        populateRunwayFields(runwayPresetCombo.getValue());
    }

    public void addObstructionsFromXML(ArrayList<Obstruction> obstructions) {
        for(Obstruction obstruction : obstructions) {
            Obstruction exists = getObstructionFromName(obstruction.getName());
            if(exists == null) {
                presetObstructions.add(obstruction);
                obstPresetCombo.getItems().add(obstruction);
            }
        }
        try {
            exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
        } catch (Exception e) {
            configError("The file could not be exported.\n" + e);
        }
        obstPresetCombo.setValue(presetObstructions.get(0));
        populateObstFields(obstPresetCombo.getValue());
    }

    public void clearPresets() {
        runwayPresetCombo.getItems().removeAll(presetRunways);
        obstPresetCombo.getItems().removeAll(presetObstructions);
        presetObstructions = new ArrayList<>();
        presetRunways = new ArrayList<>();
        try {
            exportXML.exportBothToXML("src/xml/presets.xml", presetObstructions, presetRunways);
        } catch (Exception e) {
            configError("The file could not be exported.\n" + e);
        }
        clearTextFields();
    }

    private void clearTextFields() {
        nameTextField.textProperty().setValue("");
        airportTextField.textProperty().setValue("");
        TORATextField.textProperty().setValue("");
        TODATextField.textProperty().setValue("");
        ASDATextField.textProperty().setValue("");
        LDATextField.textProperty().setValue("");
        displacedTextField.textProperty().setValue("");
        stripEndTextField.textProperty().setValue("");
        egrTextField.textProperty().setValue("");
        resaTextField.textProperty().setValue("");
        blastTextField.textProperty().setValue("");
        stopwayTextField.textProperty().setValue("");
        clearwayTextField.textProperty().setValue("");
        obstNameTextField.textProperty().setValue("");
        obstHeightTextField.textProperty().setValue("");
        obstLengthTextField.textProperty().setValue("");
        distThreshTextField.textProperty().setValue("");
        distCentreTextField.textProperty().setValue("");
    }

    // initialise comboboxes
    private void createComboBoxes() {

        runwayPresetCombo = new ComboBox<>();
        runwayPresetCombo.setPrefWidth(400);
        for (Runway runway : presetRunways) {
            runwayPresetCombo.getItems().add(runway);
        }
        runwayPresetCombo.setOnAction((event) -> {
            Runway runway = runwayPresetCombo.getValue();
            try {
                populateRunwayFields(runway);
            } catch (NullPointerException e) {
                System.out.println("No presets available");
            }
        });

        if(presetRunways.size() > 0) {
            runwayPresetCombo.setValue(presetRunways.get(0));
            populateRunwayFields(runwayPresetCombo.getValue());
        }

        obstPresetCombo = new ComboBox<>();
        obstPresetCombo.setPrefWidth(400);
        for (Obstruction obstruction : presetObstructions) {
            obstPresetCombo.getItems().add(obstruction);
        }
        obstPresetCombo.setOnAction((event) -> {
            Obstruction obstruction = obstPresetCombo.getValue();
            try {
                populateObstFields(obstruction);
            } catch (NullPointerException e) {
                System.out.println("No presets available");
            }
        });

        if(presetObstructions.size() > 0) {
            obstPresetCombo.setValue(presetObstructions.get(0));
            populateObstFields(obstPresetCombo.getValue());
        }

    }

    // fill in textfields with specific values from selected runway
    private void populateRunwayFields(Runway runway) {
        nameTextField.textProperty().setValue(runway.getName());
        airportTextField.textProperty().setValue(runway.getAirport());
        TORATextField.textProperty().setValue(Integer.toString(runway.getTORA()));
        TODATextField.textProperty().setValue(Integer.toString(runway.getTODA()));
        ASDATextField.textProperty().setValue(Integer.toString(runway.getASDA()));
        LDATextField.textProperty().setValue(Integer.toString(runway.getLDA()));
        displacedTextField.textProperty().setValue(Integer.toString(runway.getDisplacedThreshold()));
        stripEndTextField.textProperty().setValue(Integer.toString(runway.getStripEnd()));
        egrTextField.textProperty().setValue(Integer.toString(runway.getEGR()));
        resaTextField.textProperty().setValue(Integer.toString(runway.getRESA()));
        blastTextField.textProperty().setValue(Integer.toString(runway.getBlastAllowance()));
        stopwayTextField.textProperty().setValue(Integer.toString(runway.getStopway()));
        clearwayTextField.textProperty().setValue(Integer.toString(runway.getClearway()));
    }

    // fill in textfields with values of a specific obstruction
    private void populateObstFields(Obstruction obstruction) {
        obstNameTextField.textProperty().setValue(obstruction.getName());
        obstHeightTextField.textProperty().setValue(Integer.toString(obstruction.getHeight()));
        obstLengthTextField.textProperty().setValue(Integer.toString(obstruction.getLength()));
        distThreshTextField.textProperty().setValue(Integer.toString(obstruction.getDistanceFromThreshold()));
        distCentreTextField.textProperty().setValue(Integer.toString(obstruction.getDistanceFromCentre()));
    }

    // initialise config panel by adding all the fields
    private void addFieldsToConfig() {
        GridPane runwayConfigPane = new GridPane();
        runwayConfigPane.setPadding(new Insets(10));
        runwayConfigPane.setBorder(new Border(new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayConfigPane.setVgap(20);
        runwayConfigPane.setHgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        col1.setPercentWidth(25);
        col1.setHalignment(HPos.RIGHT);
        col2.setPercentWidth(25);
        col3.setPercentWidth(25);
        col3.setHalignment(HPos.RIGHT);
        col4.setPercentWidth(25);
        runwayConfigPane.getColumnConstraints().addAll(col1, col2, col3, col4);

        runwayConfigPane.add(runwayText, 0, 0);
        runwayConfigPane.add(runwayPresetCombo, 1, 0, 3, 1);
        runwayConfigPane.add(nameText, 0, 1);
        runwayConfigPane.add(nameTextField, 1, 1);
        runwayConfigPane.add(airportText, 2, 1);
        runwayConfigPane.add(airportTextField, 3, 1);
        runwayConfigPane.add(TORAText, 0,2);
        runwayConfigPane.add(TORATextField, 1, 2);
        runwayConfigPane.add(TODAText, 2, 2);
        runwayConfigPane.add(TODATextField, 3, 2);
        runwayConfigPane.add(ASDAText, 0, 3);
        runwayConfigPane.add(ASDATextField, 1, 3);
        runwayConfigPane.add(LDAText, 2, 3);
        runwayConfigPane.add(LDATextField, 3, 3);
        runwayConfigPane.add(displacedText, 0, 4);
        runwayConfigPane.add(displacedTextField, 1, 4);

        GridPane optionalGridPane = new GridPane();
        optionalGridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
        optionalGridPane.setPadding(new Insets(10));
        optionalGridPane.setVgap(20);
        optionalGridPane.setHgap(10);

        optionalGridPane.add(stripEndText, 0, 0);
        optionalGridPane.add(stripEndTextField, 1, 0);
        optionalGridPane.add(egrText, 2, 0);
        optionalGridPane.add(egrTextField, 3, 0);
        optionalGridPane.add(resaText, 0, 1);
        optionalGridPane.add(resaTextField, 1, 1);
        optionalGridPane.add(blastText, 2, 1);
        optionalGridPane.add(blastTextField, 3, 1);
        optionalGridPane.add(stopwayText, 0, 2);
        optionalGridPane.add(stopwayTextField, 1, 2);
        optionalGridPane.add(clearwayText, 2, 2);
        optionalGridPane.add(clearwayTextField, 3, 2);

        TitledPane optionals = new TitledPane("Extra Parameters", optionalGridPane);
        runwayConfigPane.add(optionals, 0, 5, 4, 1);
        runwayConfigPane.add(saveRunwayPreset, 0, 6);
        runwayConfigPane.add(removeRunwayPreset, 1, 6, 2, 1);
        runwayConfigPane.add(applyRunway, 3, 6);
        optionals.setExpanded(false);

        GridPane obstConfigPane = new GridPane();
        obstConfigPane.setPadding(new Insets(10));
        obstConfigPane.setBorder(new Border(new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        obstConfigPane.setVgap(20);
        obstConfigPane.setHgap(10);
        obstConfigPane.getColumnConstraints().addAll(col1, col2, col3, col4);

        obstConfigPane.add(obstText, 0, 0);
        obstConfigPane.add(obstPresetCombo, 1, 0, 3, 1);
        obstConfigPane.add(obstName, 0, 1);
        obstConfigPane.add(obstNameTextField, 1, 1, 3, 1);
        obstConfigPane.add(obstHeightText, 0, 2);
        obstConfigPane.add(obstHeightTextField, 1, 2);
        obstConfigPane.add(obstLengthText, 2, 2);
        obstConfigPane.add(obstLengthTextField, 3, 2);
        obstConfigPane.add(distThreshText, 0, 3);
        obstConfigPane.add(distThreshTextField, 1, 3);
        obstConfigPane.add(distCentreText, 2, 3);
        obstConfigPane.add(distCentreTextField, 3, 3);
        obstConfigPane.add(saveObstPreset, 0, 4);
        obstConfigPane.add(removeObstPreset, 1, 4, 2, 1);
        obstConfigPane.add(applyObst, 3, 4);

        configWindow.getChildren().addAll(runwayConfigText, runwayConfigPane, obstConfigText, obstConfigPane);
    }

    // temp function, fill arraylists of presets
    private void populateDefaults() {
        try {
            importXML importXML = new importXML("src/xml/presets.xml");
            presetRunways = importXML.importRunwaysFromXML();
            presetObstructions = importXML.importObstructionsFromXML();
        } catch (Exception e) {
            System.out.println(e);
            presetRunways = new ArrayList<>();
            presetObstructions = new ArrayList<>();
        }
    }

    private void configError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Configuration Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public Button getSaveRunwayPreset() {
        return saveRunwayPreset;
    }

    public Button getApplyRunway() {
        return applyRunway;
    }

    public Button getSaveObstPreset() {
        return saveObstPreset;
    }

    public Button getApplyObst() {
        return applyObst;
    }

    public ArrayList<Obstruction> getPresetObstructions() {
        return presetObstructions;
    }

    public ArrayList<Runway> getPresetRunways() {
        return presetRunways;
    }

	public ArrayList<TextField> getRunwayIntTextFields() {
		return runwayIntTextFields;
	}
	
	public ArrayList<TextField> getRunwayStringTextFields() {
		return runwayStringTextFields;
	}
	
	public ArrayList<TextField> getObstIntTextFields() {
		return obstIntTextFields;
	}
	
	public ArrayList<TextField> getOptionalFields() {
		return optionalFields;
	}
	
	public String getInvalidText() {
		return invalidText;
	}
}
