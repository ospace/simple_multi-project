package com.tistory.ospace.simpleproject.repository.dto;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

import com.tistory.ospace.core.data.BaseData;

@Alias("Authority")
public class AuthorityDto extends BaseData implements GrantedAuthority {
    private static final long serialVersionUID = 2970833796443419422L;
    
    private Integer userId;
    private String authority;
    
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
