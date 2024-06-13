package egor.top.fnvee.swing.panel;

import egor.top.fnvee.swing.PostConstructable;
import egor.top.fnvee.swing.SwingUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel implements PostConstructable {

    @Autowired
    protected SwingUtil util;

    @Autowired
    protected Dimension dimension;
}
