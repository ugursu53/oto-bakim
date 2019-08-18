package tr.com.mavi.oto.service.util;

public class SearchUtil {

    public final static String normalizedQuery(String query) {
        query = query.trim();
        query = "*" + query + "*";
        return query.replaceAll("\\s+", "* *");
    }
}
