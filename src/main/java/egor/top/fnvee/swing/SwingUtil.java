package egor.top.fnvee.swing;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class SwingUtil {

    public JTextField colorize(JTextField jTextField) {
        try {
            if (Files.isDirectory(Paths.get(jTextField.getText()))) {
                jTextField.setBackground(new Color(168,234,176));
            } else {
                jTextField.setBackground(Color.RED);
            }
        } catch (Exception e) {
            jTextField.setBackground(Color.RED);
        }
        return jTextField;
    }

    public JButton button(ActionListener actionListener, String text) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    public GridBagConstraints pos(int x, int y, int width, int gridheight) {
        GridBagConstraints pos = pos(x, y);
        pos.gridwidth = width;
        pos.gridheight = gridheight;
        return pos;
    }

    public GridBagConstraints pos(int x, int y) {
        GridBagConstraints pos = new GridBagConstraints();
        pos.gridx = x;
        pos.gridy = y;
        pos.weightx = 1;
        pos.anchor = GridBagConstraints.WEST;
        pos.fill = GridBagConstraints.HORIZONTAL;
        return pos;
    }


}
