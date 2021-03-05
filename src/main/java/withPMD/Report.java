package withPMD;

public class Report {

    private String buildID;
    private int build_Failed;
    private int duplicatedCode,GodClass,GodMethod,CyclomaticComplexity,DataClass;
    private  static int NUMBER=1;

    public Report(String buildID, int build_Failed) {
        this.buildID = buildID;
        this.build_Failed = build_Failed;
        this.duplicatedCode = 0;
        this.GodClass = 0;
        this.GodMethod = 0;
        this.CyclomaticComplexity = 0;
        this.DataClass = 0;
        Report.NUMBER++;
    }

    public static int getNumber(){
        return NUMBER;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public int getBuild_Failed() {
        return build_Failed;
    }

    public void setBuild_Failed(int build_Failed) {
        this.build_Failed = build_Failed;
    }

    public int getGodClass() {
        return GodClass;
    }

    public void setGodClass(int godClass) {
        GodClass = godClass;
    }

    public int getGodMethod() {
        return GodMethod;
    }

    public void setGodMethod(int godMethod) {
        GodMethod = godMethod;
    }

    public int getCyclomaticComplexity() {
        return CyclomaticComplexity;
    }

    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        CyclomaticComplexity = cyclomaticComplexity;
    }

    public int getDuplicatedCode() {
        return duplicatedCode;
    }

    public void setDuplicatedCode(int duplicatedCode) {
        this.duplicatedCode = duplicatedCode;
    }

    public int getDataClass() {
        return DataClass;
    }

    public void setDataClass(int dataClass) {
        DataClass = dataClass;
    }
}
