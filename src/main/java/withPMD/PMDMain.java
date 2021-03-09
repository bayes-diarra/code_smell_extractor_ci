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
            String system = args[0];
            String inputfile = args[1];

            try {
                if ((system.equals("-l")) && (inputfile.contains(".csv"))) {
                     
                    env = new Linux("/$HOME/pmd/bin", "$HOME/tmpGitRepository" ); 
                    hashmap_build = Utility.getBuilds(inputfile);
                    for (String proj : hashmap_build.keySet()) {
                        PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                        pmd.start();
                    }

                } else if ((system.equals("-w")) && (inputfile.contains(".csv"))) {
                    env = new Windows("C:/pmd/bin", "C:/tmpGitRepository" ); 
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
