package parsers;

import com.google.gson.Gson;
import entities.criterias.Criterias;
import entities.criterias.DateForStat;
import serializers.Error;

import java.io.*;

public class DateParser {

    public static DateForStat parse(String path) throws IOException {
        Gson gson = new Gson();
        DateForStat date = null;
        try (Reader reader = new FileReader(path)) {
            date = gson.fromJson(reader, DateForStat.class);
        } catch (IOException e) {
            String jsonString = new Gson().toJson(new Error("Json error"), Error.class);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonString);

            writer.close();
        }
        return date;
    }
}
