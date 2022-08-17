package parsers;

import com.google.gson.Gson;
import entities.criterias.Criterias;
import serializers.Error;

import java.io.*;

public class CriteriaParser {

    public static Criterias parse(String path) throws IOException {
        Gson gson = new Gson();
        Criterias criterias = null;
        try (Reader reader = new FileReader(path)) {
            criterias = gson.fromJson(reader, Criterias.class);
        } catch (IOException e) {
            String jsonString = new Gson().toJson(new Error("Json error"), Error.class);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonString);

            writer.close();
        }
        return criterias;
    }

}
