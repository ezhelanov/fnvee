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

}
