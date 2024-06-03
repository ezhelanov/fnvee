package egor.top.fnvee.swing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame {

    @Autowired
    private Dimension dimension;
    @Autowired
    private ImageIcon imageIcon;
    @Autowired
    private PathsPanel pathsPanel;

    @PostConstruct
    public void postConstruct() {
        setSize(dimension.width / 2, dimension.height / 2);
        setLocation(dimension.width / 4, dimension.height / 4);
        setTitle("FNVEE+ by e_ropka");
        setIconImage(imageIcon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 1));
        add(pathsPanel);
    }
}
