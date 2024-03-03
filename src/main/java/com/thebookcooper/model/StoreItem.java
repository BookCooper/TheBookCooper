package com.thebookcooper.model;

import com.thebookcooper.util.StoreItemDTO;

import java.math.BigDecimal;

public class StoreItem implements StoreItemDTO {

    private long storeId;
    private String item;
    private BigDecimal itemPrice;
    private String specialOffer;
    private String itemDescription;

    @Override
    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    @Override
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                "storeId=" + storeId +
                ", item='" + item + '\'' +
                ", itemPrice=" + itemPrice +
                ", specialOffer='" + specialOffer + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                '}';
    }
}
