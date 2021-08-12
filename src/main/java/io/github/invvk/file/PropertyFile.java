package io.github.invvk.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {

    private final Properties properties = new Properties();
    private final File file;

    public final String MAJOR = "major", MINOR = "minor", PATCH = "patch";

    public PropertyFile(File basedir, String fileName) throws IOException {
        file = new File(basedir, fileName.endsWith(".properties") ? fileName : fileName + ".properties");
        // Checking for existence
        if (!file.exists())
            file.createNewFile();

        // Loading file
        try (FileReader reader = new FileReader(file)) {
            properties.load(reader);
        }

        // checking for status if something has been modified in file.
        boolean changed = false;

        // initial default
        if (properties.getProperty(this.MAJOR) == null) {
            properties.setProperty(this.MAJOR, "1");
            changed = true;
        }

        if (properties.getProperty(this.MINOR) == null) {
            properties.setProperty(this.MINOR, "0");
            changed = true;
        }

        if (properties.getProperty(this.PATCH) == null) {
            String BUILD_NUMBER = "buildNum";
            if (properties.getProperty(BUILD_NUMBER) != null) {
                properties.setProperty(this.PATCH, properties.getProperty(BUILD_NUMBER));
                properties.remove(BUILD_NUMBER);
            } else
                properties.setProperty(this.PATCH, "0");
            changed = true;
        }

        // checking if anything changed in file
        if (changed) {
            // saving
            try (FileOutputStream stream = new FileOutputStream(file)) {
                properties.store(stream, null);
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getMajor() {
        return this.properties.getProperty(this.MAJOR);
    }

    public String getMinor() {
        return this.properties.getProperty(this.MINOR);
    }

    public String getPatch() {
        return this.properties.getProperty(this.PATCH);
    }

    public void modify(String key, String value) {
        this.properties.setProperty(key, value);
    }

    public void save() {
        try (FileOutputStream stream = new FileOutputStream(this.file)) {
            this.properties.store(stream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
