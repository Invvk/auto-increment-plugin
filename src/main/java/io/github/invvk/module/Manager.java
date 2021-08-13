package io.github.invvk.module;

import io.github.invvk.Options;

import java.io.IOException;

public abstract class Manager {

    protected final Options options;

    public Manager(Options options) {
        this.options = options;
    }

    public abstract void init() throws IOException;

}
