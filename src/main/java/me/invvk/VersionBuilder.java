package me.invvk;

import me.invvk.file.GitFile;
import me.invvk.file.PropertyFile;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;

public class VersionBuilder {

    private final String finalVersion;

    private VersionBuilder(Builder builder) {
        this.finalVersion = builder.finalVersion;
    }

    public String getFinalVersion() {
        return finalVersion;
    }

    public static final class Builder {
        private String finalVersion;
        private final VersionSettings settings;

        public Builder(VersionSettings settings) {
            this.settings = settings;
        }

        public VersionBuilder build() {
            PropertyFile file;
            try {
                file = new PropertyFile(this.settings.getBasedir(), this.settings.getStorageName());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(file.getMajor()).append(".").append(file.getMinor());

            if (this.settings.isIncludeBuildNumber())
                builder.append(".").append(file.getBuildNumber());

            if (this.settings.isIncludeHash()) {
                try {
                    GitFile git = new GitFile(this.settings.getGit());
                    builder.append("-").append(this.settings.isShortHash() ? git.getShortCommitHash() : git.getCommitHash());
                } catch (MojoExecutionException e) {
                    e.printStackTrace();
                }
            }
            this.finalVersion = builder.toString();

            // Skip updating and only generate the file with the current version
            if (!this.settings.isSkipUpdating()) {
                // updating version
                int currMajor = Integer.parseInt(file.getMajor());
                int currMinor = Integer.parseInt(file.getMinor());
                int currBuildNum = Integer.parseInt(file.getBuildNumber());

                if (currBuildNum >= settings.getBuildNumberLimit()) {
                    currBuildNum = 0;
                    if (currMinor >= settings.getMinorLimit()) {
                        currMinor = 0;
                        currMajor += 1;
                    } else
                        currMinor += 1;

                } else {
                    currBuildNum += 1;
                }

                file.modify(file.MAJOR, String.valueOf(currMajor));
                file.modify(file.MINOR, String.valueOf(currMinor));
                file.modify(file.BUILD_NUMBER, String.valueOf(currBuildNum));
                // saving
                file.save();
            }
            return new VersionBuilder(this);
        }
    }
}
