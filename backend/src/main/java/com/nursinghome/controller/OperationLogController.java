package com.nursinghome.controller;

import com.nursinghome.entity.Result;
import com.nursinghome.entity.OperationLog;
import com.nursinghome.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String operationModule,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        username = (username != null && !username.isEmpty()) ? username : null;
        operationType = (operationType != null && !operationType.isEmpty()) ? operationType : null;
        operationModule = (operationModule != null && !operationModule.isEmpty()) ? operationModule : null;
        status = (status != null && !status.isEmpty()) ? status : null;
        startTime = (startTime != null && !startTime.isEmpty()) ? startTime : null;
        endTime = (endTime != null && !endTime.isEmpty()) ? endTime : null;

        List<OperationLog> list = operationLogMapper.selectList(username, operationType, operationModule, status, startTime, endTime);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<OperationLog> getById(@PathVariable Long id) {
        OperationLog log = operationLogMapper.selectById(id);
        if (log == null) {
            return Result.error("Record not found");
        }
        return Result.success(log);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        int result = operationLogMapper.deleteById(id);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("Delete failed");
    }

    @DeleteMapping("/clean/{days}")
    public Result<Void> clean(@PathVariable int days) {
        int result = operationLogMapper.deleteByTime(days);
        return Result.success("Cleaned " + result + " logs", null);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("todayCount", operationLogMapper.countToday());
        stats.put("todayFailCount", operationLogMapper.countTodayFail());
        return Result.success(stats);
    }

    @GetMapping("/fail-statistics")
    public Result<List<Map<String, Object>>> failStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<Map<String, Object>> list;
        if (startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
            list = operationLogMapper.countFailByTypeWithTime(startTime, endTime);
        } else {
            list = operationLogMapper.countFailByType();
        }
        return Result.success(list);
    }
}
