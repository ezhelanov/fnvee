package egor.top.fnvee.swing.panel;

import egor.top.fnvee.core.PathService;
import egor.top.fnvee.swing.SwingConstants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Optional;

@Component
@Getter
public class ModlistPanel extends Panel {

    @Autowired
    private PathService pathService;
    @Autowired
    private ImageIcon header;

    private final JList<Path> eMods = new JList<>() {{
        setFont(SwingConstants.arial16);
        setBackground(SwingConstants.green);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }};
    private final JButton eButton = new JButton("Установленные Е-моды");
    private final JButton eButtonDel = new JButton("Удалить") {{ setBackground(SwingConstants.red); }};

    private final JList<Path> newMods = new JList<>() {{
        setFont(SwingConstants.arial16);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }};
    private final JButton newButton = new JButton("Скачанные архивы модов");
    private final JButton newButtonDel = new JButton("Удалить") {{ setBackground(SwingConstants.red); }};
    private final JButton newButtonAdd = new JButton("Установить") {{ setBackground(SwingConstants.green2); }};

    private final JList<Path> eView = new JList<>() {{
        setFont(SwingConstants.arial16);
        setBackground(SwingConstants.orange);
    }};

    @Override
    public void postConstruct() {
        setLayout(new GridLayout(5, 1));

        eButtonDel.addActionListener((ActionEvent e) -> {
            pathService.deleteAndRefresh(eMods, eButton);
            eView.setListData(new Path[0]);
        });
        newButtonDel.addActionListener((ActionEvent e) -> pathService.deleteAndRefresh(newMods, newButton));
        newButtonAdd.addActionListener((ActionEvent e) -> pathService.install(newMods, eButton));

        add(new JLabel(header));

        var ePanel = new JPanel();
        ePanel.setLayout(new GridBagLayout());
        ePanel.add(new JPanel() {{
            setLayout(new GridLayout(5, 1));
            add(eButton);
            add(eButtonDel);
            add(util.button());
            add(util.button());
            add(new JButton("___________________________") {{ setEnabled(false); }});
        }});
        ePanel.add(new JScrollPane(eMods), util.pos(1, 0));
        add(ePanel);

        var eViewerPanel = new JPanel();
        eViewerPanel.setLayout(new GridBagLayout());
        eViewerPanel.add(new JPanel() {{
            setLayout(new GridLayout(5, 1));
            add(util.button());
            add(util.button());
            add(util.button());
            add(util.button());
            add(new JButton("___________________________") {{ setEnabled(false); }});
        }});
        eViewerPanel.add(new JScrollPane(eView), util.pos(1, 0));
        add(eViewerPanel);

        eMods.addListSelectionListener(
                (ListSelectionEvent e) -> Optional.ofNullable(eMods.getSelectedValue()).ifPresent(path -> eView.setListData(pathService.getPaths(path)))
        );

        var newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
        newPanel.add(new JPanel() {{
            setLayout(new GridLayout(5, 1));
            add(newButton);
            add(newButtonDel);
            add(newButtonAdd);
            add(util.button());
            add(new JButton("___________________________") {{ setEnabled(false); }});
        }});
        newPanel.add(new JScrollPane(newMods), util.pos(1, 0));
        add(newPanel);
    }
}
