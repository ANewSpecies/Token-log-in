package getta.tokenlogin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.util.Session;

import java.io.*;
import java.util.ArrayList;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(ConfigUtils.getConfigDirectory().getPath() + "\\TokenLogin.json");

    public static SessionConfig loadSessions() {

        try {

            FILE.createNewFile();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;
            while((line = br.readLine()) != null){

                sb.append(line);
            }

            return GSON.fromJson(sb.toString(), SessionConfig.class);
        }catch (IOException e){e.printStackTrace();}
        return null;
    }

    public static void saveSession(Session session) {

        if (session != null) {

            try {

                SessionConfig sessions;
                FILE.createNewFile();

                if((sessions = loadSessions()) != null) {

                    BufferedWriter writer = new BufferedWriter(new FileWriter(FILE));

                    sessions.sessions.add(session);
                    GSON.toJson(sessions, writer);

                    writer.flush();
                } else {

                    FileWriter writer = new FileWriter(FILE);

                    ArrayList<Session> ses = new ArrayList<>();
                    ses.add(session);
                    GSON.toJson(new SessionConfig(ses), writer);

                    writer.flush();
                }

            } catch (IOException ignored) {
            }
        }
    }

    public static class SessionConfig {

        private ArrayList<Session> sessions;

        public SessionConfig(ArrayList<Session> sessions) {
            this.sessions = sessions;
        }

        public ArrayList<Session> getSessions() {
            return sessions;
        }

        public void setSessions(ArrayList<Session> sessions) {
            this.sessions = sessions;
        }
    }
}
