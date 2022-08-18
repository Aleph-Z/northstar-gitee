package tech.quantit.northstar.strategy.api.indicator.complex;

import org.apache.commons.math3.stat.StatUtils;
import tech.quantit.northstar.common.model.TimeSeriesValue;
import tech.quantit.northstar.strategy.api.utils.collection.ChanArray;
import xyz.redtorch.pb.CoreField.BarField;
import xyz.redtorch.pb.CoreField.BarField.Builder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static tech.quantit.northstar.strategy.api.indicator.function.ChanFunctions.*;
import static tech.quantit.northstar.strategy.api.utils.pb.ChanEnum.DirEnum.*;

public class Chan {

    private static final TimeSeriesValue TV_PLACEHOLDER = new TimeSeriesValue(0, 0);

    /**
     * 处理K线包含
     *
     * @param
     * @return
     */
    public static Function<BarField, BarField> initKx() {
        final AtomicInteger dir = new AtomicInteger();
        ChanArray<Double> h = ChanArray.ofInit(2, 0d);
        ChanArray<Double> l = ChanArray.ofInit(2, 0d);
        final AtomicInteger kLineNumber = new AtomicInteger();
        return bar -> {
            Builder toBuilder = bar.toBuilder();
            long timestamp = bar.getActionTimestamp();
            h.update(bar.getHighPrice());
            l.update(bar.getLowPrice());
            //上包含
            if (dir.get() > DIR_0_VALUE) {
                // 当前K线高点高于前K线高点，并且当前K线地点小于前K线地点  或者 当前K线高点小于前K线高点，并且当前K线低点大于前K线低点
                boolean houBaoQian = h.get() >= h.get(1) && l.get() <= l.get(1);
                boolean qianBaoHou = h.get() <= h.get(1) && l.get() >= l.get(1);
                if (houBaoQian || qianBaoHou) {
                    // 高点的高点 低点的高点
                    h.set(StatUtils.max(h.toDoubleArray()));
                    l.set(StatUtils.max(l.toDoubleArray()));
                    dir.set(DIR_SBH_VALUE);
                }
                //下包含
            } else if (dir.get() < DIR_0_VALUE) {
                // 当前高点大于前K高点，并且当前低点小于前K低点    或者   当前高点小于前高 并且当前低点 大于前低
                boolean houBaoQian = h.get() >= h.get(1) && l.get() <= l.get(1);
                boolean qianBaoHou = h.get() <= h.get(1) && l.get() >= l.get(1);
                if (houBaoQian || qianBaoHou) {
                    // 高点的低点 低点的低点
                    h.set(StatUtils.min(h.toDoubleArray()));
                    l.set(StatUtils.min(l.toDoubleArray()));
                    dir.set(DIR_XBH_VALUE);
                }
            }
            //如果当前高点大于前K高点，并且当前低点大于前K低点，说明这是上涨中
            if (h.get() > h.get(1) && l.get() > l.get(1)) {
                dir.set(DIR_UP_VALUE);
                //反之如果当前高点小于前K高点，当前低点小于前K低点,说明这是下跌中
            } else if (h.get() < h.get(1) && l.get() < l.get(1)) {
                dir.set(DIR_DN_VALUE);
            }
            toBuilder.setDir(dir.get());
            toBuilder.setFlag(DIR_0_VALUE);
            toBuilder.setFxqj(0);
            toBuilder.setBi(DIR_0_VALUE);
            toBuilder.setDuan(DIR_0_VALUE);
            toBuilder.setRhigh(h.get());
            toBuilder.setRlow(l.get());
            toBuilder.setHighPrice(bar.getHighPrice());
            toBuilder.setLowPrice(bar.getLowPrice());
            toBuilder.setNo(kLineNumber.get());
            kLineNumber.set(kLineNumber.incrementAndGet());
            return toBuilder.build();
        };
    }

    /**
     * @Description: 处理顶底分型
     * @Author: lzy
     */
    public static Function<BarField, TimeSeriesValue> initFx() {
        ChanArray<Double> h = ChanArray.ofInit(100, 0d);
        ChanArray<Double> l = ChanArray.ofInit(100, 0d);
        ChanArray<Builder> fxH = ChanArray.of(100);
        ChanArray<Builder> fxL = ChanArray.of(100);
        ChanArray<Builder> kline = ChanArray.of(100);
        ChanArray<Builder> fxKline = ChanArray.of(100);
        ChanArray<TimeSeriesValue> qZb = ChanArray.of(100);
        final AtomicInteger fxNumber = new AtomicInteger(); //分型数量
        final AtomicInteger fxJianGe = new AtomicInteger(); //分型间隔
        final Integer JianGeSize = 6;
        return bar -> {
            TimeSeriesValue result = TV_PLACEHOLDER; // 空值
            h.update(bar.getHighPrice());   //K线 高点
            l.update(bar.getLowPrice());    //K线 低点
            kline.update(bar.toBuilder()); //保存k线
            fxJianGe.set(fxJianGe.incrementAndGet()); //间隔加1
            //至少3根K线后才继续判断
            if (bar.getNo() < 10) {
                return result;
            }
            //判断顶分型，这里忽略了前前K的包含因素，并且多向前判断了1根K线
            boolean dingfx = false;
            if (h.get(1) == h.get(2)) {
                dingfx = h.get() < h.get(1) && h.get(1) > h.get(3);
            }
            if (h.get(1) > h.get(2)) {
                dingfx = h.get() < h.get(1);
            }
            //判断底分型
            boolean difx = false;
            if (l.get(1) == l.get(2)) {
                difx = l.get() > l.get(1) && l.get(1) < l.get(3);
            }
            if (l.get(1) < l.get(2)) {
                difx = l.get() > l.get(1);
            }
            //如果是顶,判断分型K线是自底分型以来最高的
            if (dingfx && fxNumber.get() > 0 && fxL.get() != null) {
                int no = bar.getNo() - fxL.get().getNo();
                dingfx = highestPosition(kline, no) == 1;
            }
            //如果是底，判断分型K线是自顶以来最低的
            if (difx && fxNumber.get() > 0 && fxH.get() != null) {
                int no = bar.getNo() - fxH.get().getNo();
                difx = lowestPosition(kline, no) == 1;
            }

            //分型预处理，第一分型
            if (dingfx && fxNumber.get() == 0) {
                fxH.update(kline.get(1)); //保存高点
                Builder builder1 = kline.get(1); //前K线
                builder1.setFlag(DIR_UP_VALUE);//顶分型
                fxNumber.set(fxNumber.incrementAndGet()); //分型计数+1
                fxKline.update(builder1);//保存分型信息
                fxJianGe.set(0);//间隔归零
                TimeSeriesValue timeSeriesValue = new TimeSeriesValue(builder1.getHighPrice(), builder1.getActionTimestamp());
                qZb.update(timeSeriesValue);
                return timeSeriesValue;
            } else if (difx && fxNumber.get() == 0) {
                fxL.update(kline.get(1)); //保存低点
                Builder builder1 = kline.get(1);//前K线
                builder1.setFlag(DIR_DN_VALUE);//底分型
                fxNumber.set(fxNumber.incrementAndGet()); //分型计数+1
                fxKline.update(builder1);//保存分型信息
                fxJianGe.set(0);//间隔归零
                TimeSeriesValue timeSeriesValue = new TimeSeriesValue(builder1.getLowPrice(), builder1.getActionTimestamp());
                qZb.update(timeSeriesValue);
                return timeSeriesValue;
            }
            //第二个 顶分型处理
            if (dingfx && fxNumber.get() > 0) {
                //顶接着顶,取后顶为新
                if (fxKline.get().getFlag() == DIR_UP_VALUE && h.get(1) >= fxKline.get().getRhigh()) {
                    Builder builder1 = kline.get(1); //前K线
                    builder1.setFlag(DIR_UP_VALUE); //顶分型
                    Builder builderFX = fxKline.get();//前分型K线
                    builderFX.setFlag(DIR_0_VALUE);
                    fxH.update(kline.get(1));//高点更新
                    fxKline.update(builder1);//保存分型信息
                    fxNumber.set(fxNumber.incrementAndGet()); //分型计数+1
                    fxJianGe.set(0);//间隔归零
                    TimeSeriesValue timeSeriesValue = qZb.get();
                    timeSeriesValue.setValue(builder1.getHighPrice());
                    timeSeriesValue.setTimestamp(builder1.getActionTimestamp());
                    return result;
                }
                //前底  当下顶
                if (fxKline.get().getFlag() == DIR_DN_VALUE && fxJianGe.get() >= JianGeSize) {
                    Builder builder1 = kline.get(1); //前K线
                    builder1.setFlag(DIR_UP_VALUE); //顶分型
                    fxH.update(kline.get(1));//高点更新
                    fxKline.update(builder1);//保存分型信息
                    fxNumber.set(fxNumber.incrementAndGet()); //分型计数+1
                    fxJianGe.set(0);//间隔归零
                    TimeSeriesValue timeSeriesValue = new TimeSeriesValue(builder1.getHighPrice(), builder1.getActionTimestamp());
                    qZb.update(timeSeriesValue);
                    return timeSeriesValue;
                }
            }
            //第二个 低分型处理
            if (difx && fxNumber.get() > 0) {
                //底 接着底 ，取后低点为新底
                if (fxKline.get().getFlag() == DIR_DN_VALUE && l.get(1) <= fxKline.get().getRlow()) {
                    Builder builder1 = kline.get(1); //前K线
                    builder1.setFlag(DIR_DN_VALUE); //底分型
                    Builder builderFX = fxKline.get();//前分型K线
                    builderFX.setFlag(DIR_0_VALUE);//归零
                    fxL.update(kline.get(1));//低点更新
                    fxKline.update(builder1);//保存分型信息
                    fxNumber.set(fxNumber.incrementAndGet()); //分型计数+1
                    fxJianGe.set(0);//间隔归零
                    TimeSeriesValue timeSeriesValue = qZb.get();
                    timeSeriesValue.setValue(builder1.getLowPrice());
                    timeSeriesValue.setTimestamp(builder1.getActionTimestamp());
                    return result;
                }
                //前顶 当下 底，顶接着底 ，并且K线间隔大于5
                if (fxKline.get().getFlag() == DIR_UP_VALUE && fxJianGe.get() >= JianGeSize) {
                    Builder builder1 = kline.get(1); //前K线
                    builder1.setFlag(DIR_DN_VALUE); //底分型
                    fxL.update(kline.get(1));//低点更新
                    fxKline.update(builder1);//保存分型信息
                    fxNumber.set(fxNumber.incrementAndGet()); //分型计数+1
                    fxJianGe.set(0);//间隔归零
                    TimeSeriesValue timeSeriesValue = new TimeSeriesValue(builder1.getLowPrice(), builder1.getActionTimestamp());
                    qZb.update(timeSeriesValue);
                    return timeSeriesValue;
                }
            }
            return result;
        };
    }

}
