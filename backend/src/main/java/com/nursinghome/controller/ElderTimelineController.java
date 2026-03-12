package com.nursinghome.controller;

import com.nursinghome.entity.ElderTimelineEvent;
import com.nursinghome.entity.Result;
import com.nursinghome.service.ElderTimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 老人时间轴Controller
 */
@RestController
@RequestMapping("/elder-timeline")
public class ElderTimelineController {

    @Autowired
    private ElderTimelineService elderTimelineService;

    /**
     * 获取老人的时间轴事件列表
     *
     * @param elderId 老人ID
     * @return 时间轴事件列表
     */
    @GetMapping("/{elderId}")
    public Result getTimelineEvents(@PathVariable Long elderId) {
        List<ElderTimelineEvent> events = elderTimelineService.getTimelineEvents(elderId);
        return Result.success(events);
    }
}
