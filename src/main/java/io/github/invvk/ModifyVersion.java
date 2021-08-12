package io.github.invvk;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

@Mojo(name = "modifyVersion", defaultPhase = LifecyclePhase.INITIALIZE)
public class ModifyVersion extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter
    protected File gitDirectory;

    @Parameter(defaultValue = "data")
    protected String storageName;

    @Parameter(defaultValue = "false")
    protected boolean includeHash;

    @Parameter(defaultValue = "SHORT")
    protected HashMode hashMode;

    @Parameter(defaultValue = "false")
    protected boolean includePatch;

    @Parameter(defaultValue = "9")
    protected int patchLimit;

    @Parameter(defaultValue = "9")
    protected int minorLimit;

    @Parameter(defaultValue = "false", property = "aip.skip")
    protected boolean skipUpdating;

    @Parameter(defaultValue = "false", property = "aip.splitmode")
    protected boolean splitMode;

    @Parameter(defaultValue = "false", property = "aip.log")
    protected boolean log;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        VersionBuilder builder = new VersionBuilder.Builder(new VersionSettings(this)).build();
        if (builder == null)
            throw new MojoFailureException("Builder failed");

        this.project.getProperties().put("aip.version.full", builder.getFinalVersion());


        log("full version is " + builder.getFinalVersion());

        if (splitMode) {
            this.project.getProperties().put("aip.version.major", builder.getMajor());
            this.project.getProperties().put("aip.version.minor", builder.getMinor());
            this.project.getProperties().put("aip.version.patch", builder.getMajor());
            this.project.getProperties().put("aip.version.commit", builder.getCommitHash());

            log("major version is " + builder.getMajor());
            log("minor version is " + builder.getMinor());
            log("patch version is " + builder.getPatchNum());
            log("commit hash is " + builder.getCommitHash());
        }
    }

    public void log(String message) {
        if (log)
            getLog().info(message);
    }

    public void error(String message, Throwable e) {
        if (log)
            getLog().error(message, e);
    }

}
