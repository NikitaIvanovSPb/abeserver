package ru.nikita.abeserver.domain.dto;

import java.util.List;

public abstract class ListResult<T> {
    private List<T> results;

    public ListResult(List<T> results) {
        this.results = results;
    }

    public List<T> getResults(){
        return results;
    }
    public void setResults(List<T> list){
        this.results = list;
    }
}