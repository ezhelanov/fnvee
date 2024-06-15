package egor.top.fnvee.swing;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SwingUtil {

    public Path toPath(JTextField jTextField) throws InvalidPathException {
        return Paths.get(jTextField.getText());
    }

    public GridBagConstraints pos(int x, int y, int width, int height) {
        GridBagConstraints pos = pos(x, y);
        pos.gridwidth = width;
        pos.gridheight = height;
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

    public JButton button() {
        var button = new JButton("Временно отключена");
        button.setEnabled(false);
        return button;
    }
    public JButton buttonWide() {
        var button = button();
        button.setText("___________________________");
        return button;
    }

    public JPanel fiveButtonsPanel(JButton b1, JButton b2, JButton b3, JButton b4) {
        var panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(buttonWide());
        return panel;
    }
}
