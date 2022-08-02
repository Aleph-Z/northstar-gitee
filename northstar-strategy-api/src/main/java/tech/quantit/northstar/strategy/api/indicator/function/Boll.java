package tech.quantit.northstar.strategy.api.indicator.function;


import tech.quantit.northstar.common.model.TimeSeriesValue;
import tech.quantit.northstar.strategy.api.indicator.TimeSeriesUnaryOperator;

import static tech.quantit.northstar.strategy.api.indicator.function.AverageFunctions.*;

public final class Boll {

    private  int n;

    private  int x;

    private TimeSeriesUnaryOperator ma;

    private TimeSeriesUnaryOperator std;

    private Boll(int n, int x) {
        this.n = n;
        this.x = x;
        this.ma = MA(n);
        this.std = STD(n);
    }

    private static Boll create(int n , int x){
        return new Boll(n,x);
    }

    public static Boll of(int n, int x){
        return create(n, x);
    }

    public TimeSeriesUnaryOperator upper(){
        return tv -> {
            TimeSeriesValue v = ma.apply(tv);
            TimeSeriesValue v0 = std.apply(tv);
            v.setValue(v.getValue() + x * v0.getValue());
            return v;
        };
    }

    public TimeSeriesUnaryOperator lower(){
        return tv -> {
            TimeSeriesValue v = ma.apply(tv);
            TimeSeriesValue v0 = std.apply(tv);
            v.setValue(v.getValue() - x * v0.getValue());
            return v;
        };
    }

    public TimeSeriesUnaryOperator mid(){
        return tv -> {
            TimeSeriesValue v = ma.apply(tv);
            return v;
        };
    }

}
