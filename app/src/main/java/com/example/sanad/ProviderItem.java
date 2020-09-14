package com.sanad.farah.sanad;



public class ProviderItem {
    private int img_id;
    private String provider_name;

    public ProviderItem(int img_id, String provider_name) {
        this.img_id = img_id;
        this.provider_name = provider_name;
    }

    public int getImg_id() {
        return img_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

}
