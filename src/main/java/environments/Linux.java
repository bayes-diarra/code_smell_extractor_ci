package environments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Linux extends Environment {

    public Linux(String pmdPath, String repositoryPath) {
        super(pmdPath, repositoryPath);
    }

    public Linux(String repositoryPath) {
        super(repositoryPath);
    }

    @Override
    public String executePMD() {
        return " ./run.sh pmd -d ";
    }

    @Override
    public String executeCPD() {
        return " ./run.sh cpd ";
    }

    @Override
    public String processBuilderApp() {
        return "bash";
    }

    @Override
    public String argument() {
        return "-c";
    }

    @Override
    public String executeTSD() {
        
        return "java -jar ./TestSmellDetector-ci.jar ";
    }

    public static String getUserHome() throws IOException{

        ProcessBuilder builder = new ProcessBuilder("bash","-c","eval echo ~$USER");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = r.readLine();
        return line;
    }


} 