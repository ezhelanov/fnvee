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
    @Autowired
    private JButton checkButton;

    @Override
    public void postConstruct() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        add(new JLabel("Расположение Fallout New Vegas Extended Edition", JLabel.LEFT), util.pos(0, 0));
        add(fnveeText, util.pos(1, 0));
        add(new JLabel("Расположение папки для скачивания", JLabel.LEFT), util.pos(0, 1));
        add(downloadText, util.pos(1,1));
        add(checkButton, util.pos(0, 2, 2, 1));

    }
}
