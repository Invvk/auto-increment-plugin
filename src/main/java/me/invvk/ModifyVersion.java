package me.invvk;

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

    @Parameter(defaultValue = "LONG")
    protected HashMode hashMode;

    @Parameter(defaultValue = "false")
    protected boolean includeBuildNumber;

    @Parameter(defaultValue = "9")
    protected int buildNumLimit;

    @Parameter(defaultValue = "9")
    protected int minorLimit;

    @Parameter(defaultValue = "false", property = "update.skip")
    protected boolean skipUpdating;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        VersionBuilder builder = new VersionBuilder.Builder(new VersionSettings(this)).build();
        if (builder == null)
            throw new MojoFailureException("Builder failed");
        this.project.getProperties().put("autoincrement.version", builder.getFinalVersion());
        getLog().info("autoincrement.version has been set to " + builder.getFinalVersion());
    }
}
