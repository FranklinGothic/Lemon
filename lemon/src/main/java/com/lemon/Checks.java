package com.lemon;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;

public class Checks {
    private Lemon lemonObj;
    private Stage stage;

    private ComboBox<String> checkKind;
    private ComboBox<String> checkTypes;
    
    private ArrayList<String> kindsArrayList;
    private ArrayList<String> typeArrayList;
    private ArrayList<Map<Label, TextField>> checkParamsArray;

    public Checks(Lemon lemonObj, Stage stage) {
        this.lemonObj = lemonObj;
        this.stage = stage;
    }

    public void handle() {
        kindsArrayList = new ArrayList<>();
        typeArrayList = new ArrayList<>();
        checkParamsArray = new ArrayList<>();

        Button addVuln = new Button("Add Vulnerability");
        VBox root = new VBox(addVuln);
        Scene addVulnerability = new Scene(root, 500, 500);

        EventHandler<ActionEvent> addButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                //On add vulnerability button pressed calls handleCheck method
                handleCheck();
            }
        };

        addVuln.setOnAction(addButtonEvent);
        stage.setScene(addVulnerability);
    }

    public void handleCheck() {
        LinkedHashMap<String, String[]> checksMap = new LinkedHashMap<>(); // Adds all the possible types of checks
        checksMap.put("CommandContains", new String[]{"cmd", "value"});// based off of OS
        checksMap.put("CommandOutput", new String[]{"cmd", "value"});
        checksMap.put("DirContains", new String[]{"path", "value"});
        checksMap.put("FileContains", new String[]{"path", "value"});
        checksMap.put("FileEquals", new String[]{"path", "value"});
        checksMap.put("FileOwner", new String[]{"path", "name"});
        checksMap.put("FirewallUp", new String[]{});
        checksMap.put("PathExists", new String[]{"path"});
        checksMap.put("ProgramInstalled", new String[]{"name"});
        checksMap.put("ProgramVersion", new String[]{"name", "value"});
        checksMap.put("ServiceUp", new String[]{"name"});
        checksMap.put("UserExists", new String[]{"name"});
        checksMap.put("UserInGroup", new String[]{"user", "group"});
        String os = lemonObj.getOs().toLowerCase();

        if (!(os.contains("windows"))) { // Checks if current OS is Linux 
            checksMap.put("PasswordChanged", new String[]{"user", "value"});
            checksMap.put("PermissionIs", new String[]{"path", "value"});
            checksMap.put("AutoCheckUpdatesEnabled", new String[]{});
            checksMap.put("Command", new String[]{"cmd"});
            checksMap.put("GuestDisabledLDM", new String[]{});
            checksMap.put("KernelVersion", new String[]{"value"});
        } else {
            checksMap.put("PasswordChanged", new String[]{"user", "after"});
            checksMap.put("PermissionIs", new String[]{"path", "name", "value"});
            checksMap.put("BitlockerEnabled", new String[]{});
            checksMap.put("FirewallDefaultBehavior", new String[]{"name", "value", "key"});
            checksMap.put("RegistryKeyExists", new String[]{"key"});
            checksMap.put("ScheduledTaskExists", new String[]{"name"});
            checksMap.put("SecurityPolicy", new String[]{"key", "value"});
            checksMap.put("ServiceStartup", new String[]{"name", "value"});
            checksMap.put("ShareExists", new String[]{"name"});
            checksMap.put("UserDetail", new String[]{"user","key", "value"});
            checksMap.put("UserRights", new String[]{"name", "value"});
            checksMap.put("WindowsFeature", new String[]{"name"});
        }

        VBox allChecksBox = new VBox();
        addCheckPass(checksMap, allChecksBox);

        Label message = new Label("message:");
        TextField messageField = new TextField();
        HBox hbMessage = new HBox();
        hbMessage.getChildren().addAll(message, messageField);
        hbMessage.setSpacing(10);

        Label points = new Label("points:");
        TextField pointsField = new TextField();
        HBox hbPoints = new HBox();
        hbPoints.getChildren().addAll(points, pointsField);
        hbPoints.setSpacing(10);


        Button add = new Button("+");
        EventHandler<ActionEvent> addButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                addCheckPass(checksMap, allChecksBox);
            }
        };
        add.setOnAction(addButtonEvent);

        Button done = new Button("Done");
        EventHandler<ActionEvent> doneButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                boolean empty = false;
                for (int i = 0; i < typeArrayList.size(); i++) {
                    if (kindsArrayList.get(i) == null || typeArrayList.get(i) == null) {
                        empty = true;
                    }
                }
                if (messageField.getText().isEmpty() || pointsField.getText().isEmpty() || empty) {
                    System.out.println("you suck");
                }
                else {
                    String message = messageField.getText();
                    int points = Integer.parseInt(pointsField.getText());
                    ArrayList<Map<String, String>> checkTransform = new ArrayList<>();

                    for (Map<Label, TextField> labelMap : checkParamsArray) {
                        Map<String, String> stringMap = new LinkedHashMap<>();
                        //every entry
                        for (Map.Entry<Label, TextField> entryMap : labelMap.entrySet()) {
                            stringMap.put(entryMap.getKey().getText(), entryMap.getValue().getText());
                        }
                        checkTransform.add(stringMap);
                    }
                    lemonObj.addCheck(message, points, kindsArrayList, typeArrayList, checkTransform);
                }
                Checks.this.handle();
            }
        };
        done.setOnAction(doneButtonEvent);

        VBox checkRoot = new VBox(done, hbMessage, hbPoints, allChecksBox, add);
        Scene sc = new Scene(checkRoot, 500, 500);
        stage.setScene(sc);
    }


    public void addCheckPass(LinkedHashMap<String, String[]> checksMap, VBox box) {
        String[] checkKinds = {"[[check.pass]]", "[[check.fail]]", "[[check.passoverride]]"};
        checkKind = new ComboBox<>();
        for (String kind : checkKinds) {
            checkKind.getItems().add(kind);
        }
        checkTypes = new ComboBox<>();
        CheckBox notBox = new CheckBox("Not");
        for (String type : checksMap.keySet()) {
            checkTypes.getItems().add(type);
        }

        VBox paramsBox = new VBox();

        EventHandler<ActionEvent> dropTypesEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            typeArrayList.add(checkTypes.getValue());
            paramsBox.getChildren().clear();
            String[] params = checksMap.get(checkTypes.getValue());
            if (params != null) {
                //new map for aech set/chunk of params (Label, TextField)
                Map<Label, TextField> checkParams = new LinkedHashMap<>();
                for (String poramValue : params) {
                    Label poram = new Label(poramValue + ":");
                    TextField poramField = new TextField();
                    HBox hbporam = new HBox();
                    hbporam.getChildren().addAll(poram, poramField);
                    hbporam.setSpacing(10);
                    paramsBox.getChildren().add(hbporam);
                    checkParams.put(poram, poramField);
                }
                //add poorm map to the array
                checkParamsArray.add(checkParams);
            }
        }
    };
            EventHandler<ActionEvent> dropKindEvent = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    kindsArrayList.add(checkKind.getValue());
                }
            }; 

            EventHandler<ActionEvent> notBoxEvent = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    if(notBox.isSelected()) {
                        typeArrayList.set(typeArrayList.size() - 1, typeArrayList.get(typeArrayList.size() - 1) + "Not");
                    } else { // ["ProgramVersionNot"]
                        typeArrayList.set(typeArrayList.size() - 1, typeArrayList.get(typeArrayList.size() - 1).substring(0, typeArrayList.get(typeArrayList.size() - 1).length() - 3));
                    }
                }
            };


    notBox.setOnAction(notBoxEvent);
    checkKind.setOnAction(dropKindEvent);
    checkTypes.setOnAction(dropTypesEvent);
    HBox comboNot = new HBox(checkTypes, notBox);
    VBox checkContainer = new VBox(checkKind, comboNot, paramsBox); // makes a chunk
    box.getChildren().add(checkContainer); // adds it to the scene
    }
}