package com.antigravity.loganalyzer.controller;

import com.antigravity.loganalyzer.entity.LogEntry;
import com.antigravity.loganalyzer.service.LogService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<LogEntry>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @PostMapping
    public ResponseEntity<LogEntry> save(@RequestBody LogEntry logEntry) {
        return ResponseEntity.ok(logService.saveLog(
                logEntry.getMessage(),
                logEntry.getLevel(),
                logEntry.getSystemName()
        ));
    }

    @PostMapping("/parse")
    public ResponseEntity<LogEntry> parse(@RequestBody RawMessageRequest request) {
        return ResponseEntity.ok(logService.parseAndSave(request.rawMessage()));
    }

    public record RawMessageRequest(String rawMessage) {
    }
}
