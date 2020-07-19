package cn.scu.rpc.util;

import cn.scu.rpc.common.RpcFilter;
import cn.scu.rpc.config.ConstantConfig;
import cn.scu.rpc.filter.ActiveFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Created by jim on 2017/10/28.
 */
@Configuration
public class ActiveFilterUtil {

    private static List<Object> getActiveFilter(){


        List<Object> rpcFilterList = Lists.newArrayList();
        Map<String, Object> rpcFilterMapObject = ApplicationContextUtils.getApplicationContext().getBeansWithAnnotation(ActiveFilter.class);
        if (null != rpcFilterMapObject) {

            rpcFilterList = Lists.newArrayList(rpcFilterMapObject.values());
            Collections.sort(rpcFilterList, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {

                    ActiveFilter activeFilterO1 = o1.getClass().getAnnotation(ActiveFilter.class);
                    ActiveFilter activeFilterO2 = o2.getClass().getAnnotation(ActiveFilter.class);
                    return activeFilterO1.order() > activeFilterO2.order() ? 1 : -1;
                }
            });
        }
        return rpcFilterList;
    }

    public static Map<String, RpcFilter> getFilterMap(boolean isServer){

        List<Object> rpcFilterList = getActiveFilter();
        Map<String,RpcFilter> filterMap=new HashMap<>();
        for (Object filterBean : rpcFilterList) {
            Class<?>[] interfaces = filterBean.getClass().getInterfaces();
            ActiveFilter activeFilter=filterBean.getClass().getAnnotation(ActiveFilter.class);
            String includeFilterGroupName=!isServer? ConstantConfig.CONSUMER:ConstantConfig.PROVIDER;
            if(null!=activeFilter.group()&& Arrays.stream(activeFilter.group()).filter(p->p.contains(includeFilterGroupName)).count()==0){
                continue;
            }
            for(Class<?> clazz:interfaces) {
                if(clazz.isAssignableFrom(RpcFilter.class)){
                    filterMap.put(filterBean.getClass().getName(),(RpcFilter) filterBean);
                }
            }
        }
        return filterMap;
    }
}
