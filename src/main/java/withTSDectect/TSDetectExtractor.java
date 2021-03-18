package withTSDectect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import environments.Environment;
import utilities.Build;
import utilities.Utility;

public class TSDetectExtractor extends Thread {
    

    
    private String project;
    private List<Build> project_builds;
    private Environment env;
    private StringBuilder codeSmells = new StringBuilder("");
    
    private ArrayList<TSDInput> pathList = new ArrayList<>();

    public TSDetectExtractor(String projectName, List<Build> project_builds, Environment env) {
        this.project = projectName;
        this.project_builds = project_builds;
        this.env = env;
    }

    @Override
    public void run() {

        ArrayList<TSDInput> list =new ArrayList<>();
        try {
            System.out.println("**********   "+project+"   **********");
            int i =1;
            System.out.println(LocalTime.now());
            Utility.cloneProject( project, env.getRepositoryPath());//clone GIT
            for (Build build : project_builds) {
                
                System.out.println(i++ +": "+project+" ********** BUILD (" + build.getBuildId() + ")");
                list = extractTS(build);
                if(!list.equals(null)) {
                    pathList.addAll(list);
                }
            }
            
            for (TSDInput item : pathList) {
                if(!item.getProjectName().equals(null)){
                    codeSmells.append(item.toString());
                    System.out.println(item.toString());
                }
            }
            int aleatoire = ThreadLocalRandom.current().nextInt();
            Utility.writeInCSV(codeSmells,project.replace("/", "-")+"_TSDinput"+aleatoire+".csv");
            System.out.println("*******Number of to analyse : "+pathList.size()+"  *********");
            
            runTSD(" '"+project.replace("/", "-")+"_TSDinput"+aleatoire+".csv'");// run tsDetect

            System.out.println(LocalTime.now());
        
            // delete the project directory
            try {
                FileUtils.deleteDirectory(new File(env.getRepositoryPath()));
            } catch (final IOException e) {
                System.err.println("Please delete the directory " + env.getRepositoryPath() + project + " manually");
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<TSDInput> extractTS(Build build )throws IOException {
        String projectPath = env.getRepositoryPath() + project;
        String commit = build.getCommits().get(build.getNbrCommits() - 1);// extract code version at the time of the last


        // built commit
        String code_version = projectPath + "/versions_builds/" + build.getBuildId();

        new File(projectPath + "/versions_builds/").mkdir();
        new File(code_version).mkdir();
        /*
         * Here we are analyzing a commit. 
         * for that we use git worktree to extract the version of the code at the time of commit in order to analyze it.
         */
        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(),env.argument(),
                "cd " + projectPath + " ; " + "git worktree add " + code_version + " " + commit);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line2;
        while ((line2 = r.readLine()) != null) {
            if (line2.contains("fatal")) {
                System.out.println(project+": "+ Arrays.toString(builder.command().toArray()));
                System.out.println(" ********** ERROR IN COMMIT " + commit + ":\n" + line2);
            }
        }
        if (new File(code_version).exists()) {
            return getFilesChanged(project, commit, code_version);
        } else
            System.out.println("VERSION PROBLEM" + Arrays.toString(builder.command().toArray()));

        // delete the version
        
        return new ArrayList<>();
    }


    private ArrayList<TSDInput> getFilesChanged(String projectName,String commit, String projectPath) throws IOException{

        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(), env.argument(), "cd " + projectPath + 
        " ;  git show --pretty='' --name-only "+ commit);
        
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        ArrayList<TSDInput> list = new ArrayList<>();
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            if (line.contains("Test.java")) { // header : lines,tokens,occurrences
                list.add(new TSDInput(projectName, projectPath+"/"+line));
            }
            if (line.contains("fatal")) {
                System.out.println(project+": "+ Arrays.toString(builder.command().toArray()));
                System.out.println(" ********** ERROR IN COMMIT " + commit + ":\n" + line);
                break;
            }
        }
        if(list.size() == 0){
            try {
                FileUtils.deleteDirectory(new File(projectPath));
            } catch (final IOException e) {
            }
        }
        return list;
    }


    private void runTSD(String file) throws IOException{
        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(), env.argument(), env.executeTSD()+file);
        builder.redirectErrorStream(true);
        builder.start();
    }

}
