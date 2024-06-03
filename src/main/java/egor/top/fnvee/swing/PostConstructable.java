package egor.top.fnvee.swing;

import javax.annotation.PostConstruct;


public interface PostConstructable {

    @PostConstruct // при переопределении аннотация сохраняется (если default)
    default void postConstruct() {}
}
