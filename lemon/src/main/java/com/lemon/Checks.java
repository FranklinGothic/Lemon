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

    private ArrayList<Check> checks;

    public Checks(Lemon lemonObj, Stage stage) {
        this.lemonObj = lemonObj;
        this.stage = stage;
    }

    public void handle() {
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
    
        String[] checkKinds = {"[[check.pass]]", "[[check.fail]]", "[[check.passoverride]]"};

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
        
        VBox 
        EventHandler<ActionEvent> addButtonEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e)
            {
                addChunk(checkKinds, checksMap);
            }
        };

        //Done button to be implemented
        VBox parent = new VBox(hbMessage, hbPoints, add);
        Scene sc = new Scene(parent, 500, 500);
        stage.setScene(sc);
    }
    
    public void addChunk(String[] checkKinds, LinkedHashMap<String, String[]> checksMap, VBox parent) {
        VBox childVbox = new VBox();

    }


}