package com.zenika.poc.controller;

import com.zenika.poc.model.DataPoint;
import com.zenika.poc.service.ScriptService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ScriptController {

    private final ScriptService scriptService;

    public ScriptController(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @RequestMapping("/executeScript")
    public String executeScript(@RequestBody(required = false) String jsCode) {
        try {
            final String startDate = "2023-01-01T00:00:00";
            final double tolerance = 2;
            final List<DataPoint> announced = new ArrayList<>();
            final List<DataPoint> produced = new ArrayList<>();

            for (int i = 0; i < 45000; i++) {
                final String currentDateTime = getDate(startDate, 1);
                announced.add(new DataPoint(currentDateTime, getRandomValue(tolerance)));
                produced.add(new DataPoint(currentDateTime, getRandomValue(tolerance)));
            }
            final String content = jsCode != null ? jsCode : readJSFileContent();
            final long startTime = System.currentTimeMillis();
            String result = scriptService.execute(content, produced, announced, tolerance);

            System.out.printf("Execution time: %d milliseconds%n", System.currentTimeMillis() - startTime);

            return result;
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String readJSFileContent() throws IOException {
        try (InputStream inputStream = ScriptController.class.getClassLoader().getResourceAsStream("static/compute.js")) {
            if (inputStream == null) {
                throw new IOException("File not found!");
            }

            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                StringBuilder contentBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    contentBuilder.append(scanner.nextLine()).append("\n");
                }
                return contentBuilder.toString();
            }
        }
    }

    private double getRandomValue(double tolerance) {
        final Random random = new Random();

        return -tolerance + 2 * tolerance * random.nextDouble();
    }

    private String getDate(String startDate, int gap) throws ParseException {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        calendar.setTime(isoDateFormat.parse(startDate));
        calendar.add(Calendar.MINUTE, gap);

        return isoDateFormat.format(calendar.getTime());
    }
}
