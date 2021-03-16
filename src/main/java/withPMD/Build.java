package withPMD;
import java.util.Arrays;
import java.util.List;



public class Build{
	private String project_name;
	private String buildId;
	private List<String> commits;
	private int build_State;
	
	public Build( String project_name, String buildId, int build_State,List<String> commits) {
		
		this.project_name =project_name;
		this.buildId = buildId;
		this.commits = commits;
		this.build_State = build_State;
	}
	
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	public List<String> getCommits() {
		return commits;
	}
	public void setCommits(List<String> commits) {
		this.commits = commits;
	}
	public int getBuild_State() {
		return build_State;
	}
	public void setBuild_State(int build_State) {
		this.build_State = build_State;
	}
	public int getNbrCommits() {
		return commits.size();
	}

	@Override
	public String toString() {
		return "Build [buildId=" + buildId + ", commits=" + Arrays.toString(commits.toArray()) + ", build_status=" + build_State + "]";
	}
	

	
	
}
