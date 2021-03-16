package environments;

public abstract class Environment {
    
    protected String pmdPath;
    protected String repositoryPath;

    Environment(String pmdPath, String repositoryPath){
        this.pmdPath = pmdPath;
        this.repositoryPath = repositoryPath;
    }


    public String getPmdPath() {
        return pmdPath;
    }
    public String getRepositoryPath() {
        return repositoryPath;
    }

    public abstract String executePMD();
    public abstract String executeCPD();
    public abstract String processBuilderApp();
    public abstract String argument(); 

}
