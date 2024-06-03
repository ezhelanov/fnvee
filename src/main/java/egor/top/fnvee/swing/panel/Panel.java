package egor.top.fnvee.swing.panel;

import egor.top.fnvee.swing.PostConstructable;
import egor.top.fnvee.swing.SwingUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

public abstract class Panel extends JPanel implements PostConstructable {

    @Autowired
    protected SwingUtil util;
}
