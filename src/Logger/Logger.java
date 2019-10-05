package Logger;

import Model.NetworkModel.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public final class Logger {
    private static final Logger instance = new Logger();

    private Logger() {}

    public static Logger getInstance(){
        return instance;
    }

    public void log(Request request, String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        String wholeString = "Request with name: " + request.getName() + "has been sent to: " + request.getDestination().name();
        lines.add(wholeString);
        Path file = Paths.get(fileName);
        Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

    public void log(Result result, Request request, String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        String wholeString = "Result has been received with status of: " + result.getResultStatus().name() + " for request name: "
                + request.getName() + "which was made by: " + request.getPatientID() ;
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        writer.println(wholeString);
        writer.close();
    }
}
