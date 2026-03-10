package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.CarePlan;
import com.nursinghome.entity.Elder;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.CarePlanMapper;
import com.nursinghome.mapper.ElderMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 护理计划Controller
 */
@RestController
@RequestMapping("/care-plan")
public class CarePlanController {

    @Autowired
    private CarePlanMapper carePlanMapper;
    
    @Autowired
    private ElderMapper elderMapper;

    /**
     * 获取护理计划列表（分页）
     */
    @GetMapping("/list")
    public Result getCarePlanList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String planName,
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String careLevel,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long elderId) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<CarePlan> list;
        
        // 如果有任何查询条件，使用条件查询
        if ((planName != null && !planName.isEmpty()) || 
            (elderName != null && !elderName.isEmpty()) || 
            (careLevel != null && !careLevel.isEmpty()) || 
            (status != null && !status.isEmpty()) || 
            elderId != null) {
            list = carePlanMapper.selectByCondition(planName, elderName, careLevel, status, elderId);
        } else {
            list = carePlanMapper.selectAll();
        }
        
        PageInfo<CarePlan> pageInfo = new PageInfo<>(list);
        
        PageResult<CarePlan> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有护理计划
     */
    @GetMapping("/all")
    public Result getAllCarePlans() {
        List<CarePlan> list = carePlanMapper.selectAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取护理计划
     */
    @GetMapping("/{id}")
    public Result getCarePlanById(@PathVariable Long id) {
        CarePlan carePlan = carePlanMapper.selectById(id);
        if (carePlan == null) {
            return Result.error("护理计划不存在");
        }
        return Result.success(carePlan);
    }

    /**
     * 根据老人ID获取护理计划
     */
    @GetMapping("/elder/{elderId}")
    public Result getCarePlansByElderId(@PathVariable Long elderId) {
        List<CarePlan> list = carePlanMapper.selectByElderId(elderId);
        return Result.success(list);
    }

    /**
     * 创建护理计划
     */
    @PostMapping
    @Transactional
    public Result createCarePlan(@RequestBody CarePlan carePlan, HttpServletRequest request) {
        // 验证老人是否存在
        if (carePlan.getElderId() == null) {
            return Result.error("请选择护理对象");
        }
        
        Elder elder = elderMapper.selectById(carePlan.getElderId());
        if (elder == null) {
            return Result.error("老人不存在");
        }
        
        // 验证老人状态，只有已入住的老人才能创建护理计划
        if (!Elder.STATUS_CHECKED_IN.equals(elder.getStatus())) {
            if (Elder.STATUS_PENDING.equals(elder.getStatus())) {
                return Result.error("该老人尚未入住，不能创建护理计划");
            }
            if (Elder.STATUS_CHECKED_OUT.equals(elder.getStatus())) {
                return Result.error("该老人已退住，不能创建护理计划");
            }
            if (Elder.STATUS_SUSPENDED.equals(elder.getStatus())) {
                return Result.error("该老人已暂停，不能创建护理计划");
            }
        }
        
        // 生成计划编号
        carePlan.setPlanNo(CodeGenerator.generatePlanNo());
        
        // 设置默认状态
        if (carePlan.getStatus() == null) {
            carePlan.setStatus(CarePlan.STATUS_ACTIVE);
        }
        
        // 设置创建人
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            carePlan.setCreateBy(userId);
        }
        
        carePlanMapper.insert(carePlan);
        return Result.success("创建成功", carePlan);
    }

    /**
     * 更新护理计划
     */
    @PutMapping
    public Result updateCarePlan(@RequestBody CarePlan carePlan) {
        CarePlan existPlan = carePlanMapper.selectById(carePlan.getId());
        if (existPlan == null) {
            return Result.error("护理计划不存在");
        }
        
        // 已完成的护理计划不能修改
        if (CarePlan.STATUS_COMPLETED.equals(existPlan.getStatus())) {
            return Result.error("已完成的护理计划不能修改");
        }
        
        // 已取消的护理计划不能修改
        if (CarePlan.STATUS_CANCELLED.equals(existPlan.getStatus())) {
            return Result.error("已取消的护理计划不能修改");
        }
        
        carePlanMapper.update(carePlan);
        return Result.success("更新成功", null);
    }

    /**
     * 删除护理计划
     */
    @DeleteMapping("/{id}")
    @Transactional
    public Result deleteCarePlan(@PathVariable Long id) {
        CarePlan existPlan = carePlanMapper.selectById(id);
        if (existPlan == null) {
            return Result.error("护理计划不存在");
        }
        
        // 进行中的护理计划不能删除
        if (CarePlan.STATUS_ACTIVE.equals(existPlan.getStatus())) {
            return Result.error("进行中的护理计划不能删除，请先暂停或完成");
        }
        
        carePlanMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 暂停护理计划
     */
    @PutMapping("/{id}/pause")
    @Transactional
    public Result pauseCarePlan(@PathVariable Long id) {
        CarePlan existPlan = carePlanMapper.selectById(id);
        if (existPlan == null) {
            return Result.error("护理计划不存在");
        }
        
        // 状态校验：只有进行中的计划才能暂停
        if (!CarePlan.STATUS_ACTIVE.equals(existPlan.getStatus())) {
            if (CarePlan.STATUS_PAUSED.equals(existPlan.getStatus())) {
                return Result.error("该护理计划已处于暂停状态");
            }
            if (CarePlan.STATUS_COMPLETED.equals(existPlan.getStatus())) {
                return Result.error("已完成的护理计划不能暂停");
            }
            if (CarePlan.STATUS_CANCELLED.equals(existPlan.getStatus())) {
                return Result.error("已取消的护理计划不能暂停");
            }
        }
        
        CarePlan updatePlan = new CarePlan();
        updatePlan.setId(id);
        updatePlan.setStatus(CarePlan.STATUS_PAUSED);
        
        carePlanMapper.update(updatePlan);
        return Result.success("已暂停", null);
    }

    /**
     * 恢复护理计划
     */
    @PutMapping("/{id}/resume")
    @Transactional
    public Result resumeCarePlan(@PathVariable Long id) {
        CarePlan existPlan = carePlanMapper.selectById(id);
        if (existPlan == null) {
            return Result.error("护理计划不存在");
        }
        
        // 状态校验：只有暂停的计划才能恢复
        if (!CarePlan.STATUS_PAUSED.equals(existPlan.getStatus())) {
            if (CarePlan.STATUS_ACTIVE.equals(existPlan.getStatus())) {
                return Result.error("该护理计划已处于进行中状态");
            }
            if (CarePlan.STATUS_COMPLETED.equals(existPlan.getStatus())) {
                return Result.error("已完成的护理计划不能恢复");
            }
            if (CarePlan.STATUS_CANCELLED.equals(existPlan.getStatus())) {
                return Result.error("已取消的护理计划不能恢复");
            }
        }
        
        CarePlan updatePlan = new CarePlan();
        updatePlan.setId(id);
        updatePlan.setStatus(CarePlan.STATUS_ACTIVE);
        
        carePlanMapper.update(updatePlan);
        return Result.success("已恢复", null);
    }

    /**
     * 完成护理计划
     */
    @PutMapping("/{id}/complete")
    @Transactional
    public Result completeCarePlan(@PathVariable Long id) {
        CarePlan existPlan = carePlanMapper.selectById(id);
        if (existPlan == null) {
            return Result.error("护理计划不存在");
        }
        
        // 状态校验：只有进行中或暂停的计划才能完成
        if (CarePlan.STATUS_COMPLETED.equals(existPlan.getStatus())) {
            return Result.error("该护理计划已完成");
        }
        if (CarePlan.STATUS_CANCELLED.equals(existPlan.getStatus())) {
            return Result.error("已取消的护理计划不能完成");
        }
        
        CarePlan updatePlan = new CarePlan();
        updatePlan.setId(id);
        updatePlan.setStatus(CarePlan.STATUS_COMPLETED);
        
        carePlanMapper.update(updatePlan);
        return Result.success("已完成", null);
    }
}
