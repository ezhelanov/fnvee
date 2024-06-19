package egor.top.fnvee.swing.frame;

import egor.top.fnvee.core.PathService;
import egor.top.fnvee.swing.PostConstructable;
import egor.top.fnvee.swing.panel.ModlistPanel;
import egor.top.fnvee.swing.panel.PathsPanel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Slf4j
@Component
@Getter
public class MainFrame extends JFrame implements PostConstructable {

    @Autowired
    private Dimension dimension;
    @Autowired
    private ImageIcon icon;
    @Autowired
    private PathsPanel pathsPanel;
    @Autowired
    private ModlistPanel modlistPanel;
    @Autowired
    private PathService pathService;

    public void postConstruct() {
        setSize(dimension.width / 2, dimension.height);
        setLocation(dimension.width / 4, 0);
        setResizable(Boolean.FALSE);
        setTitle("FNVEE+ by EG0RKAP0MID0RKA");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(pathsPanel, BorderLayout.NORTH);
        add(modlistPanel, BorderLayout.CENTER);

        modlistPanel.getEButton()
                .addActionListener(
                        e -> modlistPanel.getEMods()
                                .setListData(pathService.getPaths(pathsPanel.getFnveePath(), true))
                );
        modlistPanel.getNewButton()
                .addActionListener(
                        e -> modlistPanel.getNewMods()
                                .setListData(pathService.getPaths(pathsPanel.getDownloadPath(), false))
                );
    }

}
