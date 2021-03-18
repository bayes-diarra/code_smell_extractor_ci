import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import environments.Environment;
import environments.Linux;
import environments.Windows;
import utilities.Build;
import utilities.Utility;
import withPMD.PMDExtractor;
import withTSDectect.TSDetectExtractor;

public class Main {
    public static void main(String[] args) throws IOException {
    
    
        
        HashMap<String, List<Build>> hashmap_build=new HashMap<>();
        Environment env = null;
        // start the Code Smells detection using PMD
        if (args.length == 3) {
            String system = args[0];
            String inputfile = args[2];
            String analysisType = args[1];

            try {
                
                //linux
                if(system.equals("-l") && (inputfile.contains(".csv"))) {
                    if(analysisType.equalsIgnoreCase("-pmd")) {
                        env = new Linux("$HOME/pmd/bin", "$HOME/tmpGitRepository" );
                        for (String proj : hashmap_build.keySet()) {
                            PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                            pmd.start();
                        }                         
                    }

                    if(analysisType.equalsIgnoreCase("-tsd")) {
                        env = new Linux("$HOME/tmpGitRepository" );
                        for (String proj : hashmap_build.keySet()) {
                            TSDetectExtractor tsd = new TSDetectExtractor(proj, hashmap_build.get(proj), env);
                            tsd.start();
                        }
                    }
                }
                

                //windows
                if(system.equals("-w") && (inputfile.contains(".csv"))) {
                    if(analysisType.equalsIgnoreCase("-pmd")) {
                        env = new Windows("C:/pmd/bin", "C:/tmpGitRepository" );
                        for (String proj : hashmap_build.keySet()) {
                            PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj), env);
                            pmd.start();
                        }                         
                    }

                    if(analysisType.equalsIgnoreCase("-tsd")) {
                        env = new Windows("C:/tmpGitRepository");
                        for (String proj : hashmap_build.keySet()) {
                            TSDetectExtractor tsd = new TSDetectExtractor(proj, hashmap_build.get(proj), env);
                            tsd.start();
                        }
                    }
                }                
                

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez entrer les bons paramètres");
        }
    }

}
