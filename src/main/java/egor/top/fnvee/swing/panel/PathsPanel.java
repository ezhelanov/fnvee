package egor.top.fnvee.swing.panel;

import egor.top.fnvee.swing.SwingConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@Getter
@Setter
public class PathsPanel extends Panel {

    @Autowired
    private JTextField fnveeText;
    private Path fnveePath;

    @Autowired
    private JTextField downloadText;
    private Path downloadPath;

    @Autowired
    private JButton checkButton;

    @Override
    public void postConstruct() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        add(new JLabel("Расположение Fallout New Vegas Extended Edition", JLabel.LEFT), util.pos(0, 0));
        add(fnveeText, util.pos(1, 0));
        add(new JLabel("Расположение папки для скачивания", JLabel.LEFT), util.pos(0, 1));
        add(downloadText, util.pos(1, 1));
        add(checkButton, util.pos(0, 2, 2, 1));
        checkButton.addActionListener((ActionEvent e) -> tryGetPath());
        tryGetPath();
    }

    private void tryGetPath() {
        log.warn("+++ tryGetPath +++");

        try {
            Path fnveePath = util.toPath(fnveeText);
            if (Files.isDirectory(fnveePath) && Files.exists(fnveePath)) {
                fnveeText.setBackground(SwingConstants.green);
                this.fnveePath = fnveePath;
                log.info(fnveePath.toString());
            } else {
                fnveeText.setBackground(Color.RED);
            }
        } catch (Exception e) {
            fnveeText.setBackground(Color.RED);
        }

        try {
            Path downloadPath = util.toPath(downloadText);
            if (Files.isDirectory(downloadPath) && Files.exists(downloadPath)) {
                downloadText.setBackground(SwingConstants.green);
                this.downloadPath = downloadPath;
                log.info(downloadPath.toString());
            } else {
                downloadText.setBackground(Color.RED);
            }
        } catch (Exception e) {
            downloadText.setBackground(Color.RED);
        }

        log.warn("--- tryGetPath ---");
    }

}
