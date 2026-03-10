package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.Activity;
import com.nursinghome.entity.ActivityParticipant;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.ActivityMapper;
import com.nursinghome.mapper.ActivityParticipantMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityMapper activityMapper;
    
    @Autowired
    private ActivityParticipantMapper activityParticipantMapper;

    @GetMapping("/list")
    public Result getActivityList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String activityType) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list;
        
        if (status != null && !status.isEmpty()) {
            list = activityMapper.selectByStatus(status);
        } else if (activityType != null && !activityType.isEmpty()) {
            list = activityMapper.selectByActivityType(activityType);
        } else {
            list = activityMapper.selectAll();
        }
        
        PageInfo<Activity> pageInfo = new PageInfo<>(list);
        PageResult<Activity> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/all")
    public Result getAllActivities() {
        return Result.success(activityMapper.selectAll());
    }

    @GetMapping("/{id}")
    public Result getActivityById(@PathVariable Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) return Result.error("活动不存在");
        return Result.success(activity);
    }

    @PostMapping
    public Result createActivity(@RequestBody Activity activity) {
        activity.setActivityNo(CodeGenerator.generateActivityNo());
        if (activity.getStatus() == null) activity.setStatus(Activity.STATUS_PENDING);
        if (activity.getCurrentParticipants() == null) activity.setCurrentParticipants(0);
        activityMapper.insert(activity);
        return Result.success("创建成功", activity);
    }

    @PutMapping
    public Result updateActivity(@RequestBody Activity activity) {
        if (activityMapper.selectById(activity.getId()) == null) return Result.error("活动不存在");
        activityMapper.update(activity);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteActivity(@PathVariable Long id) {
        if (activityMapper.selectById(id) == null) return Result.error("活动不存在");
        activityParticipantMapper.deleteByActivityId(id);
        activityMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/start")
    public Result startActivity(@PathVariable Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) return Result.error("活动不存在");
        if (!Activity.STATUS_PENDING.equals(activity.getStatus())) return Result.error("该活动状态不允许开始");
        
        Activity update = new Activity();
        update.setId(id);
        update.setStatus(Activity.STATUS_ONGOING);
        activityMapper.update(update);
        return Result.success("活动已开始", null);
    }

    @PutMapping("/{id}/complete")
    public Result completeActivity(@PathVariable Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) return Result.error("活动不存在");
        if (!Activity.STATUS_ONGOING.equals(activity.getStatus())) return Result.error("该活动状态不允许完成");
        
        Activity update = new Activity();
        update.setId(id);
        update.setStatus(Activity.STATUS_COMPLETED);
        activityMapper.update(update);
        return Result.success("活动已完成", null);
    }

    @PostMapping("/{activityId}/register")
    public Result registerActivity(@PathVariable Long activityId, @RequestParam Long elderId, @RequestParam String elderName) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) return Result.error("活动不存在");
        if (!Activity.STATUS_PENDING.equals(activity.getStatus())) return Result.error("该活动不在报名阶段");
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) return Result.error("活动人数已满");
        
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setElderId(elderId);
        participant.setElderName(elderName);
        participant.setStatus(ActivityParticipant.STATUS_SIGNED_UP);
        
        activityParticipantMapper.insert(participant);
        activityMapper.incrementCurrentParticipants(activityId);
        return Result.success("报名成功", participant);
    }

    @PutMapping("/{activityId}/check-in")
    public Result checkIn(@PathVariable Long activityId, @RequestParam Long elderId) {
        ActivityParticipant participant = activityParticipantMapper.selectByActivityIdAndElderId(activityId, elderId);
        if (participant == null) return Result.error("该老人未报名此活动");
        
        ActivityParticipant update = new ActivityParticipant();
        update.setId(participant.getId());
        update.setStatus(ActivityParticipant.STATUS_ATTENDED);
        update.setSignInTime(LocalDateTime.now());
        activityParticipantMapper.update(update);
        return Result.success("签到成功", null);
    }

    @GetMapping("/{activityId}/participants")
    public Result getParticipants(@PathVariable Long activityId) {
        return Result.success(activityParticipantMapper.selectByActivityId(activityId));
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", activityMapper.countAll());
        stats.put("pending", activityMapper.countByStatus(Activity.STATUS_PENDING));
        stats.put("ongoing", activityMapper.countByStatus(Activity.STATUS_ONGOING));
        stats.put("completed", activityMapper.countByStatus(Activity.STATUS_COMPLETED));
        return Result.success(stats);
    }
}
