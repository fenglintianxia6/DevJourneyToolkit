package dev.journey.toolkit.util;

import java.util.List;

/**
 * Created by mwp on 16/4/20.
 */
public class ListUtils {
    /**
     * 判断list是否为null或空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }
}
