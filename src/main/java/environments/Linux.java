package environments;

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
        
        return "java -jar ./TestSmellDetector.jar ";
    }


} 