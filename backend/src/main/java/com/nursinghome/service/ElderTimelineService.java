package com.nursinghome.service;

import com.nursinghome.entity.*;
import com.nursinghome.mapper.CarePlanMapper;
import com.nursinghome.mapper.ElderMapper;
import com.nursinghome.mapper.FeeMapper;
import com.nursinghome.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 老人时间轴服务类
 */
@Service
public class ElderTimelineService {

    @Autowired
    private ElderMapper elderMapper;

    @Autowired
    private FeeMapper feeMapper;

    @Autowired
    private CarePlanMapper carePlanMapper;

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 获取老人的时间轴事件列表
     *
     * @param elderId 老人ID
     * @return 时间轴事件列表，按时间倒序排列
     */
    public List<ElderTimelineEvent> getTimelineEvents(Long elderId) {
        List<ElderTimelineEvent> events = new ArrayList<>();

        // 1. 获取老人基本信息
        Elder elder = elderMapper.selectById(elderId);
        if (elder == null) {
            return events;
        }

        // 2. 添加入住事件
        if (elder.getCheckInDate() != null) {
            Room room = null;
            if (elder.getRoomId() != null) {
                room = roomMapper.selectById(elder.getRoomId());
            }

            String description = "老人正式入住养老院";
            if (room != null) {
                description += "，分配房间：" + room.getRoomNo();
                if (elder.getBedNo() != null && !elder.getBedNo().isEmpty()) {
                    description += "，床位号：" + elder.getBedNo();
                }
            }

            events.add(ElderTimelineEvent.builder()
                    .elderId(elderId)
                    .eventType(ElderTimelineEvent.EventType.CHECK_IN)
                    .title("办理入住")
                    .description(description)
                    .eventTime(elder.getCheckInDate().atStartOfDay())
                    .build());
        }

        // 3. 获取费用记录（换房、缴费、退款）
        List<Fee> feeRecords = feeMapper.selectByElderId(elderId);
        for (Fee fee : feeRecords) {
            // 判断是否为换房费用（通过备注或金额特征判断）
            if (fee.getRemark() != null && fee.getRemark().contains("换房")) {
                String title = fee.getAmount().compareTo(BigDecimal.ZERO) > 0 ? "换房补差价" : "换房退差价";
                String description = fee.getRemark();
                if (fee.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    description += "，需补缴：" + fee.getAmount() + "元";
                } else {
                    description += "，应退：" + fee.getAmount().abs() + "元";
                }

                events.add(ElderTimelineEvent.builder()
                        .elderId(elderId)
                        .eventType(ElderTimelineEvent.EventType.ROOM_CHANGE)
                        .title(title)
                        .description(description)
                        .eventTime(fee.getCreateTime() != null ? fee.getCreateTime() : LocalDateTime.now())
                        .relatedId(fee.getId())
                        .relatedType("FEE")
                        .build());
            }

            // 缴费事件（只记录已缴清的）
            if (Fee.FeeStatus.PAID.equals(fee.getStatus()) && fee.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                String title = fee.getFeeType() != null ? fee.getFeeType().getLabel() + "缴费" : "费用缴纳";
                String description = "缴纳" + (fee.getFeeMonth() != null ? fee.getFeeMonth() + "月" : "") +
                        title + "，金额：" + fee.getPaidAmount() + "元";
                if (fee.getPayMethod() != null) {
                    description += "，支付方式：" + fee.getPayMethod().getLabel();
                }

                events.add(ElderTimelineEvent.builder()
                        .elderId(elderId)
                        .eventType(ElderTimelineEvent.EventType.FEE_PAYMENT)
                        .title(title)
                        .description(description)
                        .eventTime(fee.getPayTime() != null ? fee.getPayTime() :
                                (fee.getUpdateTime() != null ? fee.getUpdateTime() : fee.getCreateTime()))
                        .relatedId(fee.getId())
                        .relatedType("FEE")
                        .build());
            }

            // 退款事件
            if (fee.getAmount().compareTo(BigDecimal.ZERO) < 0 && fee.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
                String title = "费用退款";
                String description = "退还" + (fee.getFeeMonth() != null ? fee.getFeeMonth() + "月" : "") +
                        "费用，退款金额：" + fee.getPaidAmount() + "元";
                if (fee.getPayMethod() != null) {
                    description += "，退款方式：" + fee.getPayMethod().getLabel();
                }

                events.add(ElderTimelineEvent.builder()
                        .elderId(elderId)
                        .eventType(ElderTimelineEvent.EventType.FEE_REFUND)
                        .title(title)
                        .description(description)
                        .eventTime(fee.getPayTime() != null ? fee.getPayTime() :
                                (fee.getUpdateTime() != null ? fee.getUpdateTime() : fee.getCreateTime()))
                        .relatedId(fee.getId())
                        .relatedType("FEE")
                        .build());
            }
        }

        // 4. 获取护理计划记录
        List<CarePlan> carePlans = carePlanMapper.selectByElderId(elderId);
        for (CarePlan carePlan : carePlans) {
            String title = "护理计划" + (carePlan.getPlanName() != null ? " - " + carePlan.getPlanName() : "");
            StringBuilder description = new StringBuilder();
            description.append("护理等级：").append(carePlan.getCareLevel());
            if (carePlan.getNurseName() != null) {
                description.append("，责任护士：").append(carePlan.getNurseName());
            }
            if (carePlan.getStartDate() != null) {
                description.append("，开始日期：").append(carePlan.getStartDate());
            }
            if (carePlan.getEndDate() != null) {
                description.append("，结束日期：").append(carePlan.getEndDate());
            }
            if (carePlan.getStatus() != null) {
                String statusText = switch (carePlan.getStatus()) {
                    case "ACTIVE" -> "进行中";
                    case "PAUSED" -> "已暂停";
                    case "COMPLETED" -> "已完成";
                    case "CANCELLED" -> "已取消";
                    default -> carePlan.getStatus();
                };
                description.append("，状态：").append(statusText);
            }

            events.add(ElderTimelineEvent.builder()
                    .elderId(elderId)
                    .eventType(ElderTimelineEvent.EventType.CARE_PLAN)
                    .title(title)
                    .description(description.toString())
                    .eventTime(carePlan.getCreateTime() != null ? carePlan.getCreateTime() : LocalDateTime.now())
                    .relatedId(carePlan.getId())
                    .relatedType("CARE_PLAN")
                    .build());
        }

        // 5. 添加退住事件
        if (elder.getCheckOutDate() != null) {
            events.add(ElderTimelineEvent.builder()
                    .elderId(elderId)
                    .eventType(ElderTimelineEvent.EventType.CHECK_OUT)
                    .title("办理退住")
                    .description("老人办理退住手续，结束养老院生活")
                    .eventTime(elder.getCheckOutDate().atStartOfDay())
                    .build());
        }

        // 按时间倒序排列（最新的在前面）
        events.sort(Comparator.comparing(ElderTimelineEvent::getEventTime,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return events;
    }
}
