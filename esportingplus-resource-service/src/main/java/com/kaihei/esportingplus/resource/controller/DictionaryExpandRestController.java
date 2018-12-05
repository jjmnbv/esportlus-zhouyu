//package com.kaihei.esportingplus.resource.controller;
//
//import com.kaihei.esportingplus.common.ResponsePacket;
//import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryExpandDictManager;
//import com.kaihei.esportingplus.resource.domain.entity.DictionaryExpand;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
////@RestController
//@RequestMapping("dictionaryexpand")
//public class DictionaryExpandRestController extends
//        AbstractDictBaseRestController<DictionaryExpand> {
//
//    @DeleteMapping("propertyvalue/{id}")
//    public ResponsePacket<Integer> deleteValueByValueId(@PathVariable Integer id) {
//        return ResponsePacket.onSuccess(
//                ((DictionaryExpandDictManager) getSuportManager()).deleteValueByValueId(id));
//    }
//}
