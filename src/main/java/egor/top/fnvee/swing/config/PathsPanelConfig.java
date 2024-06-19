package egor.top.fnvee.swing.config;

import egor.top.fnvee.swing.panel.ModlistPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;

@Configuration
public class PathsPanelConfig extends Config {

    @Autowired
    private ModlistPanel modlistPanel;

    @Bean
    public JTextField fnveeText(@Value("${app.path.fnvee}") String path) {
        return new JTextField(path);
    }

    @Bean
    public JTextField downloadText(@Value("${app.path.download}") String path) {
        return new JTextField(path);
    }

    @Bean
    public FileChangeListener fileChangeListener() {
        return changeSet -> EventQueue.invokeLater(() -> modlistPanel.getNewButton().doClick());
    }

    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        var fileSystemWatcher = new FileSystemWatcher(
                true,
                Duration.ofMillis(5000L),
                Duration.ofMillis(3000L)
        );
        fileSystemWatcher.addListener(fileChangeListener());
        return fileSystemWatcher;
    }

    @PreDestroy
    public void preDestroy() {
        fileSystemWatcher().stop();
    }
}
