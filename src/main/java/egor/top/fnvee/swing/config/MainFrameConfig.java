package egor.top.fnvee.swing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.swing.*;
import java.io.IOException;

@Configuration
public class MainFrameConfig extends Config {

    @Bean
    public ImageIcon icon(ResourceLoader resourceLoader) throws IOException {
        return new ImageIcon(resourceLoader.getResource("assets/icon.png").getURL());
    }
}
