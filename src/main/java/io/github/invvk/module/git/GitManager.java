package io.github.invvk.module.git;

import io.github.invvk.Options;
import io.github.invvk.module.Manager;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;

public class GitManager extends Manager {

    private Git git;

    private String commit, branch;
    private int time;

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
    }

    public void load() {
        this.fetchCommitNTime();
        this.fetchBranch();
    }

    private void fetchBranch() {
        try {
            this.branch = this.git.getRepository().getBranch();
        } catch (IOException e) {
            this.options.getMojo().getLog().error("Failed to get branch name, missing repository?", e);
        }
    }

    private void fetchCommitNTime() {
        try {
            RevCommit latest = git.log().setMaxCount(1).call().iterator().next();
            this.commit = latest.getName();
            this.time = latest.getCommitTime();
        } catch (GitAPIException e) {
            this.options.getMojo().getLog().error("Operation Failed due to missing HEAD?", e);
        }
    }

    public String getCutCommit() {
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
