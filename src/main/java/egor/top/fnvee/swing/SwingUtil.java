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
