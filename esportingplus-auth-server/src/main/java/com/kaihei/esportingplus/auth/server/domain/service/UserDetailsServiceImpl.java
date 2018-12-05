package com.kaihei.esportingplus.auth.server.domain.service;

import com.kaihei.esportingplus.auth.server.config.OauthUrlsConfig;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private IAuthRecourcesService authRecourcesService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OauthUrlsConfig oauthUrlsConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String pyToken = httpServletRequest.getHeader("Authorization");
        // 可用性 :true:可用 false:不可用
        boolean enabled = true;
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;

        //获取权限
        Set<GrantedAuthority> authoritys = oauthUrlsConfig.getUrls()
                .stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        return new User(username,
                username,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authoritys);
    }

    //缓存没有从db读取
//    private Set<GrantedAuthority> getGrantedAuthorities() {
//        Set<String> auth_urls = stringRedisTemplate.opsForSet().members(NEED_AUTH_URLS);
//        if (CollectionUtils.isEmpty(auth_urls)) {
//            LOGGER.error(LOAD_AUTHURL_ERROR.getErrMsg());
//            try {
//                List<AuthRecources> authRecources = authRecourcesService.listAll();
//                if (CollectionUtils.isNotEmpty(authRecources)) {
//                    auth_urls = authRecources.stream().map(AuthRecources::getUrl)
//                            .collect(Collectors.toSet());
//                    stringRedisTemplate.opsForSet().add(NEED_AUTH_URLS, auth_urls.toArray(new String[]{}));
//                } else {
//                    throw new BusinessException(BizExceptionEnum.LOAD_AUTHURL_ERROR);
//                }
//            } catch (Exception e) {
//                throw new BusinessException(BizExceptionEnum.LOAD_AUTHURL_ERROR, e);
//            }
//        }
//        Set<GrantedAuthority> grantedAuthorities = auth_urls.stream()
//                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
//
//        return grantedAuthorities;
//    }
}