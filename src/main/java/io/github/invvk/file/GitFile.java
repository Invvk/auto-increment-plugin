package io.github.invvk.file;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GitFile {

    private String commitHash;
    private String shortCommitHash;

    public GitFile(File file) throws MojoExecutionException {
        if (!file.exists())
            throw new MojoExecutionException("The git file can't be found");
        // checking if HEAD exists
        File head_file = new File(file, "HEAD");
        if (!head_file.exists())
            throw new MojoExecutionException("HEAD file can't be found, please check the provided git file");

        // attempting to obtain the current reference
        String reference = "";
        try (FileReader freader = new FileReader(head_file);
             BufferedReader reader = new BufferedReader(freader)) {
            String ref = reader.readLine();
            if (ref.startsWith("ref:")) {
                // ref has been found
                reference = ref.split(":")[1].replace(" ", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!reference.isEmpty()) {
            // attempting to obtain the reference file
            final File reference_file = new File(file, reference);
            if (reference_file.exists()) {
                try (FileReader freader = new FileReader(reference_file);
                     BufferedReader reader = new BufferedReader(freader)) {
                    this.commitHash = reader.readLine();
                    this.shortCommitHash = this.commitHash.substring(0, 7);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getCommitHash() {
        return commitHash;
    }

    public String getShortCommitHash() {
        return shortCommitHash;
    }

}
