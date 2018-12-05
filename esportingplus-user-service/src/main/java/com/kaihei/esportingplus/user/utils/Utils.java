package com.kaihei.esportingplus.user.utils;

import com.kaihei.esportingplus.marketing.api.event.UserRegistEvent;
import com.kaihei.esportingplus.user.api.params.RegistLoginBaseParam;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.vo.CreateBankMessageVo;
import com.kaihei.esportingplus.user.api.vo.GreetingMessageVo;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import com.kaihei.esportingplus.user.api.vo.RegistLoginContext;
import com.kaihei.esportingplus.user.api.vo.RegistLoginUpdateRtokenMessageVo;
import com.kaihei.esportingplus.user.api.vo.RegistSALoginMessageVo;
import com.kaihei.esportingplus.user.api.vo.RegistTrackSignUpMessageVo;
import com.kaihei.esportingplus.user.api.vo.RegistingMessageVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-14 19:48
 * @Description:
 */
public class Utils {

    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final char[] c = str.toCharArray();

    public static String random(int i) {
        StringBuilder sb = new StringBuilder(i);
        Random r = new Random();
        for (int j = 0; j < i; j++) {
            sb.append(c[r.nextInt(str.length())]);
        }
        return sb.toString();
    }

    public static String generateOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date()) + random(20);
    }

    public static CreateBankMessageVo createBankMessageVo(RegistLoginContext context) {
        if (context.getUserVo() != null) {
            return new CreateBankMessageVo(context.getUserVo().getId(), context.getUserVo().getUid());
        }

        return new CreateBankMessageVo();
    }

    public static GreetingMessageVo greetingMessageVo(RegistLoginContext context) {
        if (context.getUserVo() != null) {
            return new GreetingMessageVo(context.getUserVo().getUid());
        }
        return new GreetingMessageVo();
    }

    public static UserRegistEvent userRegistEvent(String uid, String invitingUid) {
        UserRegistEvent event = new UserRegistEvent();
        event.setUid(invitingUid);//邀请人
        event.setInvitedUid(uid);//被邀请人
        return event;
    }

    public static RegistLoginUpdateRtokenMessageVo updateRtokenVo(String rtoken, String uid) {
        RegistLoginUpdateRtokenMessageVo vo = new RegistLoginUpdateRtokenMessageVo();
        vo.setrToken(rtoken);
        vo.setUid(uid);
        return vo;
    }

    public static RegistingMessageVo registingMessageVo(MembersUser user, String version, String channel) {
        RegistingMessageVo vo = new RegistingMessageVo();
        if (user != null) {
            vo.setNickname(user.getUsername());
            vo.setChickenId(user.getChickenId());
            vo.setPhone(user.getPhone());
            vo.setRegisterTime(user.getDateJoined());
            vo.setRegistWay(user.getRegisterWay());
            vo.setSex(user.getSex());
            vo.setUid(user.getUid());
        }
        vo.setChannel(channel);
        vo.setVersion(version);
        return vo;
    }

    public static RegistingMessageVo registingMessageVo(RegistLoginContext context) {
        RegistingMessageVo vo = new RegistingMessageVo();
        MembersUserVo user = context.getUserVo();
        if (user != null) {
            vo.setNickname(user.getUsername());
            vo.setChickenId(user.getChickenId());
            vo.setPhone(user.getPhone());
            vo.setRegisterTime(user.getDateJoined());
            vo.setRegistWay(user.getRegisterWay());
            vo.setSex(user.getSex());
            vo.setUid(user.getUid());
        }
        if (context.getParams() != null) {
            vo.setChannel(context.getParams().getChannel());
        }
        vo.setVersion(context.getVersion());
        return vo;
    }

    public static RegistSALoginMessageVo registSALoginMessageVo(RegistLoginContext context) {
        RegistSALoginMessageVo vo = new RegistSALoginMessageVo();
        RegistLoginBaseParam param = context.getParams();
        vo.setLastLogin(context.getUserVo().getDateJoined());
        if (param != null) {
            vo.setChannel(param.getChannel());
            if (param instanceof ThirdPartLoginParams) {
                vo.setPlatform(((ThirdPartLoginParams) param).getPlatform());
            }
        }
        if (context.getUserVo() != null) {
            vo.setUid(context.getUserVo().getUid());
        }
        vo.setVersion(context.getVersion());
        return vo;
    }

    public static RegistTrackSignUpMessageVo registTrackSignUpMessageVo(
            RegistLoginContext context) {
        RegistTrackSignUpMessageVo vo = new RegistTrackSignUpMessageVo();
        if (context.getUserVo() != null) {
            vo.setUid(context.getUserVo().getUid());
        }
        if (context.getParams() != null) {
            vo.setSaDistinctId(context.getParams().getSa_distinct_id());
        }
        return vo;
    }

    public static RegistSALoginMessageVo phoneSALoginMessageVo(PhoneLoginContext phoneLoginContext) {
        RegistSALoginMessageVo vo = new RegistSALoginMessageVo();
        vo.setLastLogin(new Date());
        if (phoneLoginContext.getParams() != null) {
            vo.setChannel(phoneLoginContext.getParams().getChannel());
            vo.setPlatform(MembersAuthConstants.REGISTER_WAY_PHONE);
        }
        vo.setUid(phoneLoginContext.getUid());
        vo.setVersion(phoneLoginContext.getVersion());
        return vo;
    }

    public static String getMessageKey(String ... prefix) {
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            for (int i=0; i<prefix.length; i++) {
                sb.append(prefix[i]).append("_");
            }
        }
        sb.append(uuidStringWithoutSeparator());
        return sb.toString();
    }

    public static String uuidStringWithoutSeparator() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(getMessageKey(null));

    }
}
