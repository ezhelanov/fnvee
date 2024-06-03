package egor.top.fnvee;

import egor.top.fnvee.swing.frame.MainFrame;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FnveeApplication {

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventListener(MainFrame mainFrame) {
        return applicationReadyEvent -> mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(FnveeApplication.class)
                .headless(Boolean.FALSE)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
