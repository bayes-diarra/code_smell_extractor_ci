package withPMD;


import utilities.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import environments.Environment;
import environments.Linux;
import environments.Windows;

public class PMDMain {

     


    public static void main(String[] args) throws IOException {
        
        String data = "data.csv";
        HashMap<String, List<Build>> hashmap_build=null;
        Environment env = null;
        // start the Code Smells detection using PMD
        
        try {

             env = new Windows("C:/pmd/bin", "C:/tmpGitRepository" ); 
             hashmap_build = Utility.getBuilds(data);
            for (String proj : hashmap_build.keySet()) {
                PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                pmd.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (args.length == 3) {
            String pmdPath = args[0];
            String system = args[1];
            String inputfile = args[2];

            try {
                if ((system.equals("-l")) && (inputfile.contains(".csv")) && !pmdPath.equals(null) ) {
                     
                    env = new Linux(pmdPath, "$HOME/tmpGitRepository" ); 
                    hashmap_build = Utility.getBuilds(inputfile);
                    for (String proj : hashmap_build.keySet()) {
                        PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                        pmd.start();
                    }

                } else if ((system.equals("-w")) && (inputfile.contains(".csv")) && !pmdPath.equals(null) ) {
                    env = new Windows(pmdPath, "C:/tmpGitRepository" ); 
                    hashmap_build = Utility.getBuilds(inputfile);
                    for (String proj : hashmap_build.keySet()) {
                        PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                        pmd.start();
                    }
                } else {
                    System.out.println("Veuillez entrer les bons paramètres");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez entrer les bons paramètres");
        }
    }
}
