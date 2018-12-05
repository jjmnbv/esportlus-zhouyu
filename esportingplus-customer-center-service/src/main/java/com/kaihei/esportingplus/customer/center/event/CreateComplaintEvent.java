package com.kaihei.esportingplus.customer.center.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.customer.center.domain.entity.Compaint;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItem;
import java.util.List;

/**
 * 创建投诉单
 */

public class CreateComplaintEvent implements Event {
    private Compaint compaint;
    private CompaintItem compaintItem;
    private List<String> complainImgUrlList;

    public CreateComplaintEvent(){}

    public CreateComplaintEvent(Compaint compaint, CompaintItem compaintItem, List<String> complainImgUrlList ){
        this.compaint = compaint;
        this.compaintItem  = compaintItem;
        this.complainImgUrlList = complainImgUrlList;
    }

    public Compaint getCompaint() {
        return compaint;
    }

    public void setCompaint(Compaint compaint) {
        this.compaint = compaint;
    }

    public CompaintItem getCompaintItem() {
        return compaintItem;
    }

    public void setCompaintItem(
            CompaintItem compaintItem) {
        this.compaintItem = compaintItem;
    }

    public List<String> getComplainImgUrlList() {
        return complainImgUrlList;
    }

    public void setComplainImgUrlList(List<String> complainImgUrlList) {
        this.complainImgUrlList = complainImgUrlList;
    }
}
