package io.github.invvk.worker;

import io.github.invvk.Options;
import io.github.invvk.module.autoInc.AIManager;
import io.github.invvk.module.git.GitManager;
import org.apache.maven.project.MavenProject;

import java.io.IOException;

public class MojoWorker {

    private final Options options;

    public MojoWorker(Options options) {
        this.options = options;
    }


    public void execute() {
        if (options.isGitDisabled()) {
            executeWithoutGit();
        } else {
            executeWithGit();
        }
    }

    private void executeWithoutGit() {
        final StringBuilder sb = new StringBuilder();

        final AIManager ai = new AIManager(this.options);

        // AIManager stuff
        ai.init();

        sb.append(ai.getMajor()).append(".").append(ai.getMinor());

        if (this.options.isPatch())
            sb.append(".").append(ai.getPatch());

        // setting up all properties in maven
        MavenProject project = this.options.getMojo().getProject();
        project.getProperties().put("aip.version.full", sb.toString());
        project.getProperties().put("aip.version.major", String.valueOf(ai.getMajor()));
        project.getProperties().put("aip.version.minor", String.valueOf(ai.getMinor()));
        project.getProperties().put("aip.version.patch", String.valueOf(ai.getPatch()));

        if (this.options.isPump())
            ai.pumpVersion();
    }

    private void executeWithGit() {
        final StringBuilder sb = new StringBuilder();

        final AIManager ai = new AIManager(this.options);

        // AIManager stuff
        ai.init();

        sb.append(ai.getMajor()).append(".").append(ai.getMinor());

        if (this.options.isPatch())
            sb.append(".").append(ai.getPatch());

        GitManager git = new GitManager(this.options);
        try {
            git.init();
        } catch (IOException e) {
            this.options.getMojo().getLog().error("skipping git manager, failed to retrieve current git information");
        }

        git.load();
        if (options.isCommit())
            sb.append("-").append(git.getCutCommit());

        // setting up all properties in maven
        MavenProject project = this.options.getMojo().getProject();
        project.getProperties().put("aip.version.full", sb.toString());
        project.getProperties().put("aip.version.major", String.valueOf(ai.getMajor()));
        project.getProperties().put("aip.version.minor", String.valueOf(ai.getMinor()));
        project.getProperties().put("aip.version.patch", String.valueOf(ai.getPatch()));

        if (git.isGitEnabled()) {
            project.getProperties().put("aip.git.commit", git.getCommit());
            project.getProperties().put("aip.git.time", String.valueOf(git.getTime()));
            project.getProperties().put("aip.git.branch", git.getBranch());
        }

        if (this.options.isPump())
            ai.pumpVersion();
    }

}
