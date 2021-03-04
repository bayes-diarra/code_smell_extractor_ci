package withPMD;

import utilities.Utility;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PMDExtractor extends Thread{

    String project;
    List<Build> project_builds;
    public StringBuilder codeSmells = new StringBuilder("buildID,build_Failed,duplicatedCode,GodClass,GodMethod,CyclomaticComplexity,ExcessiveParameterList,DataClass\n");
    

    ArrayList<Report> list = new ArrayList<>();
    public PMDExtractor(String projectName, List<Build> project_builds) {
        this.project = projectName;
        this.project_builds = project_builds;
    }

    public StringBuilder getCodeSmells() {
        return codeSmells;
    }


    @Override
    public void run() {
        try {
            System.out.println("**********   "+project+"   **********");
            Utility.cloneProject( project);//clone GIT
            for (Build build : project_builds) {
                System.out.println(Report.getNumber()+": "+project+" ********** BUILD (" + build.buildId + ")");
                extractCS(build);
            }
            Utility.writeInCSV(codeSmells, project.replaceAll("/", "-") + "_data.csv");
            // delete the project directory
            try {
                FileUtils.deleteDirectory(new File(Utility.PATH_REPOSITORY + project));
            } catch (final IOException e) {
                System.err.println("Please delete the directory " + Utility.PATH_REPOSITORY + project + " manually");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * we use Command line to run PMD
     */
    public void extractCS(Build build) throws IOException {
        String local_dir = Utility.PATH_REPOSITORY + project;
        String commit = build.commits.get(build.getNbrCommits() - 1);// extract code version at the time of the last
        // built commit
        String code_version = local_dir + "/versions_builds/" + build.buildId;

        new File(local_dir + "/versions_builds/").mkdir();
        new File(code_version).mkdir();
        /*
         * use git worktree to extract the code version at the time of a commit using
         * the command line PS. you should install "git" on your machine to run the
         * command. You should also download PMD and set the path of the bin folder (
         * PMD_DIR in Utils.java)
         */
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                "cd " + local_dir + " && " + "git worktree add " + code_version + " " + commit);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line2;
        while ((line2 = r.readLine()) != null) {
            if (line2.contains("fatal")) {
                System.out.println(project+": "+ Arrays.toString(builder.command().toArray()));
                System.out.println(project+" ********** ERROR IN COMMIT " + commit + ":\n" + line2);
            }
        }
        if (new File(code_version).exists()) {
            // detect code smells
            codeSmells.append(
                    runPMD(code_version + "/ ", build.buildId, build.build_failed, project).toString());
        } else
            System.out.println("VERSION PROBLEM" + Arrays.toString(builder.command().toArray()));

        // delete the version
        try {
            FileUtils.deleteDirectory(new File(code_version));
        } catch (final IOException e) {
        }
    }

    private StringBuilder runPMD(String dir, String buildID, int build_failed, String project) throws IOException {
        /*
         *
         * List of used CS:
         */
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                "cd " + Utility.PATH_PMD + " && " + " ./run.sh pmd -dir " + dir + " -f csv -R "
                        + "category/java/design.xml/GodClass" + ",category/java/design.xml/NPathComplexity"
                        + ",category/java/design.xml/CyclomaticComplexity"
                        + ",category/java/design.xml/DataClass"
                 + ",category/java/design.xml/ExcessiveParameterList"
                 +" -language java"
        );

        builder.redirectErrorStream(true);
        Process p = builder.start();
        StringBuilder codeSmells = new StringBuilder("");
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String[] details = null;
        Report report = new Report(buildID, build_failed);
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
            if (details[details.length-1].toString().contains("ExcessiveParameterList")) {
                report.setExcessiveParameterList(report.getExcessiveParameterList() + 1);
            }
            report.setDuplicatedCode(duplicatedCode(dir,buildID,build_failed));            
        }
        report.setDuplicatedCode(duplicatedCode(dir, buildID, build_failed));
        codeSmells.append(report.getBuildID() + "," +  report.getBuild_Failed() + "," + report.getDuplicatedCode() + ","+ report.getGodClass()+
                        ","+report.getGodMethod()+"," +  report.getCyclomaticComplexity() + ","+report.getExcessiveParameterList()+","+ report.getDataClass()+ '\n');
        

        // delete extracted version
        try {
            FileUtils.deleteDirectory(new File(Utility.PATH_REPOSITORY+ project + "/version/"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return codeSmells;
    }

  //  "buildID,build_Failed,duplicatedCode,GodClass,GodMethod,CyclomaticComplexity,DataClass\n"

    private int duplicatedCode(String dir, String buildID, int build_failed) throws IOException {

        ProcessBuilder builder = new ProcessBuilder("bash", "-c", "cd "+ Utility.PATH_PMD
                + " && ./run.sh cpd --minimum-tokens 100 --files " + dir + " --format xml");
        // System.out.println( Arrays.toString(builder.command().toArray()));
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
