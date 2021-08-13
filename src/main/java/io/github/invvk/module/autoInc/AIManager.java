package io.github.invvk.module.autoInc;

import io.github.invvk.Options;
import io.github.invvk.module.Manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

// Auto increment module
public class AIManager extends Manager {

    private int major,minor,patch;

    public final String MAJOR = "major", MINOR = "minor", PATCH = "patch";

    private final Properties properties = new Properties();
    private final File propertyFile;

    public AIManager(Options options) {
        super(options);
        this.propertyFile = new File(options.getMojo().getProject().getBasedir(), options.getFileName());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void init() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(this.propertyFile),
                StandardCharsets.UTF_8))) {
            // Check if file exists, create
            if (propertyFile.exists())
                propertyFile.createNewFile();

            // Then loading
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        boolean changed = false;

        if (properties.getProperty("major") == null) {
            properties.setProperty("major", "1");
            changed = true;
        }

        if (properties.getProperty("minor") == null) {
            properties.setProperty("minor", "0");
            changed = true;
        }

        if (properties.getProperty("patch") == null) {
            String BUILD_NUMBER = "buildNum";
            if (properties.getProperty(BUILD_NUMBER) != null) {
                properties.setProperty("patch", properties.getProperty(BUILD_NUMBER));
                properties.remove(BUILD_NUMBER);
            } else {
                properties.setProperty("patch", "0");
            }
            changed = true;
        }

        if (changed)
            this.save();

        this.load();
    }

    private void load() {
        try {
            this.major = Integer.parseInt(this.properties.getProperty(this.MAJOR));
            this.minor = Integer.parseInt(this.properties.getProperty(this.MINOR));
            this.patch = Integer.parseInt(this.properties.getProperty(this.PATCH));
        } catch (NumberFormatException e) {
            this.options.getMojo().getLog().error("Major, Minor, and Patch must be numbers only!");
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public void pumpVersion() {
        if (this.patch >= this.options.getPatchLimit()) {
            this.patch = 0;
            if (this.minor >= this.options.getMinorLimit()) {
                this.minor = 0;
                this.major++;
            } else {
                this.minor++;
            }
        } else {
            this.patch++;
        }

        modify(this.MAJOR, String.valueOf(this.major));
        modify(this.MINOR, String.valueOf(this.minor));
        modify(this.PATCH, String.valueOf(this.patch));
        this.save();
    }


    private void modify(String key, String value) {
        this.properties.setProperty(key, value);
    }

    private void save() {
        try (FileOutputStream stream = new FileOutputStream(this.propertyFile)) {
            this.properties.store(stream, null);
        } catch (IOException e) {
            this.options.getMojo().getLog().error("Error, failed to save file.");
        }
    }

}
