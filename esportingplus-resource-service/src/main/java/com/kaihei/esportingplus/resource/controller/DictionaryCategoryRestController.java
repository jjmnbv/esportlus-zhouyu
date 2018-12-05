package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("字典分类")
@RestController
@RequestMapping("dictionarycategory")
public class DictionaryCategoryRestController extends
        AbstractDictBaseRestController<DictionaryCategory> {

}
