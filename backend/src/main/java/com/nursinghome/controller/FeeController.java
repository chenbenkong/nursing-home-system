package com.nursinghome.controller;

import com.nursinghome.entity.Fee;
import com.nursinghome.entity.Result;
import com.nursinghome.service.FeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用管理控制器
 */
@RestController
@RequestMapping("/fee")
public class FeeController {

    @Autowired
    private FeeService feeService;

    /**
     * 获取费用记录列表（分页）
     */
    @GetMapping("/list")
    public Result list(@RequestParam(required = false) Long elderId,
                       @RequestParam(required = false) String feeType,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String feeMonth,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        return feeService.getFeeList(elderId, feeType, status, feeMonth, pageNum, pageSize);
    }

    /**
     * 根据ID获取费用记录
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return feeService.getFeeById(id);
    }

    /**
     * 根据老人ID获取费用记录
     */
    @GetMapping("/elder/{elderId}")
    public Result getByElderId(@PathVariable Long elderId) {
        return feeService.getFeesByElderId(elderId);
    }

    /**
     * 新增费用记录
     */
    @PostMapping
    public Result add(@RequestBody Fee fee) {
        return feeService.addFee(fee);
    }

    /**
     * 更新费用记录
     */
    @PutMapping
    public Result update(@RequestBody Fee fee) {
        return feeService.updateFee(fee);
    }

    /**
     * 删除费用记录
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return feeService.deleteFee(id);
    }

    /**
     * 缴费
     */
    @PostMapping("/{id}/pay")
    public Result pay(@PathVariable Long id,
                      @RequestParam BigDecimal payAmount,
                      @RequestParam String payMethod) {
        return feeService.payFee(id, payAmount, payMethod);
    }

    /**
     * 获取费用统计
     */
    @GetMapping("/statistics")
    public Result statistics(@RequestParam(required = false) String feeMonth) {
        return feeService.getFeeStatistics(feeMonth);
    }

    /**
     * 获取本月费用统计
     */
    @GetMapping("/statistics/current-month")
    public Result currentMonthStatistics() {
        return feeService.getCurrentMonthStatistics();
    }
}
