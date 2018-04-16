package sep.util.io.file;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URISyntaxException;
import java.net.URL;

public final class FileUtil {
    public static Path classpath(String name) {
        try {
            final URL url = Class.class.getResource(name);
            return (url != null) ? Paths.get(url.toURI()) : null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
 
    private FileUtil() {
    }
}