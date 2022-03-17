package bluedot.electrochemistry.simplemybatis.binding;

import bluedot.electrochemistry.simplemybatis.constants.Constant;
import bluedot.electrochemistry.simplemybatis.mapping.MappedStatement;
import bluedot.electrochemistry.simplemybatis.session.SqlSession;
import bluedot.electrochemistry.simplemybatis.utils.LogUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 代理类
 * @Author zero
 * @Create 2022/2/10 13:40
 */
public class MapperProxy<T> implements InvocationHandler {


    private final SqlSession sqlSession;

    /**
     * 被代理的接口
     */
    private final Class<T> mapperInterface;

    /**
     * 构造方法
     * @param sqlSession 当前的sqlSession对象
     * @param mapperInterface 需要被代理的接口类
     */
    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            return this.execute(method, args);
        } catch (Exception e) {
            LogUtils.getLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Object execute(Method method, Object[] args) {
        //sql语句的唯一id
        String statementId = this.mapperInterface.getName() + "." + method.getName();

        //System.out.println("statementId=>"+statementId);
        //System.out.println(sqlSession!=null?"sqlSession not null":"sqlSession is null");

        //获取配置类
        //Configuration configuration = this.sqlSession.getConfiguration();
        //System.out.println(configuration!=null?"configuration not null":"configuration is null");
        //System.out.println(sqlSession!=null?"sqlSession not null":"sqlSession is null");
        //MappedStatement mappedStatement1 = configuration.getMappedStatement(statementId);
        //System.out.println(mappedStatement1!=null?"mappedStatement1 not null":"mappedStatement1 is null");
        //获取sql信息
        MappedStatement mappedStatement = this.sqlSession.getConfiguration().getMappedStatement(statementId);
        Object result = null;
        //根据mappedStatement提供的方法选择对应的CRUD方法
        //查询

        //System.out.println(mappedStatement!=null?"mappedStatement not null":"mappedStatement is null");

        //查询
        if (Constant.SqlType.SELECT.value().equals(mappedStatement.getSqlType())) {
            Class<?> returnType = method.getReturnType();
            //返回值为集合类型
            if (Collection.class.isAssignableFrom(returnType)) {
                result = sqlSession.selectList(statementId, args);
            } else {
                result = sqlSession.selectOne(statementId, args);
            }

        }
        //更新
        if (Constant.SqlType.UPDATE.value().equals(mappedStatement.getSqlType())) {
            result = sqlSession.update(statementId, args);
        }
        //插入
        if (Constant.SqlType.INSERT.value().equals(mappedStatement.getSqlType())) {
            result = sqlSession.insert(statementId, args);
        }
        //删除
        if (Constant.SqlType.DELETE.value().equals(mappedStatement.getSqlType())) {
            result = sqlSession.delete(statementId, args);
        }


        return result;
    }
}
