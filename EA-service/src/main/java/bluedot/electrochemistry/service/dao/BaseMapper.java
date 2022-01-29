package bluedot.electrochemistry.service.dao;

import bluedot.electrochemistry.service.pojo.domain.User;

import java.util.List;

/**
 * @author Senn
 * @createDate 2021/12/16 19:19
 */
public interface BaseMapper {
    Integer checkEmail(String account);

    User loginByEmail(String account);

}
