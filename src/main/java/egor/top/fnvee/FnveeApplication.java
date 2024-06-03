package egor.top.fnvee;

import egor.top.fnvee.swing.MainFrame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@SpringBootApplication
public class FnveeApplication {

    @Bean
    public Toolkit toolkit() {
        return Toolkit.getDefaultToolkit();
    }

    @Bean
    public Dimension dimension(Toolkit toolkit) {
        return toolkit.getScreenSize();
    }

    @Bean
    public ImageIcon imageIcon(ResourceLoader resourceLoader) throws IOException {
        return new ImageIcon(resourceLoader.getResource("assets/icon.png").getURL());
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventListener(MainFrame mainFrame) {
        return applicationReadyEvent -> mainFrame.setVisible(true);
    }

    @Bean
    public JTextField fnveeText(@Value("${app.path.fnvee}") String path) {
        return new JTextField(path);
    }

    @Bean
    public JTextField downloadText(@Value("${app.path.download}") String path) {
        return new JTextField(path);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(FnveeApplication.class)
                .headless(Boolean.FALSE)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
