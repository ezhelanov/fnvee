package egor.top.fnvee.swing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@Configuration
public class SwingConfig extends Config {

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
}
