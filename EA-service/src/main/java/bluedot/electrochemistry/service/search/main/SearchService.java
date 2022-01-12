package bluedot.electrochemistry.service.search.main;

import bluedot.electrochemistry.service.search.SearchDirection;
import bluedot.electrochemistry.service.search.condition.AccountCondition;
import bluedot.electrochemistry.service.search.condition.Conditional;
import bluedot.electrochemistry.service.search.pages.AccountPage;
import bluedot.electrochemistry.service.search.pages.PageSearchable;
import bluedot.electrochemistry.simplespring.util.JsonUtil;

import java.util.List;

/**
 * @author Senn
 * @create 2021/12/26 17:00
 */
public class SearchService implements SearchModularity{

    SearchDirection direction;

    @Override
    public List<?> doService(Conditional condition, PageSearchable<?> pageSearchable) {
        return pageSearchable.search(condition);
    }



    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }


}
