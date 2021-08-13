package io.github.invvk;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class Options {

    private final AIPMojo mojo;

    private final boolean patch, commit, pump, disableGit;
    private final int patchLimit, minorLimit, commitLength;
    private final File gitDir;
    private final String fileName;

    public Options(AIPMojo mojo) {
        this.mojo = mojo;

        // initializing settings from maven

        this.patch = mojo.patch;
        this.commit = mojo.commit;
        this.pump = mojo.pump;
        this.patchLimit = mojo.patchLimit;
        this.minorLimit = mojo.minorLimit;
        this.commitLength = mojo.commitLength;
        this.gitDir = mojo.gitDirectory;
        this.disableGit = mojo.disableGit;
        this.fileName = mojo.fileName.endsWith(".properties") ? mojo.fileName : mojo.fileName + ".properties";
    }

    public AIPMojo getMojo() {
        return mojo;
    }

    public boolean isPatch() {
        return patch;
    }

    public boolean isCommit() {
        return commit;
    }

    public boolean isPump() {
        return pump;
    }

    public int getPatchLimit() {
        return patchLimit;
    }

    public int getMinorLimit() {
        return minorLimit;
    }

    public int getCommitLength() {
        return commitLength;
    }

    public boolean isGitDisabled() {
        return disableGit;
    }

    public File getGitDir() {
        return gitDir;
    }

    public String getFileName() {
        return FilenameUtils.getName(this.fileName);
    }
}
