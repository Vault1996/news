package com.epam.esm.task1.importjob;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.importjob.exception.DirectoryNotFoundException;

@Component
public class NewsImportJob {

	private static final String EXTENSION_OF_IMPORTED_FILES = "xml";

	private static final Logger LOGGER = LogManager.getLogger();

	@Autowired
	private ScheduledExecutorService newsImportScheduledExecutorService;

	@Autowired
	private ExecutorService newsImportExecutorService;

	@Value("${newsXml.initialDelayInMilliseconds}")
	private Integer initialDelayInMilliseconds;

	@Value("${newsXml.periodToSearchInMilliseconds}")
	private Integer periodToSearchInMilliseconds;

	@Value("${newsXml.errorFolder}")
	private String errorFolderName;

	@Value("${newsXml.folderPath}")
	private String sourceFolder;

	@Autowired
	private NewsImportProcessor newsImportProcessor;

	private Set<String> lockedFiles = ConcurrentHashMap.newKeySet();

	/**
	 * Method to start searching folder with fixed rate.
	 */
	@EventListener(ContextRefreshedEvent.class)
	public void startImportJob() {
		newsImportScheduledExecutorService.scheduleAtFixedRate(() -> {
			try {
				searchFolder();
			} catch (IOException e) {
				LOGGER.error("Exception while searching files", e);
			}
		}, initialDelayInMilliseconds, periodToSearchInMilliseconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * Walks file tree from source directory and proceed every file with xml
	 * extension ignoring files in error directory
	 * 
	 * @throws IOException
	 */
	public void searchFolder() throws IOException {
		File sourceDirectory = new File(sourceFolder);
		if (sourceDirectory.exists() && sourceDirectory.isDirectory()) {
			Path startFolder = sourceDirectory.toPath();
			Files.walkFileTree(startFolder, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (errorFolderName.equals(dir.getFileName().toString())) {
						return FileVisitResult.SKIP_SUBTREE;
					} else {
						return FileVisitResult.CONTINUE;
					}
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String fileName = file.toString();
					String extension = FilenameUtils.getExtension(fileName);

					if (EXTENSION_OF_IMPORTED_FILES.equals(extension)) {
						boolean locked = lockedFiles.add(fileName);
						if (locked) {
							newsImportExecutorService.execute(() -> {
								try {
									if (Files.exists(file)) {
										newsImportProcessor.proceedFile(file);
									}
								} catch (IOException e) {
									LOGGER.error("Can't proceed file", e);
								} finally {
									lockedFiles.remove(fileName);
								}
							});
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			throw new DirectoryNotFoundException(
					errorFolderName + "directory doesn't exists in root search folder " + sourceFolder);
		}
	}

}
