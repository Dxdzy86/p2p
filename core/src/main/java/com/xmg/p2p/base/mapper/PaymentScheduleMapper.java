package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.PaymentSchedule;

public interface PaymentScheduleMapper {
    int insert(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);

}