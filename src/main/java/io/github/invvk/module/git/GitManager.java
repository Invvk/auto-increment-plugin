package io.github.invvk.module.git;

import io.github.invvk.Options;
import io.github.invvk.module.Manager;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;

public class GitManager extends Manager {

    private Git git;

    private String commit = "null", branch = "null";
    private int time = 0;

    private boolean gitEnabled;

    public GitManager(Options options) {
        super(options);
    }

    @Override
    public void init() throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(options.getGitDir())
                .readEnvironment()
                .findGitDir()
                .build();
        this.git = new Git(repo);

        this.gitEnabled = true;
    }

    public boolean isGitEnabled() {
        return gitEnabled;
    }

    public void load() {
        this.fetchCommitNTime();
        this.fetchBranch();
    }

    private void fetchBranch() {
        if (!gitEnabled)
            return;
        try {
            this.branch = this.git.getRepository().getBranch();
        } catch (IOException e) {
            this.options.getMojo().getLog().error("Failed to get branch name, missing repository?");
        }
    }

    private void fetchCommitNTime() {
        if (!gitEnabled)
            return;
        try {
            RevCommit latest = git.log().setMaxCount(1).call().iterator().next();
            this.commit = latest.getName();
            this.time = latest.getCommitTime();
        } catch (GitAPIException e) {
            this.options.getMojo().getLog().error("Operation Failed to obtain commit hash and time, missing HEAD?");
        }
    }

    public String getCutCommit() {
        if (commit.length() < this.options.getCommitLength())
            return commit;

        return commit.substring(0, this.options.getCommitLength());
    }

    public String getCommit() {
        return commit;
    }

    public int getTime() {
        return time;
    }

    public String getBranch() {
        return branch;
    }
}
