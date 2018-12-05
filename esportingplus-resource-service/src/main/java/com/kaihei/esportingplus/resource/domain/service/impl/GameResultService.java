package com.kaihei.esportingplus.resource.domain.service.impl;

import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameResultService {

    private final String GAME_RESULT_CATEGORY_CODE = "game_result";
    private final List<String> positiveGameResultCodes = Arrays.asList("sf_victory", "pw_played");
    @Autowired
    private DictionaryDictManager dictionaryDictManager;
    private String catagoryCode = GAME_RESULT_CATEGORY_CODE;

    public List<Dictionary> findPositiveGameResults() {
        return positiveGameResultCodes.parallelStream()
                .map(c -> dictionaryDictManager.findByCodeAndCategoryCode(c, catagoryCode))
                .collect(Collectors.toList());
    }

    public Dictionary findPositiveGameResultByPlaymodeId(Integer playmodeId) {
        Dictionary playmode = dictionaryDictManager.findById(playmodeId);

        return playmode.getChildDictionary().stream()
                .filter(it -> catagoryCode.equals(it.getDictionaryCategory().getCode()))
                .filter(it -> positiveGameResultCodes.contains(it.getCode()))
                .findFirst()
                .orElse(null);
    }
}
