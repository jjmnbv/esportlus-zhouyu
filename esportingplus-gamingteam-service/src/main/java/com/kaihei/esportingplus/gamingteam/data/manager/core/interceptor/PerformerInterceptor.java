package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import com.kaihei.esportingplus.gamingteam.data.manager.core.Performer;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.Scene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.NecessaryPopulator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Operation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Populator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.UnnecessaryPopulator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Validator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterRestoreOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeValidator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class PerformerInterceptor implements InitializingBean {

    protected static Consumer<List<? extends Validator>> validatorConsumer = vc -> Optional
            .ofNullable(vc)
            .filter(it -> !it.isEmpty())
            .ifPresent(it -> it.forEach(Validator::validate));
    protected static Consumer<List<? extends Operation>> operationConsumer = oc -> Optional
            .ofNullable(oc)
            .filter(it -> !it.isEmpty())
            .ifPresent(it -> it.forEach(Operation::operate));
    protected static Consumer<List<EventPublisher>> publisherConsumer = ep -> Optional
            .ofNullable(ep)
            .filter(it -> !it.isEmpty())
            .ifPresent(it -> it.forEach(EventPublisher::publish));

    protected static Consumer<List<UnnecessaryPopulator>> unnecessaryPopulatorConsumer = ep -> Optional
            .ofNullable(ep)
            .filter(it -> !it.isEmpty())
            .ifPresent(it -> it.forEach(Populator::populate));

    protected static Consumer<List<NecessaryPopulator>> necessaryPopulatorConsumer = npc -> Optional
            .ofNullable(npc)
            .filter(it -> !it.isEmpty())
            .ifPresent(it -> it.forEach(Populator::populate));

    @Autowired(required = false)
    protected List<BeforeValidator> beforeValidators;
    @Autowired(required = false)
    protected List<AfterValidator> afterValidators;
    @Autowired(required = false)
    protected List<BeforeOperation> beforeOperations;
    @Autowired(required = false)
    protected List<AfterOperation> afterOperations;
    @Autowired(required = false)
    protected List<AfterRestoreOperation> afterRestoreOperations;
    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;
    @Autowired(required = false)
    protected List<NecessaryPopulator> necessaryPopulators;
    @Autowired(required = false)
    protected List<UnnecessaryPopulator> unnecessaryPopulators;

    @Autowired
    protected PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;
    protected Map<Class<? extends Scene>, Scene> scenesMap;
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired(required = false)
    protected List<Performer> performers;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Scene> sceneMap = applicationContext.getBeansOfType(Scene.class);
        scenesMap = sceneMap.values().stream().collect(Collectors.toMap(Scene::getClass, e -> e));
    }
}
