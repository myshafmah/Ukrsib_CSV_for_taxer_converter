package converter.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public final class FileUtils {

    private FileUtils() {
        // Utility class
    }

    /**
     * Find the latest modified file in a directory that matches prefix and extension.
     */
    public static Optional<File> findLatestFile(String directoryPath, String filePrefix, String fileExtension) {
        Path dir = Paths.get(directoryPath);
        if (!Files.isDirectory(dir)) {
            return Optional.empty();
        }

        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(p -> {
                        String name = p.getFileName().toString();
                        return name.startsWith(filePrefix) && name.endsWith(fileExtension);
                    })
                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()))
                    .map(Path::toFile);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
