import java.io.IOException;
import java.util.Properties;
import jira.Client;

public class Application {

    public static void main(String[] args)
    {
        Boolean nextArgIsConfig = false;
        String configPath = "./jira.properties";
        for (String arg: args) {
            if (nextArgIsConfig) {
                configPath = arg;
                nextArgIsConfig = false;
                continue;
            }
            if (arg.equals("-c")) {
                nextArgIsConfig = true;
            }
        }
        if (nextArgIsConfig) {
            System.out.println("No config path specified after `-c`");
            return;
        }

        Properties configFile = new Properties();
        try {
            configFile.load(Application.class.getClassLoader().getResourceAsStream(configPath));
        }
        catch (IOException e) {
            System.out.println("Can not read properties: " + e.getMessage());
        }

        Client jiraClient = new Client(
            configFile.getProperty("host"),
            configFile.getProperty("username"),
            configFile.getProperty("password")
        );
        jiraClient.callMethod("search", "jql=project%3DiWeb%20and%20resolutiondate%20>%200");
    }
}
