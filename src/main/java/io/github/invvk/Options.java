package io.github.invvk;

import java.io.File;

public class Options {

    private final AIPMojo mojo;

    private final boolean patch, commit, pump;
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
        this.fileName = mojo.fileName;
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

    public File getGitDir() {
        return gitDir;
    }

    public String getFileName() {
        return fileName;
    }
}
