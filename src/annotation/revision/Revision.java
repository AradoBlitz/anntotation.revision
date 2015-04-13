package annotation.revision;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Revision {

	String name();

	String date();

	String comment();

}
