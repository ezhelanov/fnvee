package egor.top.fnvee.swing.panel;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Getter
public class PathsPanel extends Panel {

    @Autowired
    private JTextField fnveeText;
    @Autowired
    private JTextField downloadText;

    @Override
    public void postConstruct() {
        setLayout(new GridLayout(2, 2));
        add(new JLabel("Расположение Fallout New Vegas Extended Edition", JLabel.LEFT));
        add(fnveeText);
        add(new JLabel("Расположение папки для скачивания", JLabel.LEFT));
        add(downloadText);
    }
}
