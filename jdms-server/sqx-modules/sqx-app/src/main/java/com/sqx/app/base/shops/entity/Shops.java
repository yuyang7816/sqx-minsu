package com.sqx.app.base.shops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 酒店
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@Getter
@Setter
@Schema(description = "酒店信息")
public class Shops implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "酒店id")
    @TableId(value = "shop_id", type = IdType.AUTO)
    private Long shopId;

    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "酒店名称")
    private String shopName;

    /**
     * 封面图
     */
    @Schema(description = "封面图")
    private String titleImg;

    @Schema(description = "酒店类型 1酒店 2民宿")
    private Integer shopType;

    @Schema(description = "轮播图")
    private String carouselUrl;

    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "省")
    private String province;

    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "市")
    private String city;

    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "区")
    private String county;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "经度")
    private Double lng;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "状态 0下架 1上架")
    private Integer isEnable;

    @Schema(description = "评分")
    private BigDecimal score;

    @Schema(description = "分成比例")
    private BigDecimal rate;

    @Schema(description = "总收益")
    private BigDecimal allMoney;

    @Schema(description = "可提现金额")
    private BigDecimal cashMoney;

    @Schema(description = "已提现金额")
    private BigDecimal cashDownMoney;

    @Schema(description = "是否是推荐0否 1是")
    private Integer isRecommend;

    @Schema(description = "标签")
    private String tags;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 酒店审核状态：1已通过 2已拒绝 3待审核
     */
    @Schema(description = "酒店审核状态：1已通过 2已拒绝 3待审核")
    private Integer auditStatus;

    /**
     * 审核备注
     */
    @Schema(description = "审核备注")
    private String auditRemark;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名称")
    @TableField(exist = false)
    private String userName;

    /**
     * 酒店联系电话
     */
    @Schema(description = "酒店联系电话")
    private String shopPhone;


    @TableField(exist = false)
    @Schema(description = "距离")
    private Double distance;

    /**
     * 房间最低价格
     */
    @TableField(exist = false)
    @Schema(description = "房间最低价格")
    private BigDecimal minMoney;

    /**
     * 配置列表名称拼接
     */
    @TableField(exist = false)
    @Schema(description = "配置列表名称拼接")
    private String classifyNames;

    /**
     * 是否收藏 0否 1是
     */
    @TableField(exist = false)
    @Schema(description = "是否收藏 0否 1是")
    private Integer isCollect;

    /**
     * 排序类型：1默认排序 2位置优先 3热门优先 4评分从高到低 5评分从低到高
     */
    @TableField(exist = false)
    @Schema(description = "排序类型：1默认排序 2位置优先 3热门优先 4评分从高到低 5评分从低到高")
    private Integer sortType;

    /**
     * 最低价格筛选
     */
    @TableField(exist = false)
    @Schema(description = "最低价格筛选")
    private BigDecimal minPrice;

    /**
     * 最高价格筛选
     */
    @TableField(exist = false)
    @Schema(description = "最高价格筛选")
    private BigDecimal maxPrice;

    /**
     * 是否仅展示含钟点房的商家 1是
     */
    @TableField(exist = false)
    @Schema(description = "是否仅展示含钟点房的商家 1是")
    private Integer hasHourlyRoom;
}
