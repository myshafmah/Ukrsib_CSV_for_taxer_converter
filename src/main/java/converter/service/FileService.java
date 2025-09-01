package converter.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileService {
    Optional<File> findLatestFile(String directoryPath, String filePrefix, String fileExtension);
    String readFileContent(File file) throws IOException;
    String getUserHomeDirectory();
    String getDownloadsPath();
}