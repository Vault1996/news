package com.epam.esm.task1.importjob;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.dto.NewsImportContent;
import com.epam.esm.task1.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NewsImportProcessor {

	private static final String TIMESTAMP_IN_FILENAME_FORMAT_PATTERN = "YYYY-MM-dd'T'HH-mm-ss-SSS";

	private static final String SEPARATOR_OF_NAME_AND_TIMESTAMP = "_";

	private static final Logger LOGGER = LogManager.getLogger();

	private Path errorDirectory;

	@Autowired
	private ObjectMapper xmlMapper;

	@Autowired
	private NewsService newsService;

	public NewsImportProcessor(@Value("${newsXml.errorFolder}") String errorFolderName,
			@Value("${newsXml.folderPath}") String sourceFolder) {
		File errorDirectory = new File(sourceFolder, errorFolderName);
		this.errorDirectory = errorDirectory.toPath();
	}

	/**
	 * Read file and marshall xml content to news.
	 * 
	 * @param file
	 *            file to proceed
	 * @throws IOException
	 */
	public void proceedFile(Path file) throws IOException {
		try {
			NewsImportContent news = xmlMapper.readValue(file.toFile(), NewsImportContent.class);

			newsService.save(news);

			Files.deleteIfExists(file);
		} catch (FileNotFoundException ex) {
			LOGGER.error("Unable to find file.", ex);
		} catch (ConstraintViolationException ex) {
			String errorMessage = buildErrorMessage(ex);
			LOGGER.error(errorMessage, ex);
			moveFile(file);
		} catch (Exception ex) {
			LOGGER.error("Exception in processing file.", ex);
			moveFile(file);
		}
	}

	private String buildErrorMessage(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> validationErrors = ex.getConstraintViolations();
		StringBuilder errorMessage = new StringBuilder();
		for (ConstraintViolation<?> violation : validationErrors) {
			errorMessage.append(violation.getMessage()).append("\n");
		}
		errorMessage.delete(errorMessage.length() - 1, errorMessage.length());
		return errorMessage.toString();
	}

	private void moveFile(Path file) throws IOException {
		if (Files.notExists(errorDirectory)) {
			Files.createDirectories(errorDirectory);
		}

		Files.move(file,
				errorDirectory.resolve(String.format("%s%s%s%s%s", FilenameUtils.getBaseName(file.toString()),
						SEPARATOR_OF_NAME_AND_TIMESTAMP,
						LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_IN_FILENAME_FORMAT_PATTERN)),
						FilenameUtils.EXTENSION_SEPARATOR, FilenameUtils.getExtension(file.toString()))),
				StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
	}
}
