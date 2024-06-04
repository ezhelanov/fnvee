package egor.top.fnvee.swing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@Configuration
public class PathsPanelConfig extends Config {

    @Bean
    public JTextField fnveeText(@Value("${app.path.fnvee}") String path) {
        return new JTextField(path);
    }

    @Bean
    public JTextField downloadText(@Value("${app.path.download}") String path) {
        return new JTextField(path);
    }

    @Bean
    public JButton checkButton() {
        return new JButton("Проверить");
    }

}
