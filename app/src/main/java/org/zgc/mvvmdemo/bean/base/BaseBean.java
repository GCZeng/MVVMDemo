package org.zgc.mvvmdemo.bean.base;

/**
 * Base Model
 * Created by Nick on 2017/1/7
 */
public class BaseBean<T> {
    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
