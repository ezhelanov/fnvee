package egor.top.fnvee.swing;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
@Getter
public class PathsPanel extends JPanel {

    @Autowired
    private JTextField fnveeText;
    @Autowired
    private JTextField downloadText;

    @PostConstruct
    public void postConstruct() {
        setLayout(new GridLayout(2, 2));
        add(new JLabel("Расположение Fallout New Vegas Extended Edition", JLabel.LEFT));
        add(fnveeText);
        add(new JLabel("Расположение папки для скачивания", JLabel.LEFT));
        add(downloadText);
    }
}
