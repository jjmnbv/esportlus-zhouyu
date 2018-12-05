package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public class UserGameRoleSimpleVo implements Serializable {

  private static final long serialVersionUID = -7156220680560037769L;
  /**
   * 游戏code
   */
  @NotNull(message = "游戏代码不能为空")
  private Integer gameCode;
  
  /**
   * 角色名
   */
  @Size(max = 12,min = 1,message = "角色名长度只能在1-12之间")
  @NotBlank(message = "角色名不能为空")
  private String name;


  /**
   * 一级职业code
   */
  @NotBlank(message = "职业不能为空")
  private String careerCode;
  /**
   * 服务器大区code
   */
  @NotNull(message = "服务器大区不能为空")
  private Integer zoneBigCode;

  /**
   * 服务器小区code
   */
  @NotNull(message = "服务器小区不能为空")
  private Integer zoneSmallCode;

  public Integer getGameCode() {
    return gameCode;
  }

  public void setGameCode(Integer gameCode) {
    this.gameCode = gameCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getCareerCode() {
    return careerCode;
  }

  public void setCareerCode(String careerCode) {
    this.careerCode = careerCode;
  }

  public Integer getZoneBigCode() {
    return zoneBigCode;
  }

  public void setZoneBigCode(Integer zoneBigCode) {
    this.zoneBigCode = zoneBigCode;
  }

  public Integer getZoneSmallCode() {
    return zoneSmallCode;
  }

  public void setZoneSmallCode(Integer zoneSmallCode) {
    this.zoneSmallCode = zoneSmallCode;
  }
}
