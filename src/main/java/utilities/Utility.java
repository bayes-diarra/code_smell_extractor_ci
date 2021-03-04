package utilities;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static final String CSV_DELIMTER = ",";
    public static final String TABLE_DELIMTER = "#";
    // If your are using linux environment
    public static final String PATH_PMD = "$HOME/pmd/bin/";
    public static final String PATH_REPOSITORY = "/home/bayesdiarra/gitRepository/";


    /**
     * Permit to write in CSV file.
     * @param sb
     * @param file
     */
    public static void writeInCSV(StringBuilder sb, String file) {

        if (new File(file).exists()) {
            System.out.println(file + " the file already exists!");
            System.out.println("If you forgot to delete it, you can copy form console:\n"+sb.toString());
        } else {
            try {
                final PrintWriter writer = new PrintWriter(new File(file));
                writer.write(sb.toString());
                writer.close();
                System.out.println("your file is ready.. please check " + file);
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Permit to clone project from git.
     * @param project
     */
    public static void cloneProject(String project) {
        try {
            File f = new File(PATH_REPOSITORY + project);
            if (!f.exists()) {
                System.out.println("cloning " + project + " project..");
                try {
                    Git.cloneRepository().setBranch("master").setDirectory(f).setURI("https://github.com/" + project)
                            .call();
                } catch (org.eclipse.jgit.dircache.InvalidPathException e) {
                    e.printStackTrace();
                }
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * permit to read data contained in CSV file.
     * @param pathToData
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static List<String> readCSVData(String pathToData) throws IOException {
        List<String> res = new ArrayList<String>();
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(pathToData));
        br.readLine();// header
        while ((line = br.readLine()) != null) {
            res.add(line);
        }
        return res;
    }
}
