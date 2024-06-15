package egor.top.fnvee.swing.panel;

import egor.top.fnvee.core.PathService;
import egor.top.fnvee.swing.SwingConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
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
    private final JButton backButton = new JButton("Назад");
    private Path viewPath;
    private final JButton rootButton = new JButton("В корень мода");

    private final JList<Path> eView = new JList<>() {{
        setFont(SwingConstants.arial16);
        setBackground(SwingConstants.orange);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }};

    @Override
    public void postConstruct() {
        setLayout(new GridLayout(5, 1));

        eButtonDel.addActionListener(e -> {
            pathService.deleteAndRefresh(eMods, eButton);
            eView.setListData(new Path[0]);
        });
        newButtonDel.addActionListener(e -> pathService.deleteAndRefresh(newMods, newButton));
        newButtonAdd.addActionListener(e -> pathService.install(newMods, eButton));

        add(new JLabel(header));

        // установленные Е-моды
        var ePanel = new JPanel();
        ePanel.setLayout(new GridBagLayout());
        ePanel.add(util.fiveButtonsPanel(eButton, eButtonDel, util.button(), util.button()));
        ePanel.add(new JScrollPane(eMods), util.pos(1, 0));
        add(ePanel);

        // просмотр файлов
        var eViewerPanel = new JPanel();
        eViewerPanel.setLayout(new GridBagLayout());
        eViewerPanel.add(util.fiveButtonsPanel(backButton, rootButton, util.button(), util.button()));
        eViewerPanel.add(new JScrollPane(eView), util.pos(1, 0));
        add(eViewerPanel);

        eMods.addListSelectionListener(e -> Optional.ofNullable(eMods.getSelectedValue()).ifPresent(path -> eView.setListData(pathService.getPaths(path))));
        eView.addListSelectionListener(
                e -> Optional.ofNullable(eView.getSelectedValue())
                        .filter(Files::isDirectory)
                        .ifPresent(path -> {
                            eView.setListData(pathService.getPaths(path));
                            viewPath = path.getParent();
                        })
        );
        backButton.addActionListener(
                e -> Optional.ofNullable(viewPath)
                        .ifPresent(path -> {
                            eView.setListData(pathService.getPaths(path));
                            viewPath = eMods.getSelectedValue().equals(viewPath) ? viewPath : viewPath.getParent();
                        })
        );
        rootButton.addActionListener(e -> Optional.ofNullable(eMods.getSelectedValue()).ifPresent(path -> eView.setListData(pathService.getPaths(path))));

        // скачанные архивы модов
        var newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
        newPanel.add(util.fiveButtonsPanel(newButton, newButtonDel, newButtonAdd, util.button()));
        newPanel.add(new JScrollPane(newMods), util.pos(1, 0));
        add(newPanel);
    }
}
