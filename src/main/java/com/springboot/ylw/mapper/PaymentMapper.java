package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

@Mapper
public interface PaymentMapper {
    Payment selectById(Integer id);
    Payment selectByOrderId(Integer orderId);
    Payment selectByOrderNo(String orderNo);
    int insert(Payment payment);
    int markPaid(@Param("id") Integer id, @Param("tradeNo") String tradeNo,
                 @Param("payTime") Date payTime, @Param("rawResponse") String rawResponse);
    int markClosed(@Param("id") Integer id);
    int markRefunded(@Param("id") Integer id, @Param("rawResponse") String rawResponse);
}
