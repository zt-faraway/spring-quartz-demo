package com.dexcoder.demo.service.impl;

import com.codrim.common.utils.json.JsonMapper;
import com.dexcoder.demo.service.Business;
import com.dexcoder.demo.vo.BusinessParamVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * Created by liang.ma on 08/11/2017.
 */

@Service("business")
public class BusinesssImpl implements Business {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void fetchOffer(BusinessParamVO param) {
        logger.info("trigger fetch offer,param:{}", JsonMapper.nonEmptyMapper().toJson(param));
    }
}
