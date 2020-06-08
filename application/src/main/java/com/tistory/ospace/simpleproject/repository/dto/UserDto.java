package com.tistory.ospace.simpleproject.repository.dto;

import java.util.Collection;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tistory.ospace.core.data.BaseDto;
import com.tistory.ospace.simpleproject.util.YN;

@Alias("User")
public class UserDto extends BaseDto implements UserDetails {
	private static final long serialVersionUID = 2324975201345076080L;
	
	private Integer id;
	private String  loginId;
	private String  username;
	private String  password;
	private String  type;
	private YN      useYn;
	private Collection<AuthorityDto> authorities;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YN getUseYn() {
		return useYn;
	}

	public void setUseYn(YN useYn) {
		this.useYn = useYn;
	}
	
    public void setAuthorities(Collection<AuthorityDto> authorities) {
        this.authorities = authorities;
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return YN.toBoolean(this.useYn);
	}
	
}
