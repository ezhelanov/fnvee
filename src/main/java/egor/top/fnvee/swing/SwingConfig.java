package egor.top.fnvee.swing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class SwingConfig {

    private final SwingUtil util;

    @Bean
    public Toolkit toolkit() {
        return Toolkit.getDefaultToolkit();
    }

    @Bean
    public Dimension dimension(Toolkit toolkit) {
        return toolkit.getScreenSize();
    }

    @Bean
    public ImageIcon icon(ResourceLoader resourceLoader) throws IOException {
        return new ImageIcon(resourceLoader.getResource("assets/icon.png").getURL());
    }

    @Bean
    public JTextField fnveeText(@Value("${app.path.fnvee}") String path) {
        return util.colorize(new JTextField(path));
    }

    @Bean
    public JTextField downloadText(@Value("${app.path.download}") String path) {
        return util.colorize(new JTextField(path));
    }

    @Bean
    public JButton checkButton(JTextField fnveeText, JTextField downloadText) {
        return util.button(
                (ActionEvent e) -> {
                    util.colorize(fnveeText);
                    util.colorize(downloadText);
                },
                "Проверить"
        );
    }
}
