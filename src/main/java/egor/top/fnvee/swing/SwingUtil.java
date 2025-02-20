package egor.top.fnvee.swing;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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

    public JPanel fiveButtonsPanel(JComponent b1, JComponent b2, JComponent b3, JComponent b4) {
        var panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(buttonWide());
        return panel;
    }

    public boolean isDelete(Path path) {
        if (Objects.isNull(path)) {
            return false;
        }
        int result = JOptionPane.showConfirmDialog(
                null,
                MessageFormatter.format("Удалить {} ?", Strings.dquote(path.toString())).getMessage(),
                SwingConstants.deletion,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return result == 0;
    }

    public void alreadyInstalled(Path path) {
        if (Objects.isNull(path)) {
            return;
        }
        JOptionPane.showMessageDialog(
                null,
                MessageFormatter.format("Мод {} уже установлен !", Strings.dquote(path.toString())).getMessage(),
                SwingConstants.installation,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void cannotInstall(Path path) {
        if (Objects.isNull(path)) {
            return;
        }
        JOptionPane.showMessageDialog(
                null,
                MessageFormatter.format("Не удалось установить мод {} !", Strings.dquote(path.toString())).getMessage(),
                SwingConstants.installation,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public boolean isInstall(Path path) {
        if (Objects.isNull(path)) {
            return false;
        }
        int result = JOptionPane.showConfirmDialog(
                null,
                MessageFormatter.format("Установить мод {} ? Может занять некоторое время", Strings.dquote(path.toString())).getMessage(),
                SwingConstants.installation,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == 0;
    }
}
