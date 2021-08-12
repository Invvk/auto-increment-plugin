package me.invvk;

import java.io.File;

public class VersionSettings {

    private final ModifyVersion instance;

    public VersionSettings(ModifyVersion instance) {
        this.instance = instance;
    }

    public ModifyVersion getInstance() {
        return instance;
    }

    public File getBasedir() {
        return instance.project.getBasedir();
    }

    public File getGit() {
        return instance.gitDirectory;
    }

    public String getStorageName() {
        return instance.storageName;
    }

    public boolean isIncludePatch() {
        return this.instance.includePatch;
    }

    public boolean isIncludeHash() {
        return this.instance.includeHash;
    }

    public boolean isShortHash() {
        return this.instance.hashMode == HashMode.SHORT;
    }

    public boolean isSkipUpdating() {
        return this.instance.skipUpdating;
    }

    public int getBuildNumberLimit() {
        return this.instance.patchLimit;
    }

    public int getMinorLimit() {
        return this.instance.minorLimit;
    }

}
