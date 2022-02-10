package com.roman.garden.adeasy.ad;

import com.roman.garden.adeasy.util.StringUtil;

import java.util.Objects;

public final class AdItem {

    private String id;
    private String type;
    private int weights;
    private int price;

    public AdItem(String id, String type, int weights, int price) {
        this.id = id;
        this.type = type;
        this.weights = weights;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeights() {
        return weights;
    }

    public int getPrice() {
        return price;
    }

    public void setWeights(int weights) {
        this.weights = weights;
    }

    public boolean valid() {
        if (StringUtil.isEmpty(id) ||
                StringUtil.isEmpty(type))
            return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdItem adItem = (AdItem) o;
        return weights == adItem.weights &&
                price == adItem.price &&
                Objects.equals(id, adItem.id) &&
                Objects.equals(type, adItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, weights, price);
    }
}
