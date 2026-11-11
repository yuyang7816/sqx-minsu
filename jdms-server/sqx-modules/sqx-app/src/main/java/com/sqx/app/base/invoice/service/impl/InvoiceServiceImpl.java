package com.sqx.app.base.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.invoice.domain.bo.InvoiceInfoBo;
import com.sqx.app.base.invoice.domain.bo.InvoicePageBo;
import com.sqx.app.base.invoice.domain.bo.InvoiceSaveBo;
import com.sqx.app.base.invoice.domain.bo.InvoiceUpdateBo;
import com.sqx.app.base.invoice.domain.entity.Invoice;
import com.sqx.app.base.invoice.domain.vo.InvoiceVo;
import com.sqx.app.base.invoice.mapper.InvoiceDao;
import com.sqx.app.base.invoice.service.InvoiceService;
import com.sqx.app.base.orders.dao.OrdersDao;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.core.exception.SqxException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 发票 服务实现类
 *
 * @author sqx
 * @since 2025-02-21 14:13:26
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceDao, Invoice> implements InvoiceService {

    @Resource
    private OrdersDao ordersDao;

    @Override
    public Result<Void> saveInfo(InvoiceSaveBo invoiceSaveBo) {
        if (Objects.nonNull(invoiceSaveBo.getOrderIds())) {
            String[] split = invoiceSaveBo.getOrderIds().trim().split(",");
            for (String orderId : split) {
                ordersDao.update(new Orders(), new LambdaUpdateWrapper<Orders>()
                        .eq(Orders::getOrderId, orderId)
                        .set(Orders::getIsInvoice, 2));
                long count = baseMapper.selectCount(new LambdaQueryWrapper<Invoice>()
                        .eq(Invoice::getOrderId, orderId));
                if (count > 0) {
                    log.error("订单" + orderId + "您已经申请开具发票，请勿重复提交！");
                    continue;
                }
                Invoice invoice = new Invoice();
                BeanUtils.copyProperties(invoiceSaveBo, invoice);
                invoice.setStatus(1);
                invoice.setOrderId(Long.valueOf(orderId));
                this.save(invoice);
            }
        }
        return Result.success();
    }

    @Override
    public Result<IPage<InvoiceVo>> pageInfo(InvoicePageBo invoicePageBo) {
//        Page<Invoice> initPage = PageUtil.initPage(invoicePageBo.getPage(), invoicePageBo.getLimit());
//
//        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda()
//                .eq(Objects.nonNull(invoicePageBo.getUserId()), Invoice::getUserId, invoicePageBo.getUserId())
//                .like(StringUtils.isNotBlank(invoicePageBo.getTitle()), Invoice::getTitle, invoicePageBo.getTitle())
//                .like(StringUtils.isNotBlank(invoicePageBo.getUserName()), Invoice::getUserName, invoicePageBo.getUserName())
//                .eq(Objects.nonNull(invoicePageBo.getType()), Invoice::getType, invoicePageBo.getType())
//                .eq(Objects.nonNull(invoicePageBo.getStatus()), Invoice::getStatus, invoicePageBo.getStatus())
//                .orderByDesc(Invoice::getCreateTime);
//
//        IPage<Invoice> iPage = this.page(initPage, queryWrapper);
//        List<InvoiceVo> voList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
//            for (Invoice record : iPage.getRecords()) {
//                InvoiceVo vo = new InvoiceVo();
//                BeanUtils.copyProperties(record, vo);
//
//                voList.add(vo);
//            }
//        }
//
//        IPage<InvoiceVo> voIPage = PageUtil.copyPage(iPage, voList);
        Page<Invoice> initPage = PageUtil.initPage(invoicePageBo.getPage(), invoicePageBo.getLimit());
        IPage<InvoiceVo> iPage = baseMapper.pageInfo(initPage, invoicePageBo);
        return Result.success(iPage);
    }

    @Override
    public Result<InvoiceVo> info(InvoiceInfoBo invoiceInfoBo) {
        Invoice invoice = this.getById(invoiceInfoBo.getId());
        if (Objects.isNull(invoice)) {
            throw new SqxException("信息不存在");
        }

        InvoiceVo vo = new InvoiceVo();
        BeanUtils.copyProperties(invoice, vo);
        return Result.success(vo);
    }

    @Override
    public Result<Void> updateInfo(InvoiceUpdateBo invoiceUpdateBo) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceUpdateBo, invoice);

        this.updateById(invoice);
        return Result.success();
    }

    @Override
    public Result<Void> delete(InvoiceInfoBo invoiceInfoBo) {
        this.removeById(invoiceInfoBo.getId());
        return Result.success();
    }

    @Override
    public Result orderList(Invoice invoice, Long userId) {
        Page<Orders> pages;
        if (invoice.getPage() != null && invoice.getLimit() != null) {
            pages = new Page<>(invoice.getPage(), invoice.getLimit());
        } else {
            pages = new Page<>(1, Integer.MAX_VALUE);
        }
        // 1. 构建查询条件
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, userId)
                .eq(Orders::getIsInvoice, 1);
        if (StringUtils.isNotBlank(invoice.getStartTime()) && StringUtils.isNotBlank(invoice.getEndTime())) {
            // ge = Greater or Equal (>=), le = Less or Equal (<=)
            wrapper.ge(Orders::getCreateTime, invoice.getStartTime())
                    .le(Orders::getCreateTime, invoice.getEndTime());
        }
        wrapper.eq(Orders::getOrderStatus, 3);
        wrapper.orderByDesc(Orders::getCreateTime);
        Page<Orders> carOrders = ordersDao.selectPage(pages, wrapper);
        return Result.success(carOrders);
    }

    @Override
    public Result<Void> batchSave(Invoice invoice) {
        String[] split = invoice.getOrderIds().trim().split(",");
        for (String orderId : split) {
            ordersDao.update(new Orders(), new LambdaUpdateWrapper<Orders>()
                    .eq(Orders::getOrderId, orderId)
                    .set(Orders::getIsInvoice, 2));
            long count = baseMapper.selectCount(new LambdaQueryWrapper<Invoice>()
                    .eq(Invoice::getOrderId, orderId));
            if (count > 0) {
                log.error("订单" + orderId + "您已经申请开具发票，请勿重复提交！");
                continue;
            }
            Invoice invoices = new Invoice();
            BeanUtils.copyProperties(invoice, invoices);
            invoices.setStatus(1);
            invoice.setOrderId(Long.valueOf(orderId));
            this.save(invoices);
        }
        return Result.success();
    }

    @Override
    public Result<Void> updateStatus(InvoiceInfoBo invoiceInfoBo) {
        Invoice invoice = this.getById(invoiceInfoBo.getId());
        if (Objects.isNull(invoice)) {
            throw new BizException("发票信息不存在");
        }
        this.update(new Invoice(), new LambdaUpdateWrapper<Invoice>()
                .eq(Invoice::getId, invoiceInfoBo.getId())
                .set(Invoice::getStatus, invoiceInfoBo.getStatus())
        );
        if (2 == invoiceInfoBo.getStatus()) {
            ordersDao.update(new Orders(), new LambdaUpdateWrapper<Orders>()
                    .eq(Orders::getOrderId, invoice.getOrderId())
                    .set(Orders::getIsInvoice, 3));
        }
        return Result.success();
    }
}
