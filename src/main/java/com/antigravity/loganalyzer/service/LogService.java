package com.antigravity.loganalyzer.service;

import com.antigravity.loganalyzer.entity.LogEntry;
import com.antigravity.loganalyzer.repository.LogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<LogEntry> getAllLogs() {
        return logRepository.findAll();
    }

    public LogEntry saveLog(String rawMessage, String level, String system) {
        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLevel(level);
        logEntry.setMessage(rawMessage);
        logEntry.setSystemName(system);
        logEntry.setErrorCode(null);

        return logRepository.save(logEntry);
    }

    public LogEntry parseAndSave(String rawMessage) {
        Pattern pattern = Pattern.compile("^\\[([A-Z]+)]\\s*(.+)$");
        Matcher matcher = pattern.matcher(rawMessage.trim());

        String level;
        String message;

        if (matcher.matches()) {
            level = matcher.group(1);
            message = matcher.group(2).trim();
        } else {
            level = "UNKNOWN";
            message = rawMessage.trim();
        }

        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLevel(level);
        logEntry.setMessage(message);
        logEntry.setSystemName("PARSER");
        logEntry.setErrorCode(null);

        return logRepository.save(logEntry);
    }
}
