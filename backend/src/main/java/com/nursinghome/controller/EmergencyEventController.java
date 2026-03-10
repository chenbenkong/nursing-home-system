package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.EmergencyEvent;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.EmergencyEventMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emergency")
public class EmergencyEventController {

    @Autowired
    private EmergencyEventMapper emergencyEventMapper;

    @GetMapping("/list")
    public Result getEmergencyEventList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String severity) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<EmergencyEvent> list;
        
        if (status != null && !status.isEmpty()) {
            list = emergencyEventMapper.selectByStatus(status);
        } else if (eventType != null && !eventType.isEmpty()) {
            list = emergencyEventMapper.selectByEventType(eventType);
        } else if (severity != null && !severity.isEmpty()) {
            list = emergencyEventMapper.selectBySeverity(severity);
        } else {
            list = emergencyEventMapper.selectAll();
        }
        
        PageInfo<EmergencyEvent> pageInfo = new PageInfo<>(list);
        PageResult<EmergencyEvent> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/all")
    public Result getAllEmergencyEvents() {
        return Result.success(emergencyEventMapper.selectAll());
    }

    @GetMapping("/{id}")
    public Result getEmergencyEventById(@PathVariable Long id) {
        EmergencyEvent event = emergencyEventMapper.selectById(id);
        if (event == null) return Result.error("紧急事件不存在");
        return Result.success(event);
    }

    @PostMapping
    public Result addEmergencyEvent(@RequestBody EmergencyEvent emergencyEvent) {
        emergencyEvent.setEventNo(CodeGenerator.generateEmergencyEventNo());
        if (emergencyEvent.getStatus() == null) emergencyEvent.setStatus(EmergencyEvent.STATUS_PENDING);
        if (emergencyEvent.getEventTime() == null) emergencyEvent.setEventTime(LocalDateTime.now());
        emergencyEventMapper.insert(emergencyEvent);
        return Result.success("上报成功", emergencyEvent);
    }

    @PutMapping
    public Result updateEmergencyEvent(@RequestBody EmergencyEvent emergencyEvent) {
        if (emergencyEventMapper.selectById(emergencyEvent.getId()) == null) {
            return Result.error("紧急事件不存在");
        }
        emergencyEventMapper.update(emergencyEvent);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteEmergencyEvent(@PathVariable Long id) {
        if (emergencyEventMapper.selectById(id) == null) return Result.error("紧急事件不存在");
        emergencyEventMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/process")
    public Result startProcess(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        EmergencyEvent event = emergencyEventMapper.selectById(id);
        if (event == null) return Result.error("紧急事件不存在");
        if (!EmergencyEvent.STATUS_PENDING.equals(event.getStatus())) {
            return Result.error("该事件状态不允许开始处理");
        }
        
        EmergencyEvent update = new EmergencyEvent();
        update.setId(id);
        update.setStatus(EmergencyEvent.STATUS_HANDLING);
        if (params.get("handlerId") != null) update.setHandlerId(Long.valueOf(params.get("handlerId").toString()));
        if (params.get("handlerName") != null) update.setHandlerName(params.get("handlerName").toString());
        
        emergencyEventMapper.update(update);
        return Result.success("开始处理", null);
    }

    @PutMapping("/{id}/resolve")
    public Result resolveEmergencyEvent(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        EmergencyEvent event = emergencyEventMapper.selectById(id);
        if (event == null) return Result.error("紧急事件不存在");
        if (EmergencyEvent.STATUS_RESOLVED.equals(event.getStatus())) return Result.error("该事件已解决");
        
        EmergencyEvent update = new EmergencyEvent();
        update.setId(id);
        update.setStatus(EmergencyEvent.STATUS_RESOLVED);
        update.setHandleTime(LocalDateTime.now());
        if (params.get("handlerId") != null) update.setHandlerId(Long.valueOf(params.get("handlerId").toString()));
        if (params.get("handlerName") != null) update.setHandlerName(params.get("handlerName").toString());
        if (params.get("result") != null) update.setResult(params.get("result").toString());
        
        emergencyEventMapper.update(update);
        return Result.success("事件已解决", null);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", emergencyEventMapper.countAll());
        stats.put("pendingCount", emergencyEventMapper.countByStatus(EmergencyEvent.STATUS_PENDING));
        stats.put("processingCount", emergencyEventMapper.countByStatus(EmergencyEvent.STATUS_PROCESSING));
        stats.put("resolvedCount", emergencyEventMapper.countByStatus(EmergencyEvent.STATUS_RESOLVED));
        return Result.success(stats);
    }
}
