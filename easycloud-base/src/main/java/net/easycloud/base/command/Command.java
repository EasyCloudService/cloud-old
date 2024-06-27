package net.easycloud.base.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();
    String description() default "";
    String[] aliases() default {};
}