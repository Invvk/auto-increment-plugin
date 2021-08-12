package me.invvk;

import me.invvk.file.GitFile;
import me.invvk.file.PropertyFile;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;

public class VersionBuilder {

    private final String finalVersion,major,minor, patch,commitHash;

    private VersionBuilder(Builder builder) {
        this.finalVersion = builder.finalVersion;
        this.major = builder.major;
        this.minor = builder.minor;
        this.patch = builder.patch;
        this.commitHash = builder.commitHash;
    }

    public String getFinalVersion() {
        return finalVersion;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getPatchNum() {
        return patch;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public static final class Builder {
        private String finalVersion,major,minor, patch,commitHash;
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
            this.major = file.getMajor();
            this.minor = file.getMinor();
            this.patch = file.getPatch();
            builder.append(file.getMajor()).append(".").append(file.getMinor());

            if (this.settings.isIncludePatch())
                builder.append(".").append(file.getPatch());

            try {
                GitFile git = new GitFile(this.settings.getGit());
                this.commitHash = this.settings.isShortHash() ? git.getShortCommitHash() : git.getCommitHash();
                if (this.settings.isIncludeHash())
                    builder.append("-").append(this.commitHash);
            } catch (MojoExecutionException e) {
                e.printStackTrace();
            }

            this.finalVersion = builder.toString();

            // Skip updating and only generate the file with the current version
            if (!this.settings.isSkipUpdating()) {
                // updating version
                int currMajor = Integer.parseInt(file.getMajor());
                int currMinor = Integer.parseInt(file.getMinor());
                int currPatchNum = Integer.parseInt(file.getPatch());

                if (currPatchNum >= settings.getBuildNumberLimit()) {
                    currPatchNum = 0;
                    if (currMinor >= settings.getMinorLimit()) {
                        currMinor = 0;
                        currMajor += 1;
                    } else
                        currMinor += 1;

                } else {
                    currPatchNum += 1;
                }

                file.modify(file.MAJOR, String.valueOf(currMajor));
                file.modify(file.MINOR, String.valueOf(currMinor));
                file.modify(file.PATCH, String.valueOf(currPatchNum));
                // saving
                file.save();
            }
            return new VersionBuilder(this);
        }
    }
}
