package dream.soulflame.flameresolveplus.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TabList {
    //主指令
    FIRST_ARG(Arrays.asList("help", "check", "open", "level", "exp","clear", "reload"), 0, null, new int[]{1}),
    //check指令
    CHECK_CHILDREN(Arrays.asList("id", "material"), 1, "check", new int[]{2}),
    //clear指令
    CLEAR_CHILDREN(Collections.singletonList("请输入要清空数据的玩家名"), 1, "clear", new int[]{2}),
    INFO_CHILDREN(Collections.singletonList("请输入要查看数据的玩家名"), 1, "info", new int[]{2}),
    //exp指令
    EXP_CHILDREN_FIRST(Arrays.asList("add", "del", "set"), 1, "exp", new int[]{2}),
    //level指令
    LEVEL_CHILDREN_FIRST(Arrays.asList("add", "del", "set"), 1, "level", new int[]{2}),
    ADD_CHILDREN(Collections.singletonList("请输入要增加的等级/经验 请输入玩家名"), 2, "add", new int[]{3}),
    DEL_CHILDREN(Collections.singletonList("请输入要减少的等级/经验 请输入玩家名"), 2, "del", new int[]{3}),
    SET_CHILDREN(Collections.singletonList("请输入要设置的等级/经验 请输入玩家名"), 2, "set", new int[]{3});
    private final List<String> list;//返回的List
    private final int[] num;//这个参数可以出现的位置
    private final int befPos;//识别的上个参数的位置
    private final String bef;//上个参数的内容

    TabList(List<String> list,int befPos, String bef, int[] num){
        this.list = list;
        this.befPos = befPos;
        this.bef = bef;
        this.num = num.clone();
    }

    public List<String> getList() {
        return list;
    }

    public int[] getNum() {
        return num;
    }

    public int getBefPos() {
        return befPos;
    }

    public String getBef() {
        return bef;
    }

    public static List<String> returnList(String[] Para, int curNum) {
        for(TabList tab : TabList.values()) {
            if(tab.getBefPos() - 1 >= Para.length) continue;
            if((tab.getBef() == null ||
                    tab.getBef().equalsIgnoreCase(Para[tab.getBefPos() - 1])) &&
                    Arrays.binarySearch(tab.getNum(),curNum) >= 0) return tab.getList();
        }
        return null;
    }
}
