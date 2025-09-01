package converter.writer;

import java.io.IOException;
import java.util.List;

public interface DataWriter<T> {
    void write(List<T> data, String outputPath) throws IOException;
}
