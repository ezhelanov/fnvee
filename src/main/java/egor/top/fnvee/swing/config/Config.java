package egor.top.fnvee.swing.config;

import egor.top.fnvee.swing.SwingUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Config {

    @Autowired
    protected SwingUtil util;
}
