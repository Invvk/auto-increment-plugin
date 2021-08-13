package io.github.invvk;

import io.github.invvk.worker.MojoWorker;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

@Mojo(name = "modifyVersion", defaultPhase = LifecyclePhase.INITIALIZE)
public class AIPMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/.git/", property = "aip.gitDir")
    protected File gitDirectory;

    @Parameter(defaultValue = "data")
    protected String fileName;

    @Parameter(defaultValue = "7")
    protected int commitLength;

    @Parameter(defaultValue = "false")
    protected boolean commit;

    @Parameter(defaultValue = "true")
    protected boolean patch;

    @Parameter(defaultValue = "9")
    protected int patchLimit;

    @Parameter(defaultValue = "9")
    protected int minorLimit;

    @Parameter(defaultValue = "true", property = "aip.pump")
    protected boolean pump;

    public MavenProject getProject() {
        return project;
    }

    @Override
    public void execute() {
        new MojoWorker(new Options(this)).execute();
    }

}
