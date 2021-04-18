package reega.viewutils;

import javafx.scene.Node;
import javafx.util.StringConverter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * <a href="https://it.wikipedia.org/wiki/Atto_di_dolore_(preghiera)">source</a>
 *
 * Mio Dio mi pento e mi dolgo con tutto il cuore dei miei peccati, perché peccando ho meritato i tuoi castighi, e molto
 * più perché ho offeso Te, infinitamente buono e degno di essere amato sopra ogni cosa. Propongo con il tuo santo aiuto
 * di non offenderti mai più e di fuggire le occasioni prossime di peccato. Signore, misericordia, perdonami
 */
public final class ViewUtils {
    private ViewUtils() {
    }

    public static <T extends Node> T wrapNodeWithStyleClasses(T node, String... classes) {
        node.getStyleClass().addAll(classes);
        return node;
    }

    public static StringConverter<Number> getDateStringConverter() {
        return new StringConverter<Number>() {

            private final DateFormat usDateFormat = new SimpleDateFormat("yyyy/MM/dd");

            @Override
            public String toString(Number object) {
                return usDateFormat.format(new Date(object.longValue()));
            }

            @Override
            public Number fromString(String string) {
                try {
                    return usDateFormat.parse(string).getTime();
                } catch (ParseException e) {
                    throw new IllegalStateException();
                }
            }
        };
    }

    //public static getTreeCellFactory
    //

    public static Long getDayOfTheMonth(int day) {
        return Date.valueOf(LocalDate.now().withDayOfMonth(day)).getTime();
    }
}
