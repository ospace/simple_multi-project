package com.tistory.ospace.simpleproject.util;

import java.util.HashSet;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Set;

import com.tistory.ospace.base.data.SearchDto;
import com.tistory.ospace.common.core.BaseDto;
import com.tistory.ospace.common.util.CmmUtils;
import com.tistory.ospace.common.util.DataUtils;
import com.tistory.ospace.common.util.StringUtils;
import com.tistory.ospace.simpleproject.model.FileInfo;
import com.tistory.ospace.simpleproject.model.SearchKeyword;
import com.tistory.ospace.simpleproject.model.User;
import com.tistory.ospace.simpleproject.repository.dto.CodeDto;
import com.tistory.ospace.simpleproject.repository.dto.FileDto;
import com.tistory.ospace.simpleproject.repository.dto.UserDto;
import com.tistory.ospace.simpleproject.repository.dto.UserPropDto;
import com.tistory.ospace.simpleproject.service.UserPropService;
import com.tistory.ospace.simpleproject.service.UserService;

public class ModelUtils {
    public static Map<String, CodeDto> convertMap(List<CodeDto> data) {
        return DataUtils.map(data, it -> it.getCode(), it -> it);
    }

    public static <T extends BaseDto> void mappingAccountName(UserService accountService, List<T> data) {
        List<UserDto> res = accountService.searchIn(mapId(data));

        if (DataUtils.isEmpty(res)) return;

        Map<Integer, UserDto> accountMap = DataUtils.map(res, it -> it.getId(), it -> it);
        mappingUserName(accountMap, data);
    }

    public static <T extends BaseDto> void mappingUserName(UserPropService userService, List<T> data) {
        List<UserPropDto> res = userService.searchIn(mapId(data));

        if (DataUtils.isEmpty(res)) return;

        Map<Integer, UserDto> userMap = DataUtils.map(res, it -> it.getId(), it -> it);
        mappingUserName(userMap, data);
    }

    private static <T extends BaseDto> void mappingUserName(Map<Integer, UserDto> userMap, List<T> data) {
        DataUtils.forEach(data, it -> {
            UserDto user = null;
            if (null != it.getModifier()) {
                user = userMap.get(it.getModifier());
                if (null != user) it.setModifierName(user.getLoginId());
            }
            if (null != it.getCreator()) {
                user = userMap.get(it.getCreator());
                if (null != user) it.setCreatorName(user.getLoginId());
            }
        });
    }

    private static <T extends BaseDto> Set<Integer> mapId(List<T> data) {
        return DataUtils.reduce(data, (n, it) -> {
            if (null != it.getModifier()) n.add(it.getModifier());
            if (null != it.getCreator()) n.add(it.getCreator());
        }, new HashSet<Integer>());
    }

    public static CodeDto convert(Category from, CodeDto to) {
        if (null == from) return to;

        CmmUtils.copy(from, to, "createDate");

        return to;
    }

    public static SearchDto convert(SearchKeyword from, SearchDto to) {
        if (null == from) return to;

        CmmUtils.copy(from, to);
        to.setType(from.getSearchType());
        to.setKeyword(from.getSearchKeyword());

        return to;
    }

    public static FileInfo convert(FileDto from, FileInfo to) {
        if (null == from) return to;

        CmmUtils.copy(from, to, "filename");
        to.setUrl(createFileUrl(from.getFilename()));

        return to;
    }

    private static String createFileUrl(String filename) {
        if (StringUtils.isEmpty(filename)) return null;

        StringBuilder sb = new StringBuilder();
        sb.append("/file");
        char c = filename.charAt(0);
        if ('/' != c && '\\' != c) {
            sb.append('/');
        }
        sb.append(filename);

        return sb.toString();
    }

    public static User convert(UserDto from, User to) {
        if (null == from) return to;

        CmmUtils.copy(from, to, "useYn", "modifyDate", "password");
        to.setModifyDate(DateHelper.toStringDate(from.getModifyDate()));
        to.setEnable(YN.toBoolean(from.getUseYn()));

        return to;
    }

    public static UserDto convert(User from, UserDto to) {
        if (null == from) return to;

        CmmUtils.copy(from, to, "enable", "modifierName", "modifyDate");
        to.setUseYn(YN.toYn(from.getEnable()));

        return to;
    }

    public static User convert(UserPropDto from, User to) {
        if (null == from) return to;

        CmmUtils.copy(from, to, "password");
        to.setEnable(YN.toBoolean(from.getUseYn()));

        return to;
    }

    public static UserPropDto convert(User from, UserPropDto to) {
        if (null == from) return to;

        CmmUtils.copy(from, to);
        to.setUseYn(YN.toYn(from.getEnable()));

        return to;
    }
}
