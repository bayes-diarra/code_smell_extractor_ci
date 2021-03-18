package withPMD;
import utilities.Utility;
import utilities.Build;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import environments.Environment;
import environments.Linux;
import environments.Windows;

public class PMDMain {

    public static void main(String[] args) throws IOException {
        
        String data = "data.csv"; //jackrabbit-oak
        HashMap<String, List<Build>> hashmap_build=null;
        Environment env = null;
        /*ArrayList<String> projects_list= new ArrayList<>();
        projects_list.add("CloudifySource/cloudify");projects_list.add("Graylog2/graylog2-server");projects_list.add("HubSpot/Singularity");
        projects_list.add("SonarSource/sonarqube");projects_list.add("apache/jackrabbit-oak");projects_list.add("gradle/gradle");
        projects_list.add("orbeon/orbeon-forms");projects_list.add("owncloud/android");projects_list.add("perfectsense/brightspot-cms");projects_list.add("square/okhttp");*/
        // start the Code Smells detection using PMD
        
        try {

            //env = new Windows("C:/pmd/bin", "C:/tmpGitRepository/");  
            env = new Linux("/home/bayesdiarra/pmd/bin", "/home/bayesdiarra/tmpGitRepositorypmd/");
             
            hashmap_build = Utility.getBuilds(data);
            for (String proj : hashmap_build.keySet()) {
                PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                pmd.start();
            }
           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
