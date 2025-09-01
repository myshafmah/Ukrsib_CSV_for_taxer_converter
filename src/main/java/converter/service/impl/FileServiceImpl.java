package converter.service.impl;

import converter.service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class FileServiceImpl implements FileService {
    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);

    @Override
    public Optional<File> findLatestFile(String directoryPath, String filePrefix, String fileExtension) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith(filePrefix) && name.endsWith(fileExtension));

        if (files == null || files.length == 0) {
            logger.info("No files found matching pattern: {} in {}", filePrefix + "*" + fileExtension, directoryPath);
            return Optional.empty();
        }

        File latestFile = files[0];
        for (File file : files) {
            if (file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }
        logger.info("Found latest file: {}", latestFile.getName());
        return Optional.of(latestFile);
    }

    @Override
    public String readFileContent(File file) throws IOException {
        logger.info("Reading content from file: {}", file.getName());
        return new String(Files.readAllBytes(file.toPath()));
    }

    @Override
    public String getUserHomeDirectory() {
        return System.getProperty("user.home");
    }

    @Override
    public String getDownloadsPath() {
        return getUserHomeDirectory() + "\\Downloads";
    }
}
