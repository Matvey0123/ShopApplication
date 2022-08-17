import com.google.gson.Gson;
import serializers.Error;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
        if (args[0].equals("search")) {
            OperationManager.executeSearch(args[1], args[2]);
        } else if (args[0].equals("stat")) {
            OperationManager.executeStat(args[1], args[2]);
        } else {
            sendError("Unknown operation error", args[2]);
        }
        } catch (SQLException sqlException) {
            sendError("Database error", args[2]);
        } catch (IOException ioException) {
            sendError("File error", args[2]);
        }
    }

    private static void sendError(String msg, String out) throws IOException {
        String jsonString = new Gson().toJson(new Error(msg), Error.class);
        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
        writer.write(jsonString);

        writer.close();
    }
}
