package withPMD;

public class Report {
    private String project_name; 
    private String buildID;
    private int build_state;
    private int duplicatedCode,godClass,godMethod,cyclomaticComplexity,dataClass,fileNumber;
    

    public Report(String project_name, String buildID, int build_state) {
        this.project_name = project_name;
        this.buildID = buildID;
        this.build_state = build_state;
        this.duplicatedCode = 0;
        this.godClass = 0;
        this.godMethod = 0;
        this.cyclomaticComplexity = 0;
        this.dataClass = 0;
        this.fileNumber =0;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public int getBuild_State() {
        return build_state;
    }

    public void setBuild_State(int build_state) {
        this.build_state = build_state;
    }

    public int getGodClass() {
        return godClass;
    }

    public void setGodClass(int godClass) {
        this.godClass = godClass;
    }

    public int getGodMethod() {
        return godMethod;
    }

    public void setGodMethod(int godMethod) {
        this.godMethod = godMethod;
    }

    public int getCyclomaticComplexity() {
        return this.cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public int getDuplicatedCode() {
        return duplicatedCode;
    }

    public void setDuplicatedCode(int duplicatedCode) {
        this.duplicatedCode = duplicatedCode;
    }

    public int getDataClass() {
        return this.dataClass;
    }

    public void setDataClass(int dataClass) {
        this.dataClass = dataClass;
    }

    public int getFileNumber() {
        return fileNumber;
    }
    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    @Override
	public String toString() {
		return "buildId=" + this.buildID + ", Build state=  "+this.build_state + ", CyclomaticComplexity = " + this.cyclomaticComplexity + "\n"+ 
        ", dataClass = "+this.dataClass + ", godClass = " +this.godClass+ ", duplicatedCode="+this.duplicatedCode+ ", godMethod = "+ this.godMethod+", FileNumber=" +this.fileNumber;
	}
}
