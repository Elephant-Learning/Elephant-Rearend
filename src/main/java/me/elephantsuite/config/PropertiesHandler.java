package me.elephantsuite.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

import lombok.Getter;
import me.elephantsuite.ElephantBackendApplication;

@Getter
public class PropertiesHandler {

	private final Path propertiesPath;

	private final Map<String, String> configValues;

	public static final Path CONFIG_HOME_DIRECTORY = Paths.get("src", "main", "resources").resolve("Elephant Backend Config");

	static {
		if (!Files.exists(CONFIG_HOME_DIRECTORY)) {
			try {
				Files.createDirectory(CONFIG_HOME_DIRECTORY);
			} catch (IOException e) {
				ElephantBackendApplication.LOGGER.error("Error while creating config home directory at: \"" + CONFIG_HOME_DIRECTORY + "\"");
				e.printStackTrace();
			}
		}
	}

	private PropertiesHandler(String filename, Map<String, String> configValues) {

		this.propertiesPath = CONFIG_HOME_DIRECTORY.resolve(filename);
		this.configValues = configValues;
	}

	public void initialize() {
		try {
			load();
			save();
		} catch (IOException e) {
			ElephantBackendApplication.LOGGER.error("Error while initializing Properties Config for file " + "\"" + propertiesPath + "\"" + "!");
			e.printStackTrace();
		}

	}

	public void load() throws IOException {

		if (!Files.exists(propertiesPath)) {
			// return bc the file has not been saved yet
			return;
		}

		Properties properties = new Properties();

		properties.load(Files.newInputStream(propertiesPath));

		properties.forEach((o, o2) -> configValues.put(o.toString(), o2.toString()));

	}

	public void save() throws IOException {

		if (!Files.exists(propertiesPath.getParent())) {
			throw new RuntimeException("Could not find directory \"" + propertiesPath.getParent() + "\"!");
		}

		Properties properties = new Properties();

		properties.putAll(configValues);

		properties.store(Files.newOutputStream(propertiesPath), "This stores the configuration properties for Elephant Backend");

	}

	public static Builder builder() {
		return new Builder();
	}

	public void setConfigOption(String option, String newValue) {
		configValues.replace(option, newValue);
	}

	public void reload() {
		try {
			save();
			load();
		} catch (IOException e) {
			ElephantBackendApplication.LOGGER.error("Error while initializing Properties Config for file " + "\"" + propertiesPath + "\"" + " for Guild " + "\"" + configValues.get("name") + "\"" + "!");
			e.printStackTrace();
		}
	}

	public <T> T getConfigOption(String key, Function<String, T> parser) {
		return parser.apply(configValues.get(key));
	}

	public String getConfigOption(String key) {
		return configValues.get(key);
	}

	public boolean hasConfigOption(String key) {
		return configValues.get(key) != null && !configValues.get(key).isEmpty();
	}

	public boolean containsKey(String key) {
		return configValues.containsKey(key);
	}


	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 * @apiNote In general, the
	 * {@code toString} method returns a string that
	 * "textually represents" this object. The result should
	 * be a concise but informative representation that is easy for a
	 * person to read.
	 * It is recommended that all subclasses override this method.
	 * The string output is not necessarily stable over time or across
	 * JVM invocations.
	 * @implSpec The {@code toString} method for class {@code Object}
	 * returns a string consisting of the name of the class of which the
	 * object is an instance, the at-sign character `{@code @}', and
	 * the unsigned hexadecimal representation of the hash code of the
	 * object. In other words, this method returns a string equal to the
	 * value of:
	 * <blockquote>
	 * <pre>
	 * getClass().getName() + '@' + Integer.toHexString(hashCode())
	 * </pre></blockquote>
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
			.append("\"")
			.append(propertiesPath.getFileName())
			.append("\"")
			.append(": ")
			.append("{\n");

		configValues.forEach((s, s2) -> {
			builder
				.append("\"")
				.append(s)
				.append("\"")
				.append(": ")
				.append("\"")
				.append(s2)
				.append("\"")
				.append("\n");
		});

		builder.append("}");

		return builder.toString();
	}

	public static class Builder {

		private final Map<String, String> configValues = new HashMap<>();
		private String filename;

		private Builder() {}

		public Builder addConfigOption(String key, String defaultValue) {
			configValues.put(key, defaultValue);
			return this;
		}

		public Builder addConfigOption(String key, Object value) {
			return addConfigOption(key, Objects.toString(value));
		}

		public Builder setFileName(String fileName) {
			if (!fileName.endsWith(".properties")) {
				fileName += ".properties";
			}

			this.filename = fileName;

			return this;
		}

		public PropertiesHandler build() {
			PropertiesHandler propertiesHandler = new PropertiesHandler(filename, configValues);
			propertiesHandler.initialize();
			ElephantBackendApplication.LOGGER.info("Properties Handler with file name \"" + filename + "\" created on path \"" + propertiesHandler.propertiesPath + "\"");
			return propertiesHandler;
		}
	}

}
