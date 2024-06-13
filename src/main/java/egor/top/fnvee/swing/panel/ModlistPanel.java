package egor.top.fnvee.swing.panel;

import egor.top.fnvee.core.PathService;
import egor.top.fnvee.swing.SwingConstants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

@Component
@Getter
public class ModlistPanel extends Panel {

    @Autowired
    private PathService pathService;

    private final JList<Path> eMods = new JList<>() {{
        setFont(SwingConstants.arial16);
        setBackground(SwingConstants.green);
    }};
    private final JButton eButton = new JButton("Установленные Е-моды");
    private final JButton eButtonDel = new JButton("Удалить") {{ setBackground(SwingConstants.red); }};

    private final JList<Path> newMods = new JList<>() {{ setFont(SwingConstants.arial16); }};
    private final JButton newButton = new JButton("Скачанные архивы модов");
    private final JButton newButtonDel = new JButton("Удалить") {{ setBackground(SwingConstants.red); }};
    private final JButton newButtonAdd = new JButton("Добавить");

    @Override
    public void postConstruct() {
        setLayout(new GridLayout(5, 1));

        eButtonDel.addActionListener((ActionEvent e) -> pathService.deleteAndRefresh(eMods, eButton));
        newButtonDel.addActionListener((ActionEvent e) -> pathService.deleteAndRefresh(newMods, newButton));
        newButtonAdd.addActionListener((ActionEvent e) -> {
            if (!newMods.getSelectedValuesList().isEmpty()) {
                newMods.getSelectedValuesList().forEach(path -> {
                    var newFolder = pathService.createFolder(path);
                    if (!pathService.unZipAndCopyToFolder(path, newFolder)) {
                        pathService.delete(newFolder);
                    } else {
                        eButton.doClick();
                    }
                });
            }
        });

        var ePanel = new JPanel();
        ePanel.setLayout(new GridBagLayout());
        ePanel.add(new JPanel() {{
            setLayout(new GridLayout(5, 1));
            add(eButton);
            add(eButtonDel);
            add(new JButton("1"));
            add(new JButton("2"));
            add(new JButton("___________________________") {{ setEnabled(false); }});
        }});
        ePanel.add(new JScrollPane(eMods), util.pos(1, 0));
        add(ePanel);

        var newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
        newPanel.add(new JPanel() {{
            setLayout(new GridLayout(5, 1));
            add(newButton);
            add(newButtonDel);
            add(newButtonAdd);
            add(new JButton("2"));
            add(new JButton("___________________________") {{ setEnabled(false); }});
        }});
        newPanel.add(new JScrollPane(newMods), util.pos(1, 0));
        add(newPanel);
    }
}
