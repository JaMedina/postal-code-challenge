package com.jorge.wcc.security.audith;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.jorge.wcc.security.utils.SecurityUtils;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  public String getCurrentAuditor() {
    String userName = SecurityUtils.getCurrentLogin();
    return (userName != null ? userName : "system");
  }
}
