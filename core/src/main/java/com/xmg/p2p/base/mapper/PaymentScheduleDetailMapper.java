package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.PaymentScheduleDetail;

public interface PaymentScheduleDetailMapper {
    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);
}