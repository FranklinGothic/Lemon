package com.lemon;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.LinkedHashMap;

public class Checks {
    private Lemon lemonObj;
    private Stage stage;

    public Checks(Lemon lemonObj, Stage stage) {
        this.lemonObj = lemonObj;
        this.stage = stage;
    }

    public void handle() {
        Button addVuln = new Button("Add Vulnerability");
        VBox root = new VBox(addVuln);
        Scene addVulnerability = new Scene(root, 500, 500);

        EventHandler<ActionEvent> doneButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                handleCheck();
            }
        };

        addVuln.setOnAction(doneButtonEvent);
        stage.setScene(addVulnerability);
    }

    public void handleCheck() {
        LinkedHashMap<String, String[]> checksAndVauls = new LinkedHashMap<>();
        checksAndVauls.put("CommandContains", new String[]{"cmd", "value"});
        checksAndVauls.put("CommandOutput", new String[]{"cmd", "value"});
        checksAndVauls.put("DirContains", new String[]{"path", "value"});
        checksAndVauls.put("FileContains", new String[]{"path", "value"});
        checksAndVauls.put("FileEquals", new String[]{"path", "value"});
        checksAndVauls.put("FileOwner", new String[]{"path", "name"});
        checksAndVauls.put("FirewallUp", new String[]{});
        checksAndVauls.put("PathExists", new String[]{"path"});
        checksAndVauls.put("ProgramInstalled", new String[]{"name"});
        checksAndVauls.put("ProgramVersion", new String[]{"name", "value"});
        checksAndVauls.put("ServiceUp", new String[]{"name"});
        checksAndVauls.put("UserExists", new String[]{"name"});
        checksAndVauls.put("UserInGroup", new String[]{"user", "group"});
        //I'm not sure if this implementation will work because people may name wrong but idk
        //also idk if we need the linux checks but like who cares (just gunna assume windows unless linux in title)
        if (lemonObj.getOs().contains("Linux") || lemonObj.getOs().contains("linux")) {
            checksAndVauls.put("PasswordChanged", new String[]{"user", "value"});
            checksAndVauls.put("PermissionIs", new String[]{"path", "value"});
            checksAndVauls.put("AutoCheckUpdatesEnabled", new String[]{});
            checksAndVauls.put("Command", new String[]{"cmd"});
            checksAndVauls.put("GuestDisabledLDM", new String[]{});
            checksAndVauls.put("KernelVersion", new String[]{"value"});
        } else {
            checksAndVauls.put("PasswordChanged", new String[]{"user", "after"});
            checksAndVauls.put("PermissionIs", new String[]{"path", "name", "value"});
            checksAndVauls.put("BitlockerEnabled", new String[]{});
            checksAndVauls.put("FirewallDefaultBehavior", new String[]{"name", "value", "key"});
            checksAndVauls.put("GuestDisabledLDM", new String[]{"key", "value"});
            checksAndVauls.put("RegistryKeyExists", new String[]{"key"});
            checksAndVauls.put("ScheduledTaskExists", new String[]{"name"});
            checksAndVauls.put("SecurityPolicy", new String[]{"key", "value"});
            checksAndVauls.put("ServiceStartup", new String[]{"name", "value"});
            checksAndVauls.put("ShareExists", new String[]{"name"});
            checksAndVauls.put("UserDetail", new String[]{"user","key", "value"});
            checksAndVauls.put("UserRights", new String[]{"name", "value"});
            checksAndVauls.put("WindowsFeature", new String[]{"name"});
        }
        final ComboBox<String> checkTypes = new ComboBox<>();
        for (String type : checksAndVauls.keySet()) {
            checkTypes.getItems().add(type);
        }
        VBox checksAndVaulsBox = new VBox();
        EventHandler<ActionEvent> dropTypesEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                handleType(checksAndVauls, checkTypes, checksAndVaulsBox);
            }
        };
        checkTypes.setOnAction(dropTypesEvent);
        VBox checkRoot = new VBox(checkTypes, checksAndVaulsBox);
        Scene sc = new Scene(checkRoot, 500, 500);
        stage.setScene(sc);
    }

    public void handleType(LinkedHashMap<String, String[]> typeMap, ComboBox<String> dropBox, VBox box) {
    box.getChildren().clear();    
        for (String porams : typeMap.get(dropBox.getValue())) {
            Label poram = new Label(porams + ":");
            TextField poramField = new TextField();
            HBox hbporam = new HBox();
            hbporam.getChildren().addAll(poram, poramField);
            hbporam.setSpacing(10);
            box.getChildren().add(hbporam);
        }
    }


    
}