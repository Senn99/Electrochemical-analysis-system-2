package bluedot.electrochemistry.simplespring.mvc.processor.impl;

import bluedot.electrochemistry.simplespring.core.BeanContainer;
import bluedot.electrochemistry.simplespring.core.LimitAdapter;
import bluedot.electrochemistry.simplespring.core.RequestURLAdapter;
import bluedot.electrochemistry.simplespring.filter.FilterAdapter;
import bluedot.electrochemistry.utils.LogUtil;
import bluedot.electrochemistry.simplespring.mvc.RequestProcessorChain;
import bluedot.electrochemistry.simplespring.mvc.RequestProcessor;
import bluedot.electrochemistry.simplespring.mvc.processor.render.impl.DefaultResultRender;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 * 请求预处理，包括编码以及路径处理
 * @author Senn
 * @create 2022/1/22 17:03
 */
public class PreRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LogUtil.getLogger("spring.mvc.processor");
    private static final String REQUEST_PATH_END = "/";
    private static final RequestURLAdapter URL_ADAPTER = (RequestURLAdapter) BeanContainer.getInstance().getBean(RequestURLAdapter.class);

    /**
     * 过滤器
     */
    private FilterAdapter filterAdapter;

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletResponse response = requestProcessorChain.getResponse();
        //// 允许跨域访问的域名：若有端口需写全（协议+域名+端口），若没有端口末尾不用加'/'
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080/");
        //// 允许前端带认证cookie：启用此项后，上面的域名不能为'*'，必须指定具体的域名，否则浏览器会提示
        //response.setHeader("Access-Control-Allow-Credentials", "true");
        //// 提示OPTIONS预检时，后端需要设置的两个常用自定义头
        //response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With");

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, PUT,PATCH, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Origin, X-Requested-With, Content-Type, Accept, UserStatus");
        // 接受跨域的cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        LOGGER.debug("请求设置允许跨域");


        //设置请求编码
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");

        // （处理路径是/aaa/bbb，所以如果传入的路径结尾是/aaa/bbb/，就需要处理成/aaa/bbb）
        String requestPath = requestProcessorChain.getRequestPath();
        LOGGER.debug("original requestPath: {}", requestPath);
        if (requestPath.length() > 1 && requestPath.endsWith(REQUEST_PATH_END)) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
            requestProcessorChain.setRequestPath(requestPath);
        }
        if (requestPath.endsWith(REQUEST_PATH_END) || "".equals(requestPath)) {
            requestProcessorChain.setResultRender(new DefaultResultRender());
            return false;
        }
        //TODO 限流
        LimitAdapter.RateLimiter(requestPath);
        //TODO 前置Filter
        if (filterAdapter.needDoBefore()) {
            filterAdapter.doBeforeFilter(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
        }

        LOGGER.debug("preprocess requestMethod: {}, requestPath: {}", requestProcessorChain.getRequestMethod(), requestProcessorChain.getRequestPath());
        return true;
    }

    public void setFilterAdapter(FilterAdapter filterAdapter) {
        this.filterAdapter = filterAdapter;
    }
}
