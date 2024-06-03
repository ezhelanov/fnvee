package egor.top.fnvee.swing.frame;

import egor.top.fnvee.swing.PostConstructable;
import egor.top.fnvee.swing.panel.PathsPanel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

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
    private JButton checkButton;

    public void postConstruct() {
        setSize(dimension.width / 2, dimension.height / 2);
        setLocation(dimension.width / 4, dimension.height / 4);
        setResizable(Boolean.FALSE);
        setTitle("FNVEE+ by e_ropka");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(pathsPanel, BorderLayout.NORTH);
    }

}
