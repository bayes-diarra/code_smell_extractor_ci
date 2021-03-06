package withPMD;

import utilities.Utility;
import utilities.Build;
import org.apache.commons.io.FileUtils;

import environments.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PMDExtractor extends Thread{

    private String project;
    private List<Build> project_builds;
    private StringBuilder codeSmells = new StringBuilder("project_name,buildID,build_State,duplicatedCode,GodClass,GodMethod,CyclomaticComplexity,DataClass,File_Modified\n");
    private Environment env;

    public PMDExtractor(String projectName, List<Build> project_builds, Environment env) {
        this.project = projectName;
        this.project_builds = project_builds;
        this.env = env;
    }

    @Override
    public void run() {
        try {
            System.out.println("**********   "+project+"   **********");
            int i =1;
            System.out.println(LocalTime.now());
            Utility.cloneProject( project, env.getRepositoryPath());//clone GIT
            for (Build build : project_builds) {
            
                System.out.println(i++ +": "+project+" ********** BUILD (" + build.getBuildId() + ")");
                extractCS(build);
            }
            
            String aleatoire = String.valueOf(Calendar.getInstance().getTimeInMillis());

            Utility.writeInCSV(codeSmells, project.replace("/", "-")+"_PMDoutputData"+aleatoire+".csv");
            
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

    /*
     * we use Command line to run PMD
     */
    public void extractCS(Build build) throws IOException {
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
        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(), env.argument(),
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
        ArrayList<String> list = new ArrayList<>();
        if (new File(code_version).exists()) {
            list = getFilesChanged( commit, code_version);

            try {
                Report report = new Report(build.getProject_name(), build.getBuildId(), build.getBuild_State());
                report.setFileNumber(list.size());
                for (String item : list) {
                    report = runPMD(code_version, report, item);
                    report.setDuplicatedCode(duplicatedCode(code_version, item));
                }
                    
                codeSmells.append(report.getProject_name() + "," + report.getBuildID() + "," +  report.getBuild_State() + "," + report.getDuplicatedCode() + ","+ report.getGodClass()+
                ","+report.getGodMethod()+"," +  report.getCyclomaticComplexity() +","+ report.getDataClass()+ ","+ report.getFileNumber() + '\n');
                System.out.println(" **** Number of file to analyse : "+report.getFileNumber()+" ****");
                
                // delete the version
                FileUtils.deleteDirectory(new File(code_version));
            } catch (Exception e) {}

        } else
            System.out.println("VERSION PROBLEM" + Arrays.toString(builder.command().toArray()));
    }


    private ArrayList<String> getFilesChanged(String commit, String projectPath) throws IOException{

        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(), env.argument(), "cd " + projectPath + 
        " ;  git show --pretty='' --name-only "+ commit);
        
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        ArrayList<String> list = new ArrayList<>();
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            if (line.contains(".java")) { // header : lines,tokens,occurrences
                list.add(line);
            }
            if (line.contains("fatal")) {
                System.out.println(project+": "+ Arrays.toString(builder.command().toArray()));
                System.out.println(project+" ********** ERROR IN COMMIT " + commit + ":\n" + line);
                break;
            }
        }
        return list;
    }



    private Report runPMD(String projectPath, Report report, String filePath) throws IOException {
        /*
         *
         * List of used CS:
         */
        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(), env.argument(),"cd "+ env.getPmdPath()+ 
        " ; " + env.executePMD() + projectPath+"/"+filePath + " -f csv -R "
                + "category/java/design.xml/GodClass" 
                + ",category/java/design.xml/NPathComplexity"
                + ",category/java/design.xml/CyclomaticComplexity"
                + ",category/java/design.xml/DataClass"
                +" -language java"
        );

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String[] details = null;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            line = line.replaceAll("NPathComplexity", "GodMethod")
                    .replaceAll("Problem,Package,File,Priority,Line,Description,Rule set,Rule", "");
            details = line.split(",");
             // header : "Problem","Package","File","Priority","Line","Description","Rule set","Rule"

            
            if (details[details.length-1].toString().contains("GodClass")) {
                report.setGodClass(report.getGodClass() + 1);
            }
            if (details[details.length-1].toString().contains("GodMethod")) {
                report.setGodMethod(report.getGodMethod() + 1);
            }
            if (details[details.length-1].toString().contains("CyclomaticComplexity")) {
                report.setCyclomaticComplexity(report.getCyclomaticComplexity() + 1);
            }
            if (details[details.length-1].toString().contains("DataClass")) {
                report.setDataClass(report.getDataClass() + 1);
            }
                        
        }
        
        
        return report;
    }

    private int duplicatedCode(String projectPath, String filePath) throws IOException {

        ProcessBuilder builder = new ProcessBuilder(env.processBuilderApp(), env.argument(),"cd "+ env.getPmdPath()+ 
        " ; " + env.executeCPD() + " --minimum-tokens 100 --files " + projectPath+"/"+filePath + " --format xml");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        int nbr = 0;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            if (line.contains("</duplication>")) { // header : lines,tokens,occurrences
                nbr++;
            }
        }

        return nbr;
    }


}
