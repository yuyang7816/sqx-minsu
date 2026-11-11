package com.sqx.app.base.invoice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.invoice.domain.bo.InvoiceInfoBo;
import com.sqx.app.base.invoice.domain.bo.InvoicePageBo;
import com.sqx.app.base.invoice.domain.bo.InvoiceSaveBo;
import com.sqx.app.base.invoice.domain.bo.InvoiceUpdateBo;
import com.sqx.app.base.invoice.domain.entity.Invoice;
import com.sqx.app.base.invoice.domain.vo.InvoiceVo;
import com.sqx.common.core.domain.Result;

/**
 * 发票 服务类
 *
 * @author sqx
 * @since 2025-02-21 14:13:26
 */
public interface InvoiceService extends IService<Invoice> {

    Result<Void> saveInfo(InvoiceSaveBo invoiceSaveBo);

    Result<IPage<InvoiceVo>> pageInfo(InvoicePageBo invoicePageBo);

    Result<InvoiceVo> info(InvoiceInfoBo invoiceInfoBo);

    Result<Void> updateInfo(InvoiceUpdateBo invoiceUpdateBo);

    Result<Void> delete(InvoiceInfoBo invoiceInfoBo);

    Result orderList(Invoice invoice,Long userId);

    Result<Void> batchSave(Invoice invoice);

    Result<Void> updateStatus(InvoiceInfoBo invoiceInfoBo);

}
