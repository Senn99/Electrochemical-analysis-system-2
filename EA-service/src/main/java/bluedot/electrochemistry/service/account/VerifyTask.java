package bluedot.electrochemistry.service.account;

import bluedot.electrochemistry.dao.BaseMapper;
import bluedot.electrochemistry.pojo.domain.User;
import bluedot.electrochemistry.service.account.verify.AccountVerify;
import bluedot.electrochemistry.service.account.verify.MailVerify;
import bluedot.electrochemistry.service.account.verify.Verify;

/**
 * @author Senn
 * @create 2022/1/13 12:30
 */
public class VerifyTask {

    /**
     * 策略
     */
    Verify verify;

    /**
     * 邮箱或账号
     */
    String account;

    /**
     * 密码
     */
    String password;

    public VerifyTask(String account, String password) {
        if (account.contains("@")) {
            verify = new MailVerify();
        }else {
            verify = new AccountVerify();
        }
        this.account = account;
        this.password = password;
    }

    public User login(BaseMapper mapper) {
        return verify.verifyAccount(mapper, account, password);
    }

    public boolean checkAccount(BaseMapper mapper) {
        return verify.checkAccount(mapper, account);
    }

}