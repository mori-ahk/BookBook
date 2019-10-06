package Logger;

import Model.NetworkModel.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class Logger {
    private static final Logger instance = new Logger();

    private Logger() {}

    public static Logger getInstance(){
        return instance;
    }

    public void log(Result result, Request request, String fileName) throws IOException {
        String wholeString = "Result has been received with status of: " + result.getResultStatus().name() + " for request name: "
                + request.getName() + " which was made for patient with ID: " + request.getPatientID() +"\n" ;
        File file = new File(fileName);
        if(!file.exists()) file.createNewFile();
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        writer.println(wholeString);
        writer.close();
    }
}
