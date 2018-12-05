package com.kaihei.esportingplus.common.sensors.enums;

public enum SensorsEventEnum {

   REGISTER_SUCCESS("RegisterSuccess","注册成功"),
   LOGIN("Login","登录"),
   FOLLOW("Follow","关注和取关"),
   ACCOUNT_BINDING("AccountBinding","绑定和解绑"),
   START_MATCHING_FREETEAM("StartMatchingFreeTeam","开始匹配免费车队"),
   END_MATCHING_FREETEAM("EndMatchingFreeTeam","结束匹配免费车队"),
   ORDER_STATUSCHANGE_FREETEAM("OrderStatusChangeFreeTeam","免费车队订单状态变更"),
   BUILD_FREETEAM("BuildFreeTeam","创建免费车队"),
   ACTION_FREETEAM("ActionFreeTeam","进入/退出免费车队"),
   DISBAND_FREETEAM("DisbandFreeTeam","解散免费车队"),
   KICK_TEAMMATE_FREETEAM("KickTeammateFreeTeam","暴鸡踢人"),
   START_FREETEAM("StartFreeTeam","暴鸡发车"),
   BOSS_ORDER_FREETEAM("BossOrderFreeTeam","老板订单生成"),
   END_FREETEAM("EndFreeTeam","免费车队结束服务"),
   BAOJI_ORDER_FREETEAM("BaojiOrderFreeTeam","暴鸡订单生成"),
   SUCCESSED_INVITE_FREETEAM("SuccessedInviteFreeTeam","邀请成功"),
   GET_FREE_FREETEAM("GetFreeFreeTeam","获得免单机会"),
   GET_POINT_FREETEAM("GetPointFreeTeam","获得鸡分"),
   USE_POINT_FREETEAM("UsePointFreeTeam","使用鸡分"),
   USER_COMPLAIN_USERCOMPLAIN("UserComplain","用户投诉"),
   USER_REVIEW("UserReview","用户评价");

   private String code;
   private String desc;

   SensorsEventEnum(String code, String desc) {
      this.code = code;
      this.desc = desc;
   }

   public String getCode() {
      return code;
   }

   public String getDesc() {
      return desc;
   }
}
