package com.kaihei.esportingplus.gamingteam.data.manager.core.team;

import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.gamingteam.data.manager.core.AbstractPerformer;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition.OnConditionLogic;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene.OnSceneLogic;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.Scene;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 谢思勇
 *
 *
 * 所有Team组件的抽象
 */
public abstract class TeamPerformer<T extends TeamGame> extends AbstractPerformer<T> implements
        InitializingBean {

    @Autowired
    protected PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;

    @Autowired(required = false)
    private List<Scene> scenes;

    private Set<Scene> includesScene;

    private Set<Scene> excludesScene;

    private OnSceneLogic onSceneLogic;

    private OnConditionLogic onConditionLogic;

    @Autowired(required = false)
    private List<Condition> conditions;

    private List<Condition> interestedConditions;

    private List<Condition> excludeConditions;
    /**
     * 判断是否要执行
     *
     * 由子类实现
     *
     * 判断是否是支持的场景
     */
    @Override
    protected boolean onStage() {
        if (this instanceof Condition && includesScene == null && excludesScene == null
                && onSceneLogic == null && interestedConditions == null
                && excludeConditions == null) {
            return true;
        }
        //与逻辑
        boolean result = isAndScene();
        if (result) {
            return true;
        }
        //非逻辑
        result = isOrScene();
        if (result) {
            return true;
        }
        result = isAndCondition();
        if (result) {
            return true;
        }

        //其他条件判断
        return isOrCondition();
    }


    private boolean isAndCondition() {
        if (onConditionLogic == null || !onConditionLogic.equals(OnConditionLogic.AND)) {
            return false;
        }

        if (interestedConditions == null || excludeConditions == null || interestedConditions
                .isEmpty()
                || excludeConditions.isEmpty()) {
            return false;
        }
        boolean includeresult = getConditionResult(interestedConditions);

        boolean excluderesult = getConditionResult(excludeConditions);

        return includeresult && !excluderesult;
    }

    private boolean isOrCondition() {
        if (onConditionLogic == null || !onConditionLogic.equals(OnConditionLogic.OR)) {
            return false;
        }
        //先判断包含
        if (interestedConditions != null && !interestedConditions.isEmpty()) {
            boolean includeResult = getConditionResult(interestedConditions);
            if (includeResult) {
                return true;
            }
        }

        //判断未包含
        if (excludeConditions != null && !excludeConditions.isEmpty()) {
            boolean excludeResult = getConditionResult(excludeConditions);
            if (!excludeResult) {
                return true;
            }
        }
        return false;
    }

    private boolean getConditionResult(List<Condition> conditions) {
        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            if (condition.condition()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAndScene() {
        if (onSceneLogic == null || !onSceneLogic.equals(OnSceneLogic.AND)) {
            return false;
        }

        if (includesScene == null || excludesScene == null || includesScene.isEmpty()
                || excludesScene.isEmpty()) {
            return false;
        } else {
            Scene scene = pvpContextHolder.getScene();
            return includesScene.contains(scene);
        }
    }

    private boolean isOrScene() {
        if (onSceneLogic == null || !onSceneLogic.equals(OnSceneLogic.OR)) {
            return false;
        }
        Scene scene = pvpContextHolder.getScene();

        //当前场景和要求的场景一样
        if (includesScene != null && includesScene.contains(scene)) {
            return true;
        }

        //excludesScene为空、不做判断
        if (excludesScene == null || excludesScene.isEmpty()) {
            return false;
        }

        return !excludesScene.contains(scene);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化支持的场景信息
        initScene();
        //初始化支持的条件信息
        initCondition();
    }

    /**
     * 初始化运行条件
     */
    private void initCondition() {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        OnCondition onCondition = this.getClass().getAnnotation(OnCondition.class);
        if (onCondition == null) {
            return;
        }

        onConditionLogic = onCondition.logic();

        Set<Class<? extends Condition>> conditions = Stream
                .of(onCondition.value()).collect(Collectors.toSet());

        this.interestedConditions = CollectionUtils
                .finds(this.conditions, it -> conditions.contains(it.getClass()));

        Set<Class<? extends Condition>> excludes = Stream
                .of(onCondition.excludes()).collect(Collectors.toSet());

        this.excludeConditions = CollectionUtils
                .finds(this.conditions, it -> excludes.contains(it.getClass()));
    }

    /**
     * 初始化支持的场景参数
     */
    private void initScene() {
        if (scenes == null || scenes.isEmpty()) {
            return;
        }
        OnScene onScene = this.getClass().getAnnotation(OnScene.class);
        if (onScene == null) {
            return;
        }

        onSceneLogic = onScene.logic();

        Set<Class<? extends Scene>> includes = Stream.of(onScene.includes())
                .collect(Collectors.toSet());

        includesScene = new HashSet<>(CollectionUtils
                .finds(scenes, it -> includes.contains(it.getClass())));

        Set<Class<? extends Scene>> excludes = Stream.of(onScene.excludes())
                .collect(Collectors.toSet());
        excludesScene = new HashSet<>(CollectionUtils
                .finds(scenes, it -> excludes.contains(it.getClass())));
    }

    /**
     * 是否支持
     */
    @Override
    protected boolean supported() {
        TeamGame teamGame = pvpContextHolder.getContext().getTeamGame();
        if (teamGame == null) {
            return false;
        }
        return type.isAssignableFrom(teamGame.getClass());
    }
}
