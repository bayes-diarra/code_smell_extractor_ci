package utilities;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Utility {

    public static final String CSV_DELIMTER = ",";
    public static final String TABLE_DELIMTER = "#";

    /**
     * Permit to write in CSV file.
     * @param sb
     * @param file
     */
    public static void writeInCSV(StringBuilder sb, String file) {

        if (new File(file).exists()) {
            System.out.println(file + " the file already exists!");
            System.out.println("If you forgot to delete it, you can copy it form console:\n"+sb.toString());
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
    public static void cloneProject(String project, String repoPath) {
        try {
            File f = new File(repoPath + project);
            if (!f.exists()) {
                System.out.println("cloning " + project + " project..");
                try {
                    Git.cloneRepository()
                    .setBranch("master")
                    .setDirectory(f)
                    .setURI("https://github.com/" + project)
                    .call();
                            System.out.println("Repository cloned!");
                } catch (org.eclipse.jgit.dircache.InvalidPathException e) {
                    e.printStackTrace();
                    System.out.println("Error in cloning repository!");
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
        if(!pathToData.isEmpty()){}
        BufferedReader br = new BufferedReader(new FileReader(pathToData));
        br.readLine();// header
        while ((line = br.readLine()) != null) {
            res.add(line);
        }
        return res;
    }

    public static HashMap<String, List<Build>> getBuilds(String data){
        ///////////////////////////////// load build information://////////////////////////////////////////////
        /*
         * the csv file contains data from a project on github: gh_project_name:project name, tr_build_id:
         * build ID, build_successful: Build result (TRUE=passed, FALSE=failed),
         * git_all_built_commits: list of built commits separated by #
         */
        List<String> allbuiltCommits;
        HashMap<String, List<Build>> hashmap_build = new HashMap<>();// create a hashmap
        try {
            allbuiltCommits = Utility.readCSVData(data);
       // the data of all the studied projects

        
            String project_name ="";
            //  Data Extraction
            List<Build> builds_project = new ArrayList<>();
            for (String row : allbuiltCommits) {
                if (row!= null){
                    String[] infos = row.split(Utility.CSV_DELIMTER);
                    if(infos.length>=4){
                        if( infos[2].equalsIgnoreCase("false") || infos[2].equalsIgnoreCase("true")) {
                            project_name=infos[0];
                            int buildRes = infos[2].equalsIgnoreCase("true") ? 1 : 0;
                            builds_project.add(new Build(infos[0], infos[1], // project name and build ID
                                    buildRes, Arrays.asList(infos[3].split(Utility.TABLE_DELIMTER))// commits
                            ));
                        }
                    }
                }
            }


            hashmap_build.put(project_name, builds_project);
            

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Enable to read " + data);
        }
        return hashmap_build;
    }



}
