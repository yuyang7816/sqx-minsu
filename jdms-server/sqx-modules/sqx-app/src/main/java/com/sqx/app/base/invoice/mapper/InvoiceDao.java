package com.sqx.app.base.invoice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.invoice.domain.bo.InvoicePageBo;
import com.sqx.app.base.invoice.domain.entity.Invoice;
import com.sqx.app.base.invoice.domain.vo.InvoiceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 发票 Mapper 接口
 * </p>
 *
 * @author sqx
 * @since 2025-02-21 14:13:26
 */
@Mapper
public interface InvoiceDao extends BaseMapper<Invoice> {

    IPage<InvoiceVo> pageInfo(Page<Invoice> initPage,
                              @Param("param") InvoicePageBo invoiceInfoPageBo);
}
