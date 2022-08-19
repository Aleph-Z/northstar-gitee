package tech.quantit.northstar.strategy.api.indicator.function;

import tech.quantit.northstar.strategy.api.utils.collection.ChanArray;
import xyz.redtorch.pb.CoreField.BarField.Builder;

public interface ChanFunctions {

    /**
     * 获取最高值的回溯步长
     * @return
     */
    static int highestPosition(ChanArray<Builder> list, int size) {
        int stepback = 0;
        double highestVal = Double.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (list.get(i).getHighPrice() > highestVal) {
                highestVal = list.get(i).getHighPrice();
                stepback = i;
            }
        }
        return stepback;
    }

    /**
     * 获取最高值
     * @return
     */
    static double highest(ChanArray<Builder> list, int size) {
        double highestVal = Double.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (list.get(i).getHighPrice() > highestVal) {
                highestVal = list.get(i).getHighPrice();
            }
        }
        return highestVal;
    }

    /**
     * 获取最低值的回溯步长
     * @return
     */
    static int lowestPosition(ChanArray<Builder> list, int size) {
        int stepback = 0;
        double lowestVal = Double.MAX_VALUE;
        for(int i=0; i<size; i++) {
            if(list.get(i).getLowPrice() < lowestVal) {
                lowestVal = list.get(i).getLowPrice();
                stepback = i;
            }
        }
        return stepback;
    }

    /**
     * 获取最低值的回溯步长
     * @return
     */
    static double lowest(ChanArray<Builder> list, int size) {
        double lowestVal = Double.MAX_VALUE;
        for(int i=0; i<size; i++) {
            if(list.get(i).getLowPrice() < lowestVal) {
                lowestVal = list.get(i).getLowPrice();
            }
        }
        return lowestVal;
    }
}
