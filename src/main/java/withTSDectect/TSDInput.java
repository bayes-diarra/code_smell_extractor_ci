package withTSDectect;

public class TSDInput {
    

    private String projectName, testFilePath, ProdFilePath;

    public TSDInput (String projectName, String testFilePath, String ProdFilePath){

        this.projectName = projectName;
        this.testFilePath =testFilePath;
        this.projectName = ProdFilePath;
    }

    public TSDInput (String projectName, String testFilePath){

        this.projectName = projectName;
        this.testFilePath =testFilePath;
    }

    public String getProdFilePath() {
        return ProdFilePath;
    }
    public String getProjectName() {
        return projectName;
    }
    public String getTestFilePath() {
        return testFilePath;
    }
    public void setProdFilePath(String prodFilePath) {
        ProdFilePath = prodFilePath;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public void setTestFilePath(String testFilePath) {
        this.testFilePath = testFilePath;
    }

    @Override
	public String toString() {
		return this.projectName+","+this.testFilePath+"\n";
	}
}
