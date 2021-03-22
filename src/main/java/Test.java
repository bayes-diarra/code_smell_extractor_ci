import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    public static void main(String[] args) throws IOException {
    
    
        ProcessBuilder builder = new ProcessBuilder("bash","-c","eval echo ~$USER");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if(line!= null)
                System.out.println(line);
        }

        


    }
    
}
