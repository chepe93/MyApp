package com.app.checklist.model.annotations;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonMapping {
    String exportKey() default "";  //@JsonMapping(exportKey="nameX",importKey="nameY")
    String importKey() default "";

}
