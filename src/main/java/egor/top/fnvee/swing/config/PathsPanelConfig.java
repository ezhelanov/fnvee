package egor.top.fnvee.swing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Configuration
public class PathsPanelConfig extends Config {

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
