package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Refund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface RefundMapper {
    Refund selectById(Integer id);
    Refund selectByOrderId(Integer orderId);
    List<Refund> selectByBuyer(Integer buyerId);
    List<Refund> selectBySeller(Integer sellerId);
    /** 管理员：获取所有仲裁中(3) 的申请 */
    List<Refund> selectArbitrating();
    /** 管理员：获取全部申请 */
    List<Refund> selectAll();

    int insert(Refund refund);

    int sellerAgree(@Param("id") Integer id, @Param("respondTime") Date respondTime);
    int sellerReject(@Param("id") Integer id, @Param("reply") String reply, @Param("respondTime") Date respondTime);
    int applyArbitrate(@Param("id") Integer id, @Param("arbitrateTime") Date arbitrateTime);
    int adminAgree(@Param("id") Integer id, @Param("note") String note, @Param("respondTime") Date respondTime);
    int adminReject(@Param("id") Integer id, @Param("note") String note, @Param("respondTime") Date respondTime);
    int markRefunded(@Param("id") Integer id, @Param("refundTime") Date refundTime);
}
