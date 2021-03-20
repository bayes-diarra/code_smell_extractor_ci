package environments;

public class Windows  extends Environment {

    public Windows (String pmdPath, String repositoryPath) {
        super(pmdPath, repositoryPath);
    }

    public Windows (String repositoryPath) {
        super(repositoryPath);
    }

    @Override
    public String executePMD() {
        return " .\\pmd.bat -d ";
    }

    @Override
    public String executeCPD() {
        return " .\\cpd.bat ";
    }

    @Override
    public String processBuilderApp() {
        return "powershell.exe";
    }

    @Override
    public String argument() {
        return "/c";
    }

    @Override
    public String executeTSD() {
        
        return "java -jar .\\TestSmellDetector-ci.jar ";
    }
}

