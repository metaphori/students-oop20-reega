package reega.viewutils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.scene.Node;
import javafx.util.StringConverter;

public final class ViewUtils {
    private ViewUtils() {
    }

    /**
     * Wrap <code>node</code> with classes.
     *
     * @param <T>     type of the node
     * @param node    node that needs to be wrapped with style classes
     * @param classes classes of the node
     * @return a node with all the styles classes defined by <code>classes</code>
     */
    public static <T extends Node> T wrapNodeWithStyleClasses(final T node, final String... classes) {
        node.getStyleClass().addAll(classes);
        return node;
    }

    /**
     * String converter that converts a date to a string and viceversa.
     *
     * @return a {@link StringConverter} that converts a date to a string and viceversa
     */
    public static StringConverter<Number> getDateStringConverter() {
        return new StringConverter<>() {

            private final DateFormat usDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            @Override
            public String toString(final Number object) {
                return this.usDateFormat.format(new Date(object.longValue()));
            }

            @Override
            public Number fromString(final String string) {
                try {
                    return this.usDateFormat.parse(string).getTime();
                } catch (final ParseException e) {
                    throw new IllegalStateException("Error occurred when parsing the string: " + string
                            + " into a Date. Trace: " + ExceptionUtils.getStackTrace(e));
                }
            }
        };
    }

    public static Long getDayOfTheMonth(final int day) {
        return Date.valueOf(LocalDate.now().withDayOfMonth(day)).getTime();
    }
}
