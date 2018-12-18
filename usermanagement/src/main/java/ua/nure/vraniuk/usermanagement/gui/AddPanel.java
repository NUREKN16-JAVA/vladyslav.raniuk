package ua.nure.vraniuk.usermanagement.gui;

import ua.nure.vraniuk.usermanagement.User;
import ua.nure.vraniuk.usermanagement.db.DatabaseException;
import ua.nure.vraniuk.usermanagement.gui.main.MainFrame;
import ua.nure.vraniuk.usermanagement.utilities.Messages;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class AddPanel extends AbstractModifiedPanel {


    private static final String ADD_PANEL = "addPanel";

    public AddPanel(MainFrame parent) {
        super(parent);
        this.setName(ADD_PANEL);
    }


    @Override
    protected void performAction() {
        User user = new User();
        user.setFirstName(getFirstNameField().getText());
        user.setLastName(getLastNameField().getText());
        try {
            user.setDateOfBirth(format.parse(getDateOfBirthField().getText()));
        } catch (ParseException e1) {
            getDateOfBirthField().setBackground(Color.RED);
            return;
        }
        try {
            parent.getUserDao().createUser(user);
        } catch (DatabaseException e1) {
            JOptionPane.showMessageDialog(this, e1.getMessage(), ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    protected String getConfirmButtonText() {
        return Messages.getString("addButton");
    }

}
