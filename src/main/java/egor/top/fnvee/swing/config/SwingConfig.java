package egor.top.fnvee.swing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

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
}
