package bluedot.electrochemistry.simplemybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 负责根据传递的参数值，对statement对象（此处是PreparedStatement）进行赋值
 * @Author zero
 * @Create 2022/2/11 14:19
 */
public class DefaultParameterHandler implements ParameterHandler {
    /**
     * 传入参数
     */
    private final Object parameter;

    public DefaultParameterHandler(Object parameter) {
        this.parameter = parameter;
    }

    /**
     * 设置参数到预处理对象中
     *
     * @param paramPreparedStatement 预处理对象
     */
    @Override
    public void setParameters(PreparedStatement paramPreparedStatement) {
        try {
            if (null != parameter) {

                if (parameter.getClass().isArray()) {
                    Object[] params = (Object[]) parameter;
                    for (int i = 0; i < params.length; i++) {
                        paramPreparedStatement.setObject(i + 1, params[i]);
                    }
                } else {
                    paramPreparedStatement.setObject(1, parameter);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
