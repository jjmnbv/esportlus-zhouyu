package com.kaihei.esportingplus.customer.center.event;

import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.customer.center.data.repository.CompaintItemPictureRepository;
import com.kaihei.esportingplus.customer.center.data.repository.CompaintItemRepository;
import com.kaihei.esportingplus.customer.center.data.repository.CompaintRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.Compaint;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItem;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItemPicture;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateComplaintEventConsumer extends EventConsumer {

    @Autowired
    private CompaintRepository compaintRepository;

    @Autowired
    private CompaintItemRepository compaintItemRepository;

    @Autowired
    private CompaintItemPictureRepository compaintItemPictureRepository;

    /**
     * 创建投诉单
     * @param event
     */
/*    @Subscribe
    @AllowConcurrentEvents*/
    @Transactional(rollbackFor = Exception.class)
    public void createComplaint(CreateComplaintEvent event) {
        //获取Compaint实体
        Compaint compaint = event.getCompaint();
        //Compaint入库
        compaintRepository.insertCompint(compaint);
        //获取CompaintItem实体
        CompaintItem compaintItem = event.getCompaintItem();
        //CompaintItem入库
        compaintItem.setCompaintId(compaint.getId());
        compaintItemRepository.insertCompaintItem(compaintItem);
        //获取ComplainImgUrlList集合
        List<String> complaintImgUrlList = event.getComplainImgUrlList();
        CompaintItemPicture compaintItemPicture = null;
        //封装ComplainImgUrlList
        List<CompaintItemPicture> list = new ArrayList<CompaintItemPicture>();
        for(int i=0; i<complaintImgUrlList.size(); i++){
            compaintItemPicture = new CompaintItemPicture();
            compaintItemPicture.setCompaintId(compaint.getId());
            compaintItemPicture.setUrl(complaintImgUrlList.get(i));
            list.add(compaintItemPicture);
        }
        //ComplainImgUrlList入库
        compaintItemPictureRepository.insertCompaintItemPicture(list);
    }
}
