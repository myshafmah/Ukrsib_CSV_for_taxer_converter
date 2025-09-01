package converter.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GenericParser<T> {
    List<T> parse(File file) throws IOException, Exception;
    List<T> parseFromPath(String filePath) throws IOException, Exception;
}