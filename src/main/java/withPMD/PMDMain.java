package withPMD;


import utilities.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PMDMain {

    
    static String PROJECT_NAME = null;
    static String DATA = "data.csv";

    public static void main(String[] args) throws IOException {
        ///////////////////////////////// load build information://////////////////////////////////////////////
        /*
         * the csv file contains data from a project on github: gh_project_name:project name, tr_build_id:
         * build ID, build_successful: Build result (TRUE=passed, FALSE=failed),
         * git_all_built_commits: list of built commits separated by #
         */
        final List<String> allbuiltCommits = Utility.readCSVData(DATA);// the data of all the studied projects

        HashMap<String, List<Build>> hashmap_build = new HashMap<>();// create a hashmap

        //  Data Extraction
        List<Build> builds_project = new ArrayList<>();
        for (String row : allbuiltCommits) {
            if (row!= null){
                String[] infos = row.split(Utility.CSV_DELIMTER);
                if( infos[2].equalsIgnoreCase("false") || infos[2].equalsIgnoreCase("true")) {
                    PROJECT_NAME=infos[0];
                    int buildRes = infos[2].equalsIgnoreCase("false") ? 1 : 0;
                    builds_project.add(new Build(infos[1], // build ID
                            buildRes, Arrays.asList(infos[3].split(Utility.TABLE_DELIMTER))// commits
                    ));
                }
            }
        }
        
        hashmap_build.put(PROJECT_NAME, builds_project);
        // start the Code Smells detection using PMD
        for (String proj : hashmap_build.keySet()) {
            PMDExtractor pmd = new PMDExtractor(proj, hashmap_build.get(proj));
            pmd.start();
        }
    }
}
